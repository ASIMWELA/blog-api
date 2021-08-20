package com.personal.website.controller;

import com.personal.website.entity.PasswordResetToken;
import com.personal.website.entity.UserEntity;
import com.personal.website.exception.EntityNotFoundException;
import com.personal.website.exception.TokenExpiredException;
import com.personal.website.dto.UserDto;
import com.personal.website.payload.ApiResponse;
import com.personal.website.payload.LoginRequest;
import com.personal.website.payload.LoginResponse;
import com.personal.website.repository.PasswordResetTokenRepository;
import com.personal.website.repository.RoleRepository;
import com.personal.website.repository.UserRepository;
import com.personal.website.security.JwtTokenProvider;
import com.personal.website.service.UserService;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    PasswordResetTokenRepository passwordResetTokenRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider jwtUtils;
    JavaMailSender javaMailSender;
    UserService userService;


    @Value("${app.emailOrigin}")
    String emailSender;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtUtils, JavaMailSender javaMailSender, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserEntity entity = userRepository.findByUserName(jwtUtils.getUserNameFromJwtToken(jwt)).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        UserDto userDtoModel = UserDto.build(entity);
        userDtoModel.add(linkTo(
                        methodOn(UserController.class).getUser(entity.getUuid())).withSelfRel());
        return ResponseEntity.ok(new LoginResponse(jwt, userDtoModel));
    }

    @SneakyThrows
    @RequestMapping(
            value = "/forgot-password",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam("email") String email, @RequestParam("url") String clientLocation) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("No user found with email " + email)
        );

        PasswordResetToken token = new PasswordResetToken(UUID.randomUUID().toString());
        token.setUser(user);
        //token expire after 60 minutes
        token.setExpiryDate(60);
        passwordResetTokenRepository.save(token);


        //get application url
        String url = clientLocation + "/reset-password?token=" + token.getToken();

        //send email after 5 seconds to give time for smp server initialisation
        Thread.sleep(5000);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(emailSender);
        mail.setSubject("Password Reset");
        mail.setText("Click on the link below to complete the reset process\n\n" + url);
        javaMailSender.send(mail);
        return new ResponseEntity<>(new ApiResponse(true, "Reset Link sent to your email. Complete process within 1 hour"), HttpStatus.OK);

    }

    @RequestMapping(
            value = "/reset-password",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam("token") String requestToken, @RequestParam("password") String password) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(requestToken).orElseThrow(
                () -> new EntityNotFoundException("Token: " + requestToken + " not found")
        );

        if (token.isExpired())
            throw new TokenExpiredException("Link broken. Token expired");

        UserEntity user = token.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        token.setUser(null);
        passwordResetTokenRepository.delete(token);

        return new ResponseEntity<>(new ApiResponse(true, "Password Reset Successful"), HttpStatus.OK);

    }
}