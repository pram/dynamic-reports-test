package com.pram.controller;

import com.pram.SimpleReportStep01;
import com.pram.model.User;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    public static final String PAGE_INDEX = "index";
    // inject via application.properties
    @Value("${welcome.message}")
    private String message;

    private List<User> users = IndexController.generateUsers();

    private static List<User> generateUsers() {
        List<User> retVal = new ArrayList<>();

        retVal.add(new User(1,"Mick", "Jones", "mick@jones.com",5));
        retVal.add(new User(2,"Jim", "Smith", "jim@smith.com",7));
        retVal.add(new User(3,"Pete", "Ward", "pete@ward.com",8));

        return retVal;
    }

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("users", users);

        return PAGE_INDEX; //view
    }

    // /hello?name=kotlin
    @GetMapping("/hello")
    public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {

        model.addAttribute("message", name);

        return PAGE_INDEX; //view
    }

    @RequestMapping(value="/generate-report")
    public String generateReport(Model model) {
        System.out.println("Success");
        model.addAttribute("message", "Button Pusher");
        model.addAttribute("users", users);
        return PAGE_INDEX;
    }

    @RequestMapping(value = "/generate-pdf-report", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdfReport(Model model) throws DRException {

        model.addAttribute("message", "PDF Report Generator");
        model.addAttribute("users", users);

        SimpleReportStep01 simpleReportStep01 = new SimpleReportStep01();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        simpleReportStep01.generateReport().toPdf(out);
        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=users-report.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}