package com.robotica.kohasimulator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "biblios")
@Getter @Setter @NoArgsConstructor
public class Biblio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "biblio_id", length = 36)
    private String biblioId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "author", length = 255)
    private String author;

    @Column(name = "isbn", length = 30)
    private String isbn;

    @Column(name = "publisher", length = 255)
    private String publisher;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "language", length = 10)
    private String language = "es";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
