package com.learningscorecard.ucs.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counts {

    private Integer classAttendances = 0;
    private Integer practicalAssignments = 0;
    private Integer quizzes = 0;
    private Integer exercises = 0;
    private Integer contents = 0;

    public void addClass() {
        this.classAttendances +=1;
    }

    public void addPractical() {
        this.practicalAssignments +=1;
    }

    public void addQuiz() {
        this.quizzes +=1;
    }

    public void addExercise() {
        this.exercises +=1;
    }

    public void addContent() {
        this.contents +=1;
    }
}
