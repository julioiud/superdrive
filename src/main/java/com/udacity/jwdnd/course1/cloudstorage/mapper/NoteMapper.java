package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getNotesByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM NOTES WHERE notetitle = #{noteTitle} AND userid = #{userId}")
    int getNotesByTitle(String noteTitle, Long userId);

    @Insert("INSERT INTO NOTES (notetitle,notedescription,userid) " +
            "VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int create(Note note);

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    File getNote(Integer noteId);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    int delete(Integer noteId);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription}" +
            " WHERE noteid = #{noteId}")
    int update(Note note);
}
