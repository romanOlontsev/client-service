package ru.neoflex.tariffs.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Audited
@DynamicInsert
@Table(name = "tariff", schema = "tariffs_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "version")
    private Long version;

    @PrePersist
    @PreUpdate
    public void onModifying() {
        upVersion();
    }

    private void upVersion() {
        if (version == null) {
            version = 1L;
        } else {
            ++version;
        }
    }
}
