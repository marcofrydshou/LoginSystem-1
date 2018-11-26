package demo.controller;

import demo.exception.BusinessException;
import demo.model.User;
import demo.model.dto.ApiResponseDTO;
import demo.model.dto.ResetPasswordForm;
import demo.model.dto.ResetPasswordSendForm;
import demo.service.PasswordResetService;
import demo.service.UserService;
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

        User user = userService.findByEmail(sendForm.getEmail());

        passwordResetService.sendPasswordResetEmail(user);

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

        User requestedUser = userService.findUserById(passwordForm.getId());
        if (!passwordForm.getPassword().equals(passwordForm.getConfirmPassword())) {
            throw new BusinessException("Passwords do not match.");
        }

        if (requestedUser.hasAuthority("CHANGE_PASSWORD_PRIVILEGE")) {
            passwordResetService.updatePassword(requestedUser, passwordForm.getPassword());
        }

        return new ResponseEntity<>(new ApiResponseDTO(HttpStatus.OK, "ok"), HttpStatus.OK);
    }

}
