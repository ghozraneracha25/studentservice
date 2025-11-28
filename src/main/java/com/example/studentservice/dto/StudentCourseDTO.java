package com.example.studentservice.dto;

import lombok.Data;

@Data
public class StudentCourseDTO {
    private Long id_studentcours;
    private Long student_id;
    private Long course; // id du cours
}
