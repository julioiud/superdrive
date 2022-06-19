package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;
    private Long userId;

    public FileService(FileMapper fileMapper,
                       UserMapper userMapper,
                       AuthenticationService authenticationService){
       this.fileMapper = fileMapper;
       this.userMapper = userMapper;
       this.authenticationService = authenticationService;
    }

    public List<File> getFiles(){
        if(authenticationService!=null){
            userId = authenticationService.getIdUser();
            return fileMapper.getFilesByuserId(userId);
        }
        return new ArrayList<>();
    }

    public File createFile(MultipartFile uploadFile) throws IOException {
        if(authenticationService == null){
            return null;
        }
        userId = authenticationService.getIdUser();
        if(fileMapper.getFileByName(uploadFile.getOriginalFilename(), userId) > 0){
            return null;
        }

        File file = new File();
        file.setFileName(uploadFile.getOriginalFilename());
        file.setContentType(uploadFile.getContentType());
        file.setFileSize(String.valueOf(uploadFile.getSize()));
        file.setUserId(userId);
        file.setFileData(uploadFile.getBytes());
        int fileDB = fileMapper.create(file);
        if(fileDB > 0){
            return file;
        }
        return null;

    }

    public int deleteById(Long fileId){
        if(fileMapper.getFile(fileId) == null){
            return 0;
        }
       return fileMapper.delete(fileId);
    }

    public File findById(Long fileId){
        return fileMapper.getFile(fileId);
    }
}
