package com.pram.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    public static final String PAGE_INDEX = "index";
    // inject via application.properties
    @Value("${welcome.message}")
    private String message;

    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);

        return PAGE_INDEX; //view
    }

    // /hello?name=kotlin
    @GetMapping("/hello")
    public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {

        model.addAttribute("message", name);

        return PAGE_INDEX; //view
    }

    @RequestMapping(value="/do-stuff")
    public String doStuffMethod(Model model) {
        System.out.println("Success");
        model.addAttribute("message", "Button Pusher");
        return PAGE_INDEX;
    }

}