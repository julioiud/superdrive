package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final HashService hashService;
    private final UserMapper userMapper;

    public UserService(HashService hashService,
                       UserMapper userMapper){
        this.hashService = hashService;
        this.userMapper = userMapper;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.create(new User(null, user.getUserName(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }
}
