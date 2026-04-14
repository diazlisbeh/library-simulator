package com.robotica.kohasimulator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkouts")
@Getter @Setter @NoArgsConstructor
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "checkout_id", length = 36)
    private String checkoutId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "library_id", nullable = false, length = 10)
    private String libraryId;

    @Column(name = "checkout_date", nullable = false)
    private LocalDateTime checkoutDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "renewals_count")
    private Integer renewalsCount = 0;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "auto_renew")
    private Boolean autoRenew = false;
}
