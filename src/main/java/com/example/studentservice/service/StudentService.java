package com.example.studentservice.service;

import com.example.studentservice.model.Student;
import com.example.studentservice.model.University;
import com.example.studentservice.repository.StudentRepository;
import com.example.studentservice.repository.UniversityRepository;
import com.example.studentservice.dto.StudentCourseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UniversityRepository universityRepository;
    private final RestTemplate restTemplate;

    // Constructeur avec RestTemplate
    public StudentService(StudentRepository studentRepository, UniversityRepository universityRepository,
            RestTemplate restTemplate) {
        this.studentRepository = studentRepository;
        this.universityRepository = universityRepository;
        this.restTemplate = restTemplate;
    }

    // Lister tous les étudiants
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Ajouter un étudiant
    public Student addStudent(Student student) {
        if (student.getUniversity() != null && student.getUniversity().getUniversity_id() != null) {
            Long uniId = student.getUniversity().getUniversity_id();
            University uni = universityRepository.findById(uniId).orElse(null);
            student.setUniversity(uni); // si uni == null -> laisse null (ou gérer erreur)
        }
        return studentRepository.save(student);
    }

    // Trouver par ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Supprimer un étudiant
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Modifier un étudiant
    public Student updateStudent(Long id, Student updatedStudent) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setFirstName(updatedStudent.getFirstName());
            student.setLastName(updatedStudent.getLastName());
            student.setEmail(updatedStudent.getEmail());

            if (updatedStudent.getUniversity() != null && updatedStudent.getUniversity().getUniversity_id() != null) {
                Long uniId = updatedStudent.getUniversity().getUniversity_id();
                University uni = universityRepository.findById(uniId).orElse(null);
                student.setUniversity(uni);
            }
            return studentRepository.save(student);
        }
        return null; // si l'étudiant avec l'id donné n'existe pas
    }

    // Rechercher par prénom
    public List<Student> searchByFirstName(String name) {
        return studentRepository.findByFirstNameContainingIgnoreCase(name);
    }

    // Filtrer par université
    public List<Student> findByUniversity(String universityName) {
        return studentRepository.findByUniversity_NameU(universityName);
    }

    // Filtrer les étudiants par cours inscrits (nouvelle fonctionnalité)
    public List<Student> findStudentsByCourse(Long courseId) {
        // URL du service Django
        String url = "http://localhost:8000/api/studentcourses/?course=" + courseId;

        // Appel REST pour récupérer les relations StudentCourse
        StudentCourseDTO[] response = restTemplate.getForObject(url, StudentCourseDTO[].class);

        if (response == null || response.length == 0) {
            return List.of(); // pas d'étudiants trouvés
        }

        // Extraire les IDs des étudiants
        List<Long> studentIds = Arrays.stream(response)
                .map(StudentCourseDTO::getStudent_id)
                .toList();

        // Retourner la liste des étudiants correspondants
        return studentRepository.findAllById(studentIds);
    }
}
