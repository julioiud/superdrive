package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    @PostConstruct
    public void postConstruct() {
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload")MultipartFile fileUpload, Model model){
        try {
            File file =  fileService.createFile(fileUpload);
            if(file == null){
                model.addAttribute("error", true);
                model.addAttribute("redir", "/home");
                model.addAttribute("error2", true);
                model.addAttribute("msg", "Already exists file name!");
                return "result";
            }
            return "redirect:/home";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Long fileId, Model model){
        int result = fileService.deleteById(fileId);
        if(result <= 0){
            return "error";
        }
        return "redirect:/home";
    }

    @GetMapping("/view/{fileId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable("fileId") Long fileId) {
        File fileDB = fileService.findById(fileId);
        Resource file = new ByteArrayResource(fileDB.getFileData());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileDB.getFileName() + "\"").body(file);
    }

}
