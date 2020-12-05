package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    public String signup(User user) throws SignUpRestrictedException {
        if (isUserNameInUse(user.getUserName())) {
            throw new SignUpRestrictedException(
                    "SGR-001", "Try any other Username, this Username has already been taken");
        }

        if (isEmailInUse(user.getEmail())) {
            throw new SignUpRestrictedException(
                    "SGR-002", "This user has already been registered, try with any other emailId");
        }
        // Assign a UUID to the user that is being created.
        user.setUuid(UUID.randomUUID().toString());
        // Assign encrypted password and salt to the user that is being created.
        String[] encryptedText = passwordCryptographyProvider.encrypt(user.getPassword());
        user.setSalt(encryptedText[0]);
        user.setPassword(encryptedText[1]);
        User createdUser= userRepository.save(user);
        return createdUser.getUuid();
    }

    // checks whether the username exist in the database
    private boolean isUserNameInUse(final String userName) {
        return null!= userRepository.findByUserName(userName);
        //return userDao.getUserByUserName(userName) != null;
    }

    // checks whether the email exist in the database
    private boolean isEmailInUse(final String email) {
        return null!= userRepository.findByEmail(email);
        // return userDao.getUserByEmail(email) != null;
    }


}
