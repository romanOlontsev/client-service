package ru.neoflex.products.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Audited
@DynamicInsert
@Table(name = "product", schema = "products_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "type", length = 4)
    private String type;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "tariff")
    private UUID tariff;

    @Column(name = "author")
    private UUID author;

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
