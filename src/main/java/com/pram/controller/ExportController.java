package com.pram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExportController {

    @GetMapping("/download")
    public String download(Model model) {
        model.addAttribute("report", null);
        return "";
    }


}