package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final AuthenticationService authenticationService;
    private final EncryptionService encryptionService;
    private Long userId;

    public CredentialService(CredentialMapper credentialMapper,
                       AuthenticationService authenticationService,
                             EncryptionService encryptionService){
        this.credentialMapper = credentialMapper;
        this.authenticationService = authenticationService;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(){
        if(authenticationService!=null){
            userId = authenticationService.getIdUser();
            List<Credential> credentials = credentialMapper.getCredentialsByUserId(userId);
            credentials = credentials.stream().map(c -> {
                String passwordDec = encryptionService.decryptValue(c.getPassword(), c.getKey());
                c.setPasswordDec(passwordDec);
                return c;
            }).collect(Collectors.toList());
            return credentials;
        }
        return new ArrayList<>();
    }

    public Credential createCredential(Credential credential) {
        if(authenticationService == null){
            return null;
        }
        userId = authenticationService.getIdUser();
        credential.setUserId(userId);

        if(credentialMapper.countCredentials(credential) > 0){
            return null;
        }

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        credential.setKey(encodedKey);

        String encryptedPassword = encryptionService
                .encryptValue(credential.getPasswordDec(), encodedKey);

        credential.setPassword(encryptedPassword);

        int noteDB = credentialMapper.create(credential);
        if(noteDB <= 0){
            return null;
        }
        credential.setCredentialId(noteDB);
        return credential;
    }

    public Credential updateCredential(Credential credential) {
        if(credentialMapper.getCredential(credential.getCredentialId()) == null){
            return null;
        }
        if(authenticationService == null){
            return null;
        }
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        credential.setKey(encodedKey);

        String encryptedPassword = encryptionService
                .encryptValue(credential.getPasswordDec(), encodedKey);

        credential.setPassword(encryptedPassword);

        userId = authenticationService.getIdUser();
        credential.setUserId(userId);
        int credDB = credentialMapper.update(credential);
        if(credDB <= 0){
            return null;
        }
        return credential;
    }

    public int deleteById(Integer credentialId){
        if(credentialMapper.getCredential(credentialId) == null){
            return 0;
        }
        return credentialMapper.delete(credentialId);
    }
}
