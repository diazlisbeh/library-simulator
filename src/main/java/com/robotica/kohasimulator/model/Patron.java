package com.robotica.kohasimulator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patrons")
@Getter @Setter @NoArgsConstructor
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "patron_id", length = 36)
    private String patronId;

    @Column(name = "cardnumber", unique = true, length = 32)
    private String cardnumber;

    @Column(name = "userid", nullable = false, unique = true, length = 75)
    private String userid;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "firstname", length = 100)
    private String firstname;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 25)
    private String phone;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "categorycode", nullable = false, length = 10)
    private String categorycode = "PT";

    @Column(name = "branchcode", nullable = false, length = 10)
    private String branchcode;

    // Koha-compatible flags bitmask (1 = superlibrarian, etc.)
    @Column(name = "flags")
    private Long flags = 0L;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "date_enrolled")
    private LocalDate dateEnrolled;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
