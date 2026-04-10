package com.robotica.kohasimulator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Getter @Setter @NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id", length = 36)
    private String itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biblio_id", nullable = false)
    private Biblio biblio;

    @Column(name = "barcode", nullable = false, unique = true, length = 20)
    private String barcode;

    @Column(name = "location", length = 80)
    private String location;

    @Column(name = "callnumber", length = 255)
    private String callnumber;

    @Column(name = "itype", length = 10)
    private String itype = "BK";

    @Column(name = "branchcode", nullable = false, length = 10)
    private String branchcode;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @Column(name = "damaged", nullable = false)
    private Boolean damaged = false;

    @Column(name = "lost", nullable = false)
    private Boolean lost = false;

    @Column(name = "withdrawn", nullable = false)
    private Boolean withdrawn = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
