package com.example.excel2word.controller;

import com.example.excel2word.service.DocService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.*;
import org.docx4j.convert.in.xhtml.XHTMLImporter;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
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

    @PostMapping("/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
        List<String> files = new ArrayList<>();
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select an .xlsx file");
            return "upload";
        }

        try (InputStream is = file.getInputStream(); Workbook wb = WorkbookFactory.create(is)) {
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header
                Cell sttCell = row.getCell(0);
                Cell ngayTlCell = row.getCell(1);
                if (sttCell == null || ngayTlCell == null) continue;
                String stt = cellToString(sttCell);
                String ngayTl = cellToString(ngayTlCell);
                Path doc = docService.createDocFromRow(stt, ngayTl, outputDir);
                files.add(doc.getFileName().toString());
            }
            model.addAttribute("files", files);
        } catch (Exception e) {
            model.addAttribute("error", "Error processing file: " + e.getMessage());
            e.printStackTrace();
        }
        return "upload";
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileName) throws Exception {
        Path file = outputDir.resolve(fileName);
        InputStreamResource resource = new InputStreamResource(java.nio.file.Files.newInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "")
                .contentType(MediaType.parseMediaType("application/msword"))
                .body(resource);
    }

    @GetMapping("/download-template")
    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/templates/SO_THU_LY_KIEM_SAT_THA_DAN_SU_HANH_CHINH.xlsx");
        InputStreamResource inputStream = new InputStreamResource(resource.getInputStream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SO_THU_LY_KIEM_SAT_THA_DAN_SU_HANH_CHINH.xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStream);
    }

    @GetMapping("/download-doc-template")
    public ResponseEntity<InputStreamResource> downloadDocx() throws Exception {

        // đọc file HTML từ classpath
        ClassPathResource resource = new ClassPathResource("templates/temp_doc/phieu_ks.html");
        StringBuilder htmlBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlBuilder.append(line).append("\n");
            }
        }
        String html = htmlBuilder.toString();

        // 1) Unescape HTML named entities -> chuyển tất cả &nbsp;, &ndash;... thành ký tự thực
        // Ví dụ: &nbsp; -> '\u00A0' (non-breaking space)

        // 2) Parse với Jsoup và ép sang XHTML hợp lệ
        Document doc = Jsoup.parse(html);
        doc.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)      // ép thành XHTML
                .escapeMode(Entities.EscapeMode.xhtml)           // escape theo xhtml
                .charset(StandardCharsets.UTF_8)
                .prettyPrint(false);                              // giữ nội dung như gốc (tùy chọn)

        // Jsoup.outerHtml() hoặc .html() đều được; outerHtml() trả cả doctype nếu có
        String xhtml = doc.outerHtml();

        // 3) Convert XHTML -> DOCX
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
        wordMLPackage.getMainDocumentPart().getContent().addAll(importer.convert(xhtml, null));

        File tempFile = File.createTempFile("phieu_ks_", ".docx");
        // 4) Save -> trả về file
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wordMLPackage.save(tempFile);
        byte[] bytes = baos.toByteArray();

        InputStreamResource response = new InputStreamResource(new ByteArrayInputStream(bytes));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=phieu_ks.docx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(response);
    }

    private String cellToString(Cell c) {
        if (c == null) return "";
        if (c.getCellType() == CellType.STRING) return c.getStringCellValue();
        if (c.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(c)) {
                return c.getLocalDateTimeCellValue().toLocalDate().toString();
            } else {
                return String.valueOf((long)c.getNumericCellValue());
            }
        }
        return c.toString();
    }
}
