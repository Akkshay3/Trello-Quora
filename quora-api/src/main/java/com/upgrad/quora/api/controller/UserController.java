package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserAuthService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/user/signup")
    public ResponseEntity<SignupUserResponse> signup(SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        User user = new User();
        user.setFirstName(signupUserRequest.getFirstName());
        user.setLastName(signupUserRequest.getLastName());
        user.setUserName(signupUserRequest.getUserName());
        user.setEmail(signupUserRequest.getEmailAddress());
        user.setPassword(signupUserRequest.getPassword());
        user.setCountry(signupUserRequest.getCountry());
        user.setAboutMe(signupUserRequest.getAboutMe());
        user.setDob(signupUserRequest.getDob());
        user.setRole("nonadmin");
        user.setContactNumber(signupUserRequest.getContactNumber());

        String uuid = userAuthService.signup(user);
        SignupUserResponse userResponse =
                new SignupUserResponse();
        userResponse.setId(uuid);
        userResponse.setStatus("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);

    }
}
