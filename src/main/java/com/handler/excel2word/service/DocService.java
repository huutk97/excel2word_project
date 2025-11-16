package com.handler.excel2word.service;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DocService {

//    public Path createDocFromRow(String stt, String ngayTl, Path outputDir) throws Exception {
//        if (!Files.exists(outputDir)) Files.createDirectories(outputDir);
//        String fileName = String.format("STT_%s_NgayTL_%s.doc", sanitize(stt), sanitize(ngayTl));
//        Path out = outputDir.resolve(fileName);
//
//        // Create simple .doc using HWPFDocument
//        try (POIFSFileSystem fs = new POIFSFileSystem();
//             HWPFDocument doc = new HWPFDocument(fs)) {
//            doc.getRange().insertAfter("STT: " + stt + "\n" + "Ngày TL: " + ngayTl + "\n");
//            try (FileOutputStream fos = new FileOutputStream(out.toFile())) {
//                doc.write(fos);
//            }
//        }
//        return out;
//    }

    private String sanitize(String s) {
        return s.replaceAll("[\\/:*?<>|]", "_").replaceAll("\\s", "_");
    }
}
