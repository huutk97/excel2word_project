package com.handler.excel2word.core.export;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.handler.excel2word.core.utils.DateUtil;
import com.handler.excel2word.core.utils.LogUtil;
import com.handler.excel2word.dto.SoThuLyKiemSoatDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.*;

import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import org.apache.poi.xssf.streaming.*;
import org.apache.poi.ss.usermodel.*;

import java.math.*;
import java.util.*;

import java.io.*;

@Slf4j
public class Export {
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
                    Export.log.error((clazz.getName() + " get method [ " + column.textMethod() + " ] occurred Exception\uff1a" + e.toString()));
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

        Assert.assertNotNull(clazz);
        Assert.assertNotNull(outStream);

        // workbook sẽ là SXSSFWorkbook để ghi streaming
        SXSSFWorkbook sxWorkbook = null;
        Workbook templateWorkbook = null; // chỉ để đóng nếu dùng template

        try {
            Sheet sheet;
            int rowIndex = 0;

            if (inputstream != null) {
                // Mở workbook từ template, sau đó bọc bằng SXSSFWorkbook (streaming)
                templateWorkbook = WorkbookFactory.create(inputstream);
                // Convert template Workbook -> SXSSFWorkbook
                sxWorkbook = new SXSSFWorkbook((XSSFWorkbook) templateWorkbook);
                // Lấy sheet đầu tiên (có sẵn trong template)
                sheet = sxWorkbook.getSheetAt(0);
                // Nếu muốn giữ rowIndex từ template, có thể detect lastRowNum
                rowIndex = sheet.getLastRowNum() + 1;
            } else {
                sxWorkbook = new SXSSFWorkbook();
                // tên sheet an toàn
                String safeName = WorkbookUtil.createSafeSheetName(sheetName == null ? "Sheet1" : sheetName);
                sheet = sxWorkbook.createSheet(safeName);
                rowIndex = 0;
            }

            // Freeze panes: 2 dòng header (colSplit, rowSplit)
            sheet.createFreezePane(0, 2);

            // -------- STYLE CHUNG --------
            final CellStyle cellStyle = sxWorkbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setWrapText(true);

            final Font titleFont = sxWorkbook.createFont();
            titleFont.setBold(false); // nếu muốn normal
            titleFont.setFontName("Times New Roman");
            titleFont.setFontHeightInPoints((short) 12);
            cellStyle.setFont(titleFont);

            final CellStyle titleStyle = sxWorkbook.createCellStyle();
            titleStyle.cloneStyleFrom(cellStyle);

            final Font headerFont = sxWorkbook.createFont();
            headerFont.setBold(true); // tiêu đề in đậm
            headerFont.setFontName("Times New Roman");
            headerFont.setFontHeightInPoints((short) 12);
            titleStyle.setFont(headerFont);

            // set background màu xám nhạt cho header
            titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Gọi hàm writeData của bạn (giữ nguyên) - giả định phương thức tồn tại
            writeData(clazz, dataList, sheet, rowIndex, titleStyle, cellStyle);

            // Ghi workbook ra OutputStream
            sxWorkbook.write(outStream);
            outStream.flush();

        } catch (InvalidFormatException ife) {
            LogUtil.error("InvalidFormatException: " + ife.getMessage());
        } catch (Exception e) {
            LogUtil.error("Export error: " + e.toString());
        } finally {
            // Đóng workbook / template
            try {
                if (sxWorkbook != null) {
                    sxWorkbook.dispose(); // giải phóng temp files của SXSSFWorkbook
                    sxWorkbook.close();
                }
                if (templateWorkbook != null) {
                    templateWorkbook.close();
                }
            } catch (IOException ioe) {
                LogUtil.error("Close workbook error: " + ioe.toString());
            }

            // Đóng outStream nếu cần (nếu caller muốn tự quản lý thì bỏ dòng này)
            try {
                outStream.close();
            } catch (IOException e) {
                LogUtil.error(e.toString());
            }
        }
    }

    private static void writeData(final Class<?> clazz, final Collection<?> dataList,
                                  final Sheet sheet, int rowIndex,
                                  CellStyle titleStyle, CellStyle cellStyle) throws Exception {
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
                            cell.setCellType(CellType.NUMERIC);
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
        exportFieldsCache = new HashMap<Class<?>, List<Field>>(128);
        exportCustTextFields = new HashMap<Class<?>, Map<Field, Method>>(128);
    }
}
