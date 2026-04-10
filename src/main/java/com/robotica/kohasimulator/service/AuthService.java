package com.robotica.kohasimulator.service;

import com.robotica.kohasimulator.dto.SessionRequest;
import com.robotica.kohasimulator.dto.SessionResponse;
import com.robotica.kohasimulator.repository.PatronRepository;
import com.robotica.kohasimulator.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PatronRepository patronRepository;
    private final JwtUtil jwtUtil;

    public SessionResponse login(SessionRequest req) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.userid(), req.password())
        );

        var patron = patronRepository.findByUserid(req.userid())
            .orElseThrow();

        String token = jwtUtil.generateToken(patron.getUserid(), patron.getPatronId());

        return new SessionResponse(
            patron.getPatronId(),
            patron.getCardnumber(),
            patron.getSurname(),
            patron.getFirstname(),
            patron.getEmail(),
            patron.getCategorycode(),
            patron.getBranchcode(),
            patron.getFlags(),
            token
        );
    }
}
