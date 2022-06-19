package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService){
        this.userService = userService;
    }


    @GetMapping
    public String signupPage(Model model){
        String status = "";
        model.addAttribute("status", status);
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute("user") User userForm, Model model){
        String status = "ko";
        String msgError = "Error signing up!!!";
        if (userService.isUsernameAvailable(userForm.getUserName())) {
            int userCreated = userService.createUser(userForm);
            if(userCreated > 0){
                status = "ok";
                msgError = "";
            }
        }else{
            msgError = "Username Already Exist!";
        }
        model.addAttribute("status", status);
        model.addAttribute("msgError", msgError);
        return "signup";
    }
}
