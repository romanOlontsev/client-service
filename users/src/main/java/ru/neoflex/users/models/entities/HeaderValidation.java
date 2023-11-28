package ru.neoflex.users.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@Table(name = "header", schema = "accounts_app")
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
    @CollectionTable(name = "required_fields", schema = "accounts_app")
    private List<String> requiredFields = new ArrayList<>();

}
