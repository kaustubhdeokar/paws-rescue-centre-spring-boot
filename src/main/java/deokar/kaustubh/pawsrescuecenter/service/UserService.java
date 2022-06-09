package deokar.kaustubh.pawsrescuecenter.service;

import deokar.kaustubh.pawsrescuecenter.dto.users.SignUpResponseDto;
import deokar.kaustubh.pawsrescuecenter.dto.users.SignupDto;
import deokar.kaustubh.pawsrescuecenter.exceptions.CustomException;
import deokar.kaustubh.pawsrescuecenter.model.User;
import deokar.kaustubh.pawsrescuecenter.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public SignUpResponseDto signUp(SignupDto signupDto) throws CustomException {
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
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
}
