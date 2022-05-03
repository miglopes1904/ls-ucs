package com.learningscorecard.ucs.controller;

import com.learningscorecard.ucs.model.request.UpdateGradeRequest;
import com.learningscorecard.ucs.model.request.UpdateRankRequest;
import com.learningscorecard.ucs.model.request.UpdateXPRequest;
import com.learningscorecard.ucs.model.response.LSResponse;
import com.learningscorecard.ucs.service.ManagementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class ManagementController {

    private final ManagementService service;

    public ManagementController(ManagementService service) {
        this.service = service;
    }

    @PutMapping("/start/{id}")
    public LSResponse<String> start(@PathVariable String id) {
        return new LSResponse<>(service.start(UUID.fromString(id)));
    }

    @PutMapping("/close/{id}")
    public LSResponse<String> close(@PathVariable String id) {
        return new LSResponse<>(service.close(UUID.fromString(id)));
    }

    @PutMapping("/ranks")
    public LSResponse<String> updateRanks(@RequestBody UpdateRankRequest request) {
        return new LSResponse<>(service.updateRanks(request));
    }

    @PutMapping("/grades")
    public LSResponse<String> updateGrades(@RequestBody UpdateGradeRequest request) {
        return new LSResponse<>(service.updateGrades(request));
    }

    @PutMapping("/xps")
    public LSResponse<String> updateXPs(@RequestBody UpdateXPRequest request) {
        return new LSResponse<>(service.updateXPs(request));
    }
}
