package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class LoginController {

    @GetMapping("/")
    public String Home(Model model){
        String status = "";
        model.addAttribute("status", status);
        return "login";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        String status = "";
        model.addAttribute("status", status);
        return "login";
    }
}
