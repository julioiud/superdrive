package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    private List<File> files = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();
    private List<Credential> credentials = new ArrayList<>();

    public HomeController(FileService fileService,
                          NoteService noteService,
                          CredentialService credentialService,
                          EncryptionService encryptionService){
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String getHome(Model model){
        files = fileService.getFiles();
        notes = noteService.getNotes();
        credentials = credentialService.getCredentials();
        model.addAttribute("credentials", credentials);
        model.addAttribute("files", files);
        model.addAttribute("notes", notes);
        model.addAttribute("note", new Note());
        model.addAttribute("credential", new Credential());
        return "home";
    }
}
