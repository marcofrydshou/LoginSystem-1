package demo.exposed.controller;

import demo.exposed.exception.BusinessException;
import demo.data.model.User;
import demo.data.model.dto.ApiResponseDTO;
import demo.data.model.dto.ResetPasswordForm;
import demo.data.model.dto.ResetPasswordSendForm;
import demo.business.PasswordResetService;
import demo.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;

@Slf4j
@RequestMapping(value = "/api/password")
@RestController
public class PasswordResetController {

    private UserService userService;
    private PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(UserService userService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping(value = "/reset")
    public ResponseEntity<ApiResponseDTO> requestPasswordReset(@RequestBody ResetPasswordSendForm sendForm) throws BusinessException {
        try{
            System.out.println("hej");
            User user = userService.findByEmail(sendForm.getEmail());
            passwordResetService.sendPasswordResetEmail(user);
        }
        catch (Exception e){
            throw new BusinessException("Email invalid");
        }

        return new ResponseEntity<>(new ApiResponseDTO(HttpStatus.OK, "ok"), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/reset/{user_id}/{token}")
    public ResponseEntity<ApiResponseDTO> receivePasswordReset(@PathVariable(name = "user_id") Integer userId, @PathVariable(name = "token") String token) throws BusinessException {

        if (userId <= 0) {
            log.info("receivePasswordReset called with invalid user ID ({})", userId);
            throw new BusinessException("receivePasswordReset called with invalid user ID");
        }

        if (StringUtils.isEmpty(token) || token.length() < 30) {
            log.info("receivePasswordReset called with invalid token");
            throw new BusinessException("receivePasswordReset called with invalid token");
        }

        log.info("receivePasswordReset called for user with ID ({})", userId);

        try {
            // validate and give authority
            passwordResetService.validateToken(userId, token);
        } catch (Exception e) {
            throw new BusinessException("PasswordResetToken invalid.");
        }

        return new ResponseEntity<>(new ApiResponseDTO(HttpStatus.OK, String.valueOf(userId)), HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value = "/update")
    public ResponseEntity<ApiResponseDTO> updatePassword(@RequestBody ResetPasswordForm passwordForm) throws BusinessException {

        log.info("updatePassword: (ID : '{}')", passwordForm.getId());

        // find the user by the given id
        User requestedUser = userService.findUserById(passwordForm.getId());

        // throw an exception if two passwords not match
        if (!passwordForm.getPassword().equals(passwordForm.getConfirmPassword())) {
            throw new BusinessException("Passwords do not match.");
        }

        // check if the found user has authority
        if (requestedUser.hasAuthority("CHANGE_PASSWORD_PRIVILEGE")) {

            // deleted the token related to the user and saved the new password
            passwordResetService.updatePassword(requestedUser, passwordForm.getPassword());
        }

        return new ResponseEntity<>(new ApiResponseDTO(HttpStatus.OK, "ok"), HttpStatus.OK);
    }

}
