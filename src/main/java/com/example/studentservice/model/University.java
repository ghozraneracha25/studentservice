package com.example.studentservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.util.List;

@Entity
@Data
@ToString(exclude = "students")            // évite toString() récursif
@EqualsAndHashCode(exclude = "students")  // évite equals/hashCode récursifs
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long university_id;

    private String nameU;
    private String location;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Student> students;
}
