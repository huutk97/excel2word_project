package com.handler.excel2word.controller;

import com.handler.excel2word.service.DocService;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.ss.usermodel.*;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelToDocController {

    private final DocService docService;
    private final Path outputDir = Paths.get("output");

    public ExcelToDocController(DocService docService) {
        this.docService = docService;
    }

    @GetMapping("/")
    public String index() { return "upload"; }

//    @PostMapping("/upload")
//    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
//        List<String> files = new ArrayList<>();
//        if (file.isEmpty()) {
//            model.addAttribute("error", "Please select an .xlsx file");
//            return "upload";
//        }
//
//        try (InputStream is = file.getInputStream(); Workbook wb = WorkbookFactory.create(is)) {
//            Sheet sheet = wb.getSheetAt(0);
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) continue; // skip header
//                Cell sttCell = row.getCell(0);
//                Cell ngayTlCell = row.getCell(1);
//                if (sttCell == null || ngayTlCell == null) continue;
//                String stt = cellToString(sttCell);
//                String ngayTl = cellToString(ngayTlCell);
//                Path doc = docService.createDocFromRow(stt, ngayTl, outputDir);
//                files.add(doc.getFileName().toString());
//            }
//            model.addAttribute("files", files);
//        } catch (Exception e) {
//            model.addAttribute("error", "Error processing file: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return "upload";
//    }
//
//    @GetMapping("/download/{fileName:.+}")
//    public ResponseEntity<InputStreamResource> download(@PathVariable String fileName) throws Exception {
//        Path file = outputDir.resolve(fileName);
//        InputStreamResource resource = new InputStreamResource(java.nio.file.Files.newInputStream(file));
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "")
//                .contentType(MediaType.parseMediaType("application/msword"))
//                .body(resource);
//    }
//
//    @GetMapping("/download-template")
//    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
//        ClassPathResource resource = new ClassPathResource("static/templates/SO_THU_LY_KIEM_SAT_THA_DAN_SU_HANH_CHINH.xlsx");
//        InputStreamResource inputStream = new InputStreamResource(resource.getInputStream());
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SO_THU_LY_KIEM_SAT_THA_DAN_SU_HANH_CHINH.xlsx\"")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(inputStream);
//    }

    @GetMapping("/download-doc-template")
    public ResponseEntity<InputStreamResource> downloadDocx() throws Exception {
        // ---- Đọc file HTML 1 ----
        ClassPathResource res = new ClassPathResource("templates/temp_doc/phieu_ks.html");
        String html = readFileToString(res);

        // ---- Convert sang XHTML ----
        Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml4(html));

        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml)
                .charset(StandardCharsets.UTF_8).prettyPrint(false);

        String xhtml1 = doc.outerHtml();

        // ---- Tạo document Word A4 ----
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, false);
        org.docx4j.wml.ObjectFactory factory = new org.docx4j.wml.ObjectFactory();

        // ---- Set font Times New Roman ----
        org.docx4j.wml.RFonts rfonts = factory.createRFonts();
        rfonts.setAscii("Times New Roman");
        rfonts.setHAnsi("Times New Roman");
        rfonts.setCs("Times New Roman");
        rfonts.setEastAsia("Times New Roman");

        org.docx4j.wml.Style normalStyle = wordMLPackage.getMainDocumentPart()
                .getStyleDefinitionsPart().getStyleById("Normal");
        if (normalStyle != null) {
            if (normalStyle.getRPr() == null) normalStyle.setRPr(factory.createRPr());
            normalStyle.getRPr().setRFonts(rfonts);
        }

        // ---- Convert HTML 1 → Word ----
        XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
        wordMLPackage.getMainDocumentPart().getContent().addAll(importer.convert(xhtml1, null));

        // ---- Thêm ngắt trang (page break) ----
        org.docx4j.wml.Br breakObj = factory.createBr();
        breakObj.setType(org.docx4j.wml.STBrType.PAGE);
        org.docx4j.wml.P paragraph = factory.createP();
        paragraph.getContent().add(breakObj);
        wordMLPackage.getMainDocumentPart().addObject(paragraph);

        // ---- Xuất file ----
        File tempFile = File.createTempFile("phieu_ks_", ".docx");
        wordMLPackage.save(tempFile);

        byte[] bytes = Files.readAllBytes(tempFile.toPath());
        tempFile.delete();

        InputStreamResource response = new InputStreamResource(new ByteArrayInputStream(bytes));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=phieu_ks_2page.docx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(response);
    }

    private String readFileToString(ClassPathResource resource) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
//
//    private String cellToString(Cell c) {
//        if (c == null) return "";
//        if (c.getCellType() == CellType.STRING) return c.getStringCellValue();
//        if (c.getCellType() == CellType.NUMERIC) {
//            if (DateUtil.isCellDateFormatted(c)) {
//                return c.getLocalDateTimeCellValue().toLocalDate().toString();
//            } else {
//                return String.valueOf((long)c.getNumericCellValue());
//            }
//        }
//        return c.toString();
//    }
}
