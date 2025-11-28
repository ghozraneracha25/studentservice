package com.example.studentservice.controller;

import com.example.studentservice.model.University;
import com.example.studentservice.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {

    @Autowired
    private UniversityRepository universityRepository;

    // ➕ Ajouter une université
    @PostMapping
    public University addUniversity(@RequestBody University university) {
        return universityRepository.save(university);
    }

    // 🔍 Récupérer toutes les universités
    @GetMapping
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    // 🔍 Récupérer une université par ID
    @GetMapping("/{id}")
    public University getUniversityById(@PathVariable Long id) {
        return universityRepository.findById(id).orElse(null);
    }
}
