package com.udacity.jwdnd.course1.cloudstorage.mapper;


import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getFilesByuserId(Long userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFile(Long fileId);

    @Select("SELECT COUNT(*) FROM FILES WHERE fileName = #{fileName} AND userid = #{userId}")
    int getFileByName(String fileName, Long userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize,userid, filedata) " +
            "VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int create(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int delete(Long fileId);
}
