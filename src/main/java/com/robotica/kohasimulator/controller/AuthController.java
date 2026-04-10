package com.robotica.kohasimulator.controller;

import com.robotica.kohasimulator.dto.ApiError;
import com.robotica.kohasimulator.dto.SessionRequest;
import com.robotica.kohasimulator.dto.SessionResponse;
import com.robotica.kohasimulator.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Koha-compatible session endpoints")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Create session (login)", description = "Authenticates a patron and returns a JWT token. Also sets CGISESSID cookie for Koha compatibility.")
    @PostMapping("/session")
    public ResponseEntity<?> login(@Valid @RequestBody SessionRequest req,
                                   HttpServletResponse response) {
        try {
            SessionResponse session = authService.login(req);

            // Set CGISESSID cookie for Koha client compatibility
            Cookie cookie = new Cookie("CGISESSID", session.token());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(8 * 60 * 60); // 8 hours
            response.addCookie(cookie);

            return ResponseEntity.ok(session);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.of("UNAUTHORIZED", "Invalid userid or password"));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiError.of("FORBIDDEN", "Patron account is disabled"));
        }
    }

    @Operation(summary = "Delete session (logout)")
    @DeleteMapping("/session")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("CGISESSID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }
}
