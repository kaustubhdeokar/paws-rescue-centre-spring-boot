package deokar.kaustubh.pawsrescuecenter.controller;

import deokar.kaustubh.pawsrescuecenter.dto.users.SignUpResponseDto;
import deokar.kaustubh.pawsrescuecenter.dto.users.SignupDto;
import deokar.kaustubh.pawsrescuecenter.exceptions.CustomException;
import deokar.kaustubh.pawsrescuecenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> Signup(@RequestBody SignupDto signupDto) throws CustomException{
        return new ResponseEntity<>(userService.signUp(signupDto), HttpStatus.OK);
    }

}
