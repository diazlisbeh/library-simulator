package com.robotica.kohasimulator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "branches")
@Getter @Setter @NoArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "branch_id", length = 36)
    private String branchId;

    @Column(name = "branchcode", nullable = false, unique = true, length = 10)
    private String branchcode;

    @Column(name = "branchname", nullable = false, length = 100)
    private String branchname;

    @Column(name = "branchaddress1", length = 100)
    private String branchaddress1;

    @Column(name = "branchphone", length = 20)
    private String branchphone;

    @Column(name = "branchemail", length = 100)
    private String branchemail;
}
