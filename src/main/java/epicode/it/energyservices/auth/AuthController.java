package epicode.it.energyservices.auth;

import epicode.it.energyservices.auth.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AppUserSvc appUserSvc;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest) {
        String message = appUserSvc.registerUser(registerRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(appUserSvc.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/requestChangePassword")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody EmailForPasswordResetRequest passwordResetRequest) {
        String message = appUserSvc.sendEmailForChangePassword(passwordResetRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPasswordRedirect(@RequestParam String token, HttpServletResponse response) {

        return new ResponseEntity<>(appUserSvc.verifyTokenPasswordReset(token, response), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PatchMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        String message = appUserSvc.resetPassword(passwordResetRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/withAppUser")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserResponse> getByCustomerWithAppUser(@AuthenticationPrincipal User userDetails) {

        AppUser appUser = appUserSvc.getByUsername(userDetails.getUsername());
        AppUserResponse response = new AppUserResponse();
        response.setId(appUser.getId());
        response.setName(appUser.getName());
        response.setSurname(appUser.getSurname());
        response.setEmail(appUser.getEmail());
        response.setAvatar(appUser.getAvatar());
        response.setUsername(appUser.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/withAppUser")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserResponse> updateByCustomerWithAppUser(@RequestBody AppUserResponse appUserUpdateRequest, @AuthenticationPrincipal User userDetails) {

        AppUserResponse response = appUserSvc.updateUser(appUserUpdateRequest, userDetails);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal User userDetails) {
        String message = appUserSvc.changePassword(changePasswordRequest, userDetails);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
