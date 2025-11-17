package com.handler.excel2word.handlerApi.controller;

import com.handler.excel2word.entity.SoThuLyKiemSoat;
import com.handler.excel2word.handlerApi.Interface.SoThuLyService;
import com.handler.excel2word.handlerApi.dto.SoThuLyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class SoThuLyViewController {

    private final SoThuLyService service;

    @GetMapping
    public String viewList(Model model, @RequestParam(defaultValue = "0") int page) {
        SoThuLyDTO soThuLyDTO = new SoThuLyDTO();
        Page<SoThuLyKiemSoat> list = service.queryPage(soThuLyDTO, 0, 10);
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", list.getTotalPages());
        return "so-thu-ly";  // templates/list.html
    }
}
