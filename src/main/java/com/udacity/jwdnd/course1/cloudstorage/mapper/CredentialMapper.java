package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {


    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    List<Credential> getCredentialsByUserId(Long userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credential getCredential(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url,username,key,password,userid) " +
            "VALUES (#{url}, #{userName}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int create(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{userName}, password = #{password}" +
            ", key = #{key} WHERE credentialid = #{credentialId}")
    int update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    int delete(Integer credentialId);

    @Select("SELECT COUNT(*) FROM CREDENTIALS WHERE url = #{url} AND username = #{userName} AND userid = #{userId}")
    int countCredentials(Credential credential);
}
