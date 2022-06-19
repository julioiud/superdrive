package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    @PostConstruct
    public void postConstruct() {

    }

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    @GetMapping
    public String getNotes(Model model){
        return "/home/nav-notes";
    }

    @PostMapping
    public String saveNote(@ModelAttribute("note") Note note, Model model){
        Note result;
        if(note.getNoteId()==null){
            result = noteService.createNote(note);
        }else{
            result = noteService.updateNote(note);
        }
        if(result == null){
            model.addAttribute("error", true);
            model.addAttribute("redir", "/home");
            model.addAttribute("error2", true);
            model.addAttribute("msg", "Already exists title!");
            return "result";
        }
        return "redirect:/notes/ok";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Model model){
        int result = noteService.deleteById(noteId);
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
