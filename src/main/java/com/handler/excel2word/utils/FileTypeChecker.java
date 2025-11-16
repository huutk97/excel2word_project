package com.handler.excel2word.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileTypeChecker {
    public static boolean isCsvFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && fileName.toLowerCase().endsWith(".csv");
    }

    public static boolean isExcelFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && fileName.toLowerCase().endsWith(".xlsx");
    }
}
