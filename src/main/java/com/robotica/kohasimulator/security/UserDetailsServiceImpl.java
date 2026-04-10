package com.robotica.kohasimulator.security;

import com.robotica.kohasimulator.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PatronRepository patronRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        var patron = patronRepository.findByUserid(userid)
            .orElseThrow(() -> new UsernameNotFoundException("Patron not found: " + userid));

        var role = patron.getFlags() != null && patron.getFlags() == 1L
            ? "ROLE_ADMIN" : "ROLE_PATRON";

        return User.builder()
            .username(patron.getUserid())
            .password(patron.getPasswordHash())
            .authorities(List.of(new SimpleGrantedAuthority(role)))
            .accountExpired(patron.getExpiryDate() != null
                && patron.getExpiryDate().isBefore(java.time.LocalDate.now()))
            .disabled(!patron.getActive())
            .build();
    }
}
