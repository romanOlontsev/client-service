package ru.neoflex.clientservice.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@Table(name = "header_validation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HeaderValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "header_name")
    private String headerName;

    @ElementCollection
    @CollectionTable(name = "required_fields")
    private List<String> requiredFields = new ArrayList<>();

}
