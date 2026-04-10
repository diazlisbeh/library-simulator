package com.robotica.kohasimulator.service;

import com.robotica.kohasimulator.dto.PatronResponse;
import com.robotica.kohasimulator.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatronService {

    private final PatronRepository patronRepository;

    public PatronResponse getById(String patronId) {
        return patronRepository.findById(patronId)
            .map(PatronResponse::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Patron not found: " + patronId));
    }

    public Optional<PatronResponse> findByCardnumber(String cardnumber) {
        return patronRepository.findByCardnumber(cardnumber).map(PatronResponse::from);
    }
}
