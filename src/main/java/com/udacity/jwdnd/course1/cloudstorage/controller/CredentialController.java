package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService){
        this.credentialService = credentialService;
    }

    @PostMapping
    public String saveCredential(@ModelAttribute("credential") Credential credential, Model model){
        Credential result;
        if(credential.getCredentialId() == null){
            result = credentialService.createCredential(credential);
        }else{
            result = credentialService.updateCredential(credential);
        }
        if(result == null){
            model.addAttribute("error", true);
            model.addAttribute("redir", "/home");
            model.addAttribute("error2", true);
            model.addAttribute("msg", "Already exists url & username!");
            return "result";
        }
        return "redirect:/credentials/ok";
    }


    @GetMapping("/delete/{credentialId}")
    public String deleteNote(@PathVariable("credentialId") Integer credentialId, Model model){
        int result = credentialService.deleteById(credentialId);
        if(result > 0){
            return "redirect:/notes/ok";
        }
        return "error";
    }

    @GetMapping("/ok")
    public String getOKnote(Model model){
        model.addAttribute("error", false);
        model.addAttribute("redir", "/home");
        return "result";
    }
}
