package com.handler.excel2word.handlerApi.controller;

import com.handler.excel2word.core.export.Export;
import com.handler.excel2word.core.utils.DateUtil;
import com.handler.excel2word.dto.SoThuLyKiemSoatDTO;
import com.handler.excel2word.entity.SoThuLyKiemSoat;
import com.handler.excel2word.handlerApi.Interface.SoThuLyService;
import com.handler.excel2word.handlerApi.dto.SoThuLyDTO;
import lombok.RequiredArgsConstructor;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Style;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/so-thu-ly")
@RequiredArgsConstructor
public class SoThuLyController {

    private final SoThuLyService service;
    private final TemplateEngine templateEngine;

    @PostMapping
    public SoThuLyKiemSoat create(@RequestBody SoThuLyDTO dto) {
        return service.create(dto);
    }

    @PutMapping
    public SoThuLyKiemSoat update(@RequestBody SoThuLyDTO dto) {
        return service.update(dto.getId(),dto);
    }

    @GetMapping("/{id}")
    public SoThuLyKiemSoat getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping(value = {"/export"})
    public void export(final SoThuLyDTO dto, HttpServletResponse response) throws Exception {
        String title = "so thu ly";
        String fileName = URLEncoder.encode(title + DateUtil.getCurrentTime() + ".xlsx", "UTF-8");
        response.setContentType("application/msexcel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        List<SoThuLyKiemSoatDTO> results = service.exportExcel(dto);
        Export.exportPoi2(results, SoThuLyKiemSoatDTO.class, response.getOutputStream(), null, title);
    }

    @PostMapping("/copy/{id}")
    public ResponseEntity<SoThuLyDTO> copy(@PathVariable Long id) throws InvocationTargetException, IllegalAccessException {
        SoThuLyKiemSoat existing = service.getById(id);
        SoThuLyDTO dto = service.copyFromId(existing);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("search")
    public Page<SoThuLyKiemSoat> searchJson(SoThuLyDTO dto, @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return service.queryPage(dto, page, size);
    }

    @GetMapping("/download-doc/{id}")
    public ResponseEntity<InputStreamResource> downloadDocx(@PathVariable Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("ID không được để trống");

        SoThuLyDTO lyKiemSoat = service.findById(id);

        // ----- Render HTML bằng Thymeleaf -----
        Context context = new Context();
        context.setVariable("ly", lyKiemSoat);

        // phieu_ks.html nằm trong /templates/temp_doc/
        String html = templateEngine.process("temp_doc/phieu_ks", context);

        // ---- Convert sang XHTML ----
        Document doc = Jsoup.parse(html);
        doc.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .escapeMode(Entities.EscapeMode.xhtml)
                .prettyPrint(false);

        String xhtml = doc.outerHtml();

        // ---- Tạo Word ----
        // ---- Tạo Word ----
        WordprocessingMLPackage wordMLPackage =
                WordprocessingMLPackage.createPackage(PageSizePaper.A4, false);

        ObjectFactory factory = new ObjectFactory();

// =====  SET MARGIN =====
        SectPr sectPr = wordMLPackage.getMainDocumentPart()
                .getJaxbElement()
                .getBody()
                .getSectPr();

        if (sectPr == null) {
            sectPr = factory.createSectPr();
            wordMLPackage.getMainDocumentPart()
                    .getJaxbElement()
                    .getBody()
                    .setSectPr(sectPr);
        }

        SectPr.PgMar pgMar = new SectPr.PgMar();
        pgMar.setTop(BigInteger.valueOf(1134));      // 2 cm
        pgMar.setBottom(BigInteger.valueOf(1134));   // 2 cm
        pgMar.setRight(BigInteger.valueOf(1134));    // 2 cm
        pgMar.setLeft(BigInteger.valueOf(1701));     // 3 cm

        sectPr.setPgMar(pgMar);
// ===== END SET MARGIN =====


// ---- Font ----
        RFonts rfonts = factory.createRFonts();
        rfonts.setAscii("Times New Roman");
        rfonts.setHAnsi("Times New Roman");
        rfonts.setCs("Times New Roman");

        Style normalStyle = wordMLPackage
                .getMainDocumentPart()
                .getStyleDefinitionsPart()
                .getStyleById("Normal");

        if (normalStyle != null) {
            if (normalStyle.getRPr() == null) normalStyle.setRPr(factory.createRPr());
            normalStyle.getRPr().setRFonts(rfonts);
        }

        // ---- Import HTML ----
        XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
        wordMLPackage.getMainDocumentPart().getContent().addAll(importer.convert(xhtml, null));

        String name = lyKiemSoat.getOrderNumber() + " - " + lyKiemSoat.getSttNgayTl() + DateUtil.dateToYMDHMSNo(new Date());
        File tempFile = File.createTempFile(name, ".docx");
        wordMLPackage.save(tempFile);

        byte[] bytes = Files.readAllBytes(tempFile.toPath());
        tempFile.delete();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + name + ".docx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(new InputStreamResource(new ByteArrayInputStream(bytes)));
    }

}

