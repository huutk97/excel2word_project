package com.handler.excel2word.core.export;

import com.handler.excel2word.core.utils.DateUtil;
import com.handler.excel2word.core.utils.LogUtil;
import com.handler.excel2word.dto.SoThuLyKiemSoatDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.*;

import java.lang.reflect.*;

import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.*;
import org.apache.poi.xssf.streaming.*;
import org.apache.poi.ss.usermodel.*;

import java.math.*;
import java.util.*;

import java.io.*;

public class Export {
    private static final Logger logger;
    private static final Map<Class<?>, List<Field>> exportFieldsCache;
    private static final Map<Class<?>, Map<Field, Method>> exportCustTextFields;

    public static List<Field> getClassExportFields(final Class<?> clazz) throws Exception {
        Assert.assertNotNull(clazz);
        final List<Field> result = Export.exportFieldsCache.get(clazz);
        if (result != null) {
            return result;
        }
        ExcelColumn column = null;
        final List<Field> allFields = (List<Field>) ReflectUtils.getClassAndSuperClassFields((Class) clazz);
        final List<Field> exportFields = new ArrayList<Field>();
        final Map<Field, Method> custTextFields = new HashMap<Field, Method>();
        for (final Field field : allFields) {
            column = field.getAnnotation(ExcelColumn.class);
            if (column != null) {
                if (column.skip()) {
                    continue;
                }
                field.setAccessible(true);
                exportFields.add(field);
                if (!org.apache.commons.lang3.StringUtils.isNotEmpty(column.textMethod())) {
                    continue;
                }
                try {
                    custTextFields.put(field, clazz.getMethod(column.textMethod(), (Class<?>[]) new Class[0]));
                } catch (Exception e) {
                    Export.logger.error((clazz.getName() + " get method [ " + column.textMethod() + " ] occurred Exception\uff1a" + e.toString()));
                    throw e;
                }
            }
        }
        if (exportFields.size() > 0) {
            Collections.sort(exportFields, Comparator.comparingInt(f -> f.getAnnotation(ExcelColumn.class).index()));
            Export.exportFieldsCache.put(clazz, exportFields);
            Export.exportCustTextFields.put(clazz, custTextFields);
            return exportFields;
        }
        return null;
    }

    public static void exportPoi2(final Collection<?> dataList,
                                 final Class<?> clazz,
                                 final OutputStream outStream,
                                  final InputStream inputstream,
                                 final String sheetName) {

        SXSSFWorkbook workBook = null;
        try {
            Assert.assertNotNull(clazz);
            Assert.assertNotNull(outStream);

            Sheet sheet;
            int rowIndex = 0;
            Row xRow;

            // Nếu có template
            if (inputstream != null) {
                // Tạm thời giữ nguyên logic cũ của bạn:
                workBook = new SXSSFWorkbook();
                sheet = workBook.getSheetAt(0);
                xRow = sheet.getRow(rowIndex++);
            } else {
                workBook = new SXSSFWorkbook();
                sheet = workBook.createSheet(sheetName);
            }

            // Freeze panes: 2 dòng header
            sheet.createFreezePane(1, 2);

            // -------- STYLE CHUNG --------
            final CellStyle cellStyle = workBook.createCellStyle();
            cellStyle.setBorderBottom((short) 1);
            cellStyle.setBorderTop((short) 1);
            cellStyle.setBorderLeft((short) 1);
            cellStyle.setBorderRight((short) 1);
            cellStyle.setAlignment((short) 2);          // center
            cellStyle.setVerticalAlignment((short) 1);  // center
            cellStyle.setWrapText(true);

            final Font titleFont = workBook.createFont();
            titleFont.setBoldweight((short) 400);
            titleFont.setFontName("Times New Roman");     // FONT
            titleFont.setFontHeightInPoints((short) 12);  // SIZE
            cellStyle.setFont(titleFont);

            final CellStyle titleStyle = workBook.createCellStyle();
            titleStyle.cloneStyleFrom(cellStyle);

            final Font font = workBook.createFont();
            font.setBoldweight((short) 400); // BOLD (POI 3.x)
            titleStyle.setFont(font);
            titleStyle.setFillForegroundColor((short) 11);
            titleStyle.setFillPattern((short) 1); // CellStyle.SOLID_FOREGROUND = 1

            final List<Field> expFields = getClassExportFields(clazz);
            final Map<Field, Method> expCustTextFields = Export.exportCustTextFields.get(clazz);

            Cell cell;
            int cellIndex = 0;

            // ===================== HEADER =====================
            // Nếu là SoThuLyKiemSoatDTO -> header 2 dòng + merge giống HTML
            if (SoThuLyKiemSoatDTO.class.equals(clazz)) {

                // tạo 2 dòng header
                Row headerRow1 = sheet.createRow(rowIndex++);
                Row headerRow2 = sheet.createRow(rowIndex++);

                // set width theo ExcelColumn
                for (Field f : expFields) {
                    ExcelColumn ann = f.getAnnotation(ExcelColumn.class);
                    if (ann != null) {
                        sheet.setColumnWidth(ann.index(), ann.width() * 256);
                    }
                }

                // 1) Các cột rowspan (A đến I) = 9 cột (index 0..8)
                String[] rowspanHeaders = new String[] {
                        "Ngày TL",
                        "Bản án, Quyết định\n(Số; Ngày, tháng, năm; Cơ quan ban hành)",
                        "Người phải thi hành\n(tên địa chỉ)",
                        "Người được thi hành\n(tên địa chỉ)",
                        "QĐ Ủy thác đi\n(Số; Ngày, tháng, năm; Số tiền; Nơi BH)",
                        "QĐ Ủy thác đến\n(Số; Ngày, tháng, năm; Số tiền; Nơi BH)",
                        "QĐ thi hành án dân sự\n(Số; Ngày, tháng, năm; Số tiền)",
                        "Nội dung thi hành\n( Các khoản phải thi hành, số tiền)",
                        "QĐ về việc chưa có điều kiện thi hành án dân sự\n(Số; Ngày, tháng, năm; Số tiền)",
                        "QĐ rút Quyết định THA\n(Số; Ngày, tháng, năm; Số tiền)"
                };

                cellIndex = 0;
                for (String title : rowspanHeaders) {
                    cell = headerRow1.createCell(cellIndex);
                    cell.setCellStyle(titleStyle);
                    cell.setCellValue(title);

                    // merge row 0..1 cho cột này
                    sheet.addMergedRegion(
                            new org.apache.poi.ss.util.CellRangeAddress(
                                    headerRow1.getRowNum(), headerRow2.getRowNum(),
                                    cellIndex, cellIndex
                            )
                    );
                    cellIndex++;
                }

                // 2) Nhóm "QĐ hoãn thi hành án Dân sự" (colspan 2)
                int startColHoan = cellIndex; // 9
                cell = headerRow1.createCell(startColHoan);
                cell.setCellStyle(titleStyle);
                cell.setCellValue("QĐ hoãn thi hành án Dân sự");
                // merge 1 ô trên: row1, col 9..10
                sheet.addMergedRegion(
                        new org.apache.poi.ss.util.CellRangeAddress(
                                headerRow1.getRowNum(), headerRow1.getRowNum(),
                                startColHoan, startColHoan + 1
                        )
                );

                // dòng 2: 2 ô con
                cell = headerRow2.createCell(startColHoan);
                cell.setCellStyle(titleStyle);
                cell.setCellValue("Số; Ngày, tháng, năm; Lý do; Số tiền");

                cell = headerRow2.createCell(startColHoan + 1);
                cell.setCellStyle(titleStyle);
                cell.setCellValue("QĐ tiếp tục THA\n(Số; Ngày, tháng, năm)");

                cellIndex = startColHoan + 2; // 11

                // 3) Nhóm "QĐ tạm đình chỉ thi hành án dân sự" (colspan 2)
                int startColTamDinhChi = cellIndex; // 11
                cell = headerRow1.createCell(startColTamDinhChi);
                cell.setCellStyle(titleStyle);
                cell.setCellValue("QĐ tạm đình chỉ thi hành án dân sự");
                sheet.addMergedRegion(
                        new org.apache.poi.ss.util.CellRangeAddress(
                                headerRow1.getRowNum(), headerRow1.getRowNum(),
                                startColTamDinhChi, startColTamDinhChi + 1
                        )
                );

                cell = headerRow2.createCell(startColTamDinhChi);
                cell.setCellStyle(titleStyle);
                cell.setCellValue("Số; Ngày, tháng, năm; Lý do; Số tiền");

                cell = headerRow2.createCell(startColTamDinhChi + 1);
                cell.setCellStyle(titleStyle);
                cell.setCellValue("QĐ tiếp tục THA\n(Số; Ngày, tháng, năm)");

                cellIndex = startColTamDinhChi + 2; // 13

                // 4) Các cột rowspan cuối: O, P, Q (index 13..15)
                String[] lastHeaders = new String[] {
                        "QĐ đình chỉ thi hành án dân sự\n(Số; Ngày, tháng, năm; Số tiền)",
                        "Đã thi hành xong\n(Số; Ngày, tháng, năm; Số tiền)",
                        "Ghi chú\n(Ghi các thông tin như tên chấp hành viên; Vi phạm..)",
                        "Về thời hạn gửi Quyết định",
                        "Về căn cứ ban hành Quyết định",
                        "Về thẩm quyền ban hành Quyết định",
                        "Về hình thức của Quyết định",
                        "Về nội dung của Quyết định",
                        "Những nội dung khác",
                        "Quan điểm của KSV"
                };

                for (String title : lastHeaders) {
                    cell = headerRow1.createCell(cellIndex);
                    cell.setCellStyle(titleStyle);
                    cell.setCellValue(title);

                    sheet.addMergedRegion(
                            new org.apache.poi.ss.util.CellRangeAddress(
                                    headerRow1.getRowNum(), headerRow2.getRowNum(),
                                    cellIndex, cellIndex
                            )
                    );
                    cellIndex++;
                }

            } else {
                // ===================== HEADER MẶC ĐỊNH (CHO CLASS KHÁC) =====================
                Row headerRow = sheet.createRow(rowIndex++);

                for (final Field field : expFields) {
                    ExcelColumn ann = field.getAnnotation(ExcelColumn.class);
                    if (ann == null) continue;

                    sheet.setColumnWidth(cellIndex, ann.width() * 256);
                    cell = headerRow.createCell(cellIndex++);
                    cell.setCellStyle(titleStyle);
                    cell.setCellValue(ann.name());
                }
            }

            // ===================== GHI DATA =====================
            if (CollectionUtils.isNotEmpty((Collection) dataList)) {
                try {
                    for (final Object vo : dataList) {
                        Row dataRow = sheet.createRow(rowIndex++);
                        cellIndex = 0;

                        for (final Field field2 : expFields) {
                            ExcelColumn ann = field2.getAnnotation(ExcelColumn.class);
                            if (ann == null) continue;

                            Object fieldVal;
                            if (expCustTextFields != null && expCustTextFields.get(field2) != null) {
                                try {
                                    fieldVal = expCustTextFields.get(field2).invoke(vo);
                                } catch (Exception e5) {
                                    fieldVal = "DỮ LIỆU LỖI";
                                }
                            } else {
                                field2.setAccessible(true);
                                fieldVal = field2.get(vo);
                            }

                            cell = dataRow.createCell(cellIndex++);
                            cell.setCellStyle(cellStyle);

                            if (isNumberType(field2.getType()) &&
                                    (fieldVal == null || fieldVal instanceof Number)) {
                                cell.setCellType(0);
                                cell.setCellValue((double) getNumberValue(fieldVal));
                            } else {
                                cell.setCellValue(getStringValue(fieldVal));
                            }
                        }
                    }
                } catch (Exception e) {
                    sheet.createRow(rowIndex++).createCell(0).setCellValue(e.toString());
                    LogUtil.error(e.toString());
                }
            }

            workBook.write(outStream);
        } catch (Exception e2) {
            LogUtil.error(e2.toString());
        } finally {
            try {
                outStream.flush();
                outStream.close();
            } catch (IOException e3) {
                LogUtil.error(e3.toString());
            }
        }
    }

    private static int createRowSpanColumn(Sheet sheet, Row row1, Row row2, int col,
                                           String title, CellStyle style) {
        createCell(row1, col, title, style);

        // merge row 0 -> row 1
        sheet.addMergedRegion(new CellRangeAddress(0, 1, col, col));

        return col + 1;
    }

    private static void createCell(Row row, int col, String text, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(text);
        cell.setCellStyle(style);
    }

    public static void exportPoi(final Collection<?> dataList, final Class<?> clazz, final OutputStream outStream, final InputStream inputstream, final String sheetName) {
        try {
            Assert.assertNotNull(clazz);
            Assert.assertNotNull(outStream);
            SXSSFWorkbook workBook = null;
            Sheet sheet = null;
            int rowIndex = 0;
            Row xRow = null;
            if (inputstream != null) {
                workBook = new SXSSFWorkbook();
                sheet = workBook.getSheetAt(0);
                xRow = sheet.getRow(rowIndex++);
            } else {
                workBook = new SXSSFWorkbook();
                sheet = workBook.createSheet(sheetName);
                xRow = sheet.createRow(rowIndex++);
            }
            sheet.createFreezePane(1, 1);
            final CellStyle cellStyle = workBook.createCellStyle();
            cellStyle.setBorderBottom((short) 1);
            cellStyle.setBorderTop((short) 1);
            cellStyle.setBorderLeft((short) 1);
            cellStyle.setBorderRight((short) 1);
            cellStyle.setAlignment((short) 2);
            cellStyle.setVerticalAlignment((short) 1);
            cellStyle.setWrapText(true);
            final CellStyle titleStyle = workBook.createCellStyle();
            titleStyle.cloneStyleFrom(cellStyle);
            final Font font = workBook.createFont();
            font.setBoldweight((short) 700);
            titleStyle.setFont(font);
            titleStyle.setFillForegroundColor((short) 11);
            titleStyle.setFillPattern((short) 1);
            final List<Field> expFields = getClassExportFields(clazz);
            final Map<Field, Method> expCustTextFields = Export.exportCustTextFields.get(clazz);
            Cell cell = null;
            int cellIndex = 0;
            for (final Field field : expFields) {
                if (field.getAnnotation(ExcelColumn.class).isJavaBean()) {
                    for (final Field subField : getClassExportFields(field.getType())) {
                        sheet.setColumnWidth(cellIndex, subField.getAnnotation(ExcelColumn.class).width() * 256);
                        cell = xRow.createCell(cellIndex++);
                        cell.setCellStyle(titleStyle);
                        cell.setCellValue(field.getAnnotation(ExcelColumn.class).name() + subField.getAnnotation(ExcelColumn.class).name());
                    }
                } else {
                    sheet.setColumnWidth(cellIndex, field.getAnnotation(ExcelColumn.class).width() * 256);
                    cell = xRow.createCell(cellIndex++);
                    cell.setCellStyle(titleStyle);
                    cell.setCellValue(field.getAnnotation(ExcelColumn.class).name());
                }
            }
            if (CollectionUtils.isNotEmpty((Collection) dataList)) {
                try {
                    for (final Object vo : dataList) {
                        xRow = sheet.createRow(rowIndex++);
                        cellIndex = 0;
                        for (final Field field2 : expFields) {
                            Object fieldVal;
                            if (expCustTextFields.get(field2) != null) {
                                try {
                                    fieldVal = expCustTextFields.get(field2).invoke(vo, new Object[0]);
                                } catch (Exception e5) {
                                    fieldVal = "\u975e\u6b63\u5e38\u5165\u6b3e";
                                }
                            } else {
                                fieldVal = field2.get(vo);
                            }
                            if (field2.getAnnotation(ExcelColumn.class).isJavaBean()) {
                                for (final Field subField2 : getClassExportFields(field2.getType())) {
                                    cell = xRow.createCell(cellIndex++);
                                    cell.setCellStyle(cellStyle);
                                    final Object subFieldValue = subField2.get(fieldVal);
                                    if (isNumberType(subField2.getType()) && (subFieldValue == null || subFieldValue instanceof Number)) {
                                        cell.setCellType(0);
                                        cell.setCellValue((double) getNumberValue(subFieldValue));
                                    } else {
                                        cell.setCellValue(getStringValue(subFieldValue));
                                    }
                                }
                            } else {
                                cell = xRow.createCell(cellIndex++);
                                cell.setCellStyle(cellStyle);
                                if (isNumberType(field2.getType()) && (fieldVal == null || fieldVal instanceof Number)) {
                                    cell.setCellType(0);
                                    cell.setCellValue((double) getNumberValue(fieldVal));
                                } else {
                                    cell.setCellValue(getStringValue(fieldVal));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    sheet.createRow(rowIndex++).createCell(0).setCellValue(e.toString());
                    LogUtil.error(e.toString());
                }
            }
            workBook.write(outStream);
        } catch (Exception e2) {
            LogUtil.error(e2.toString());
            try {
                outStream.flush();
                outStream.close();
            } catch (IOException e3) {
                LogUtil.error(e3.toString());
            }
        } finally {
            try {
                outStream.flush();
                outStream.close();
            } catch (IOException e4) {
                LogUtil.error(e4.toString());
            }
        }
    }

    public static void exportPoi(final SXSSFWorkbook workBook, final Collection<?> dataList, final Class<?> clazz, final OutputStream outStream, final String sheetName, final int sheetNum) {
        try {
            Assert.assertNotNull(clazz);
            Assert.assertNotNull(outStream);
            Sheet sheet = null;
            int rowIndex = 0;
            Row xRow = null;
            sheet = workBook.createSheet();
            workBook.setSheetName(sheetNum, sheetName);
            xRow = sheet.createRow(rowIndex++);
            sheet.createFreezePane(1, 1);
            final CellStyle cellStyle = workBook.createCellStyle();
            cellStyle.setBorderBottom((short) 1);
            cellStyle.setBorderTop((short) 1);
            cellStyle.setBorderLeft((short) 1);
            cellStyle.setBorderRight((short) 1);
            cellStyle.setAlignment((short) 2);
            cellStyle.setVerticalAlignment((short) 1);
            cellStyle.setWrapText(true);
            final CellStyle titleStyle = workBook.createCellStyle();
            titleStyle.cloneStyleFrom(cellStyle);
            final Font font = workBook.createFont();
            font.setBoldweight((short) 700);
            titleStyle.setFont(font);
            titleStyle.setFillForegroundColor((short) 11);
            titleStyle.setFillPattern((short) 1);
            final List<Field> expFields = getClassExportFields(clazz);
            final Map<Field, Method> expCustTextFields = Export.exportCustTextFields.get(clazz);
            Cell cell = null;
            int cellIndex = 0;
            for (final Field field : expFields) {
                if (field.getAnnotation(ExcelColumn.class).isJavaBean()) {
                    for (final Field subField : getClassExportFields(field.getType())) {
                        sheet.setColumnWidth(cellIndex, subField.getAnnotation(ExcelColumn.class).width() * 256);
                        cell = xRow.createCell(cellIndex++);
                        cell.setCellStyle(titleStyle);
                        cell.setCellValue(field.getAnnotation(ExcelColumn.class).name() + subField.getAnnotation(ExcelColumn.class).name());
                    }
                } else {
                    sheet.setColumnWidth(cellIndex, field.getAnnotation(ExcelColumn.class).width() * 256);
                    cell = xRow.createCell(cellIndex++);
                    cell.setCellStyle(titleStyle);
                    cell.setCellValue(field.getAnnotation(ExcelColumn.class).name());
                }
            }
            if (CollectionUtils.isNotEmpty((Collection) dataList)) {
                try {
                    for (final Object vo : dataList) {
                        xRow = sheet.createRow(rowIndex++);
                        cellIndex = 0;
                        for (final Field field2 : expFields) {
                            Object fieldVal;
                            if (expCustTextFields.get(field2) != null) {
                                try {
                                    fieldVal = expCustTextFields.get(field2).invoke(vo, new Object[0]);
                                } catch (Exception e3) {
                                    fieldVal = "";
                                }
                            } else {
                                fieldVal = field2.get(vo);
                            }
                            if (field2.getAnnotation(ExcelColumn.class).isJavaBean()) {
                                for (final Field subField2 : getClassExportFields(field2.getType())) {
                                    cell = xRow.createCell(cellIndex++);
                                    cell.setCellStyle(cellStyle);
                                    final Object subFieldValue = subField2.get(fieldVal);
                                    if (isNumberType(subField2.getType()) && (subFieldValue == null || subFieldValue instanceof Number)) {
                                        cell.setCellType(0);
                                        cell.setCellValue((double) getNumberValue(subFieldValue));
                                    } else {
                                        cell.setCellValue(getStringValue(subFieldValue));
                                    }
                                }
                            } else {
                                cell = xRow.createCell(cellIndex++);
                                cell.setCellStyle(cellStyle);
                                if (isNumberType(field2.getType()) && (fieldVal == null || fieldVal instanceof Number)) {
                                    cell.setCellType(0);
                                    final double value = getNumberValue(fieldVal);
                                    cell.setCellValue((double) getNumberValue(fieldVal));
                                    if (!field2.getAnnotation(ExcelColumn.class).haveColor()) {
                                        continue;
                                    }
                                    final CellStyle style = workBook.createCellStyle();
                                    style.cloneStyleFrom(cellStyle);
                                    final Font f = workBook.createFont();
                                    if (value < 0.0) {
                                        f.setColor((short) 10);
                                        style.setFont(f);
                                    } else if (value > 0.0) {
                                        f.setColor((short) 17);
                                        style.setFont(f);
                                    }
                                    cell.setCellStyle(style);
                                } else {
                                    cell.setCellValue(getStringValue(fieldVal));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    sheet.createRow(rowIndex++).createCell(0).setCellValue(e.toString());
                    LogUtil.error(e.toString());
                }
            }
        } catch (Exception e2) {
            LogUtil.error(e2.toString());
        }
    }

    private static boolean isNumberType(final Class clazz) {
        return Integer.class.equals(clazz) || Double.class.equals(clazz) || Long.class.equals(clazz) || Float.class.equals(clazz) || Short.class.equals(clazz) || Byte.class.equals(clazz) || Integer.TYPE.equals(clazz) || Double.TYPE.equals(clazz) || Long.TYPE.equals(clazz) || Float.TYPE.equals(clazz) || Short.TYPE.equals(clazz) || Byte.TYPE.equals(clazz);
    }

    public static Double getNumberValue(final Object value) {
        if (value instanceof Double || value instanceof Float) {
            return ((Number) value).doubleValue();
        }
        return (value == null) ? 0.0 : Double.valueOf(String.valueOf(value));
    }

    public static String getStringValue(final Object value) {
        if (value instanceof Date) {
            return DateUtil.dateToYMDHMS((Date) value);
        }
        return (value == null) ? "" : String.valueOf(value);
    }

    public static void main(final String[] args) {
        System.out.print(new BigDecimal(Double.valueOf(23.222235)).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    static {
        logger = Logger.getLogger((Class) Export.class);
        exportFieldsCache = new HashMap<Class<?>, List<Field>>(128);
        exportCustTextFields = new HashMap<Class<?>, Map<Field, Method>>(128);
    }
}
