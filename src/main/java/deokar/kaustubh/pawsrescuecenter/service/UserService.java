package deokar.kaustubh.pawsrescuecenter.service;

import deokar.kaustubh.pawsrescuecenter.config.MessageString;
import deokar.kaustubh.pawsrescuecenter.dto.users.SignInDto;
import deokar.kaustubh.pawsrescuecenter.dto.users.SignInResponseDto;
import deokar.kaustubh.pawsrescuecenter.dto.users.SignUpResponseDto;
import deokar.kaustubh.pawsrescuecenter.dto.users.SignupDto;
import deokar.kaustubh.pawsrescuecenter.exceptions.AuthenticationFailException;
import deokar.kaustubh.pawsrescuecenter.exceptions.CustomException;
import deokar.kaustubh.pawsrescuecenter.model.AuthenticationToken;
import deokar.kaustubh.pawsrescuecenter.model.User;
import deokar.kaustubh.pawsrescuecenter.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;

    public SignUpResponseDto signUp(SignupDto signupDto) throws CustomException {
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            throw new CustomException("User account exists with this email id, sign in.!");
        }
        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.printf("hashing password failed {}%n", e.getMessage());
        }
        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encryptedPassword);
        try {
            userRepository.save(user);

            AuthenticationToken token = new AuthenticationToken(user);
            authenticationService.saveConfirmationToken(token);

            return new SignUpResponseDto("success", "user created successfully");
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }

    public SignInResponseDto signIn(SignInDto signInDto) throws AuthenticationFailException, CustomException {
        String userEmail = signInDto.getEmail();
        User user = userRepository.findByEmail(userEmail);
        if (!Objects.nonNull(user)) {
            throw new AuthenticationFailException("user not present");
        }

        authenticationPassword(signInDto, user);

        AuthenticationToken token = authenticationService.getToken(user);
        if (!Objects.nonNull(token)) {
            throw new CustomException(MessageString.AUTH_TOKEN_NOT_PRESENT);
        }

        return new SignInResponseDto("success", token.getToken());
    }

    private void authenticationPassword(SignInDto signInDto, User user) throws AuthenticationFailException, CustomException {
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException(MessageString.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.printf("hashing password failed {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }
    }
}
