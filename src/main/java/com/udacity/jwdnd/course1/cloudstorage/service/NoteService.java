package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final AuthenticationService authenticationService;
    private Long userId;

    public NoteService(NoteMapper noteMapper,
                       AuthenticationService authenticationService){
        this.noteMapper = noteMapper;
        this.authenticationService = authenticationService;
    }

    public List<Note> getNotes(){
        if(authenticationService!=null){
            userId = authenticationService.getIdUser();
            List<Note> notes = noteMapper.getNotesByUserId(userId);
            return noteMapper.getNotesByUserId(userId);
        }
        return new ArrayList<>();
    }

    public Note createNote(Note note) {
        if(authenticationService == null){
            return null;
        }
        userId = authenticationService.getIdUser();
        note.setUserId(userId);
        if(noteMapper.getNotesByTitle(note.getNoteTitle(), userId) > 0){
            return null;
        }

        int noteDB = noteMapper.create(note);
        if(noteDB <= 0){
            return null;
        }
        note.setNoteId(noteDB);
        return note;
    }

    public int deleteById(Integer noteId){
        if(noteMapper.getNote(noteId) == null){
            return 0;
        }
        return noteMapper.delete(noteId);
    }

    public Note updateNote(Note note) {
        if(noteMapper.getNote(note.getNoteId()) == null){
            return null;
        }
        if(authenticationService == null){
            return null;
        }
        userId = authenticationService.getIdUser();
        note.setUserId(userId);
        int noteDB = noteMapper.update(note);
        if(noteDB <= 0){
            return null;
        }
        return note;
    }
}
