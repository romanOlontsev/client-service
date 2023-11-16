package ru.neoflex.tariffs.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.time.LocalDateTime;

@Entity
@RevisionEntity
@Table(name = "revinfo", schema = "tariffs_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "REV")
    @RevisionNumber
    private Integer rev;

    @Column(name = "revtstmp")
    @RevisionTimestamp
    private LocalDateTime timestamp;
}