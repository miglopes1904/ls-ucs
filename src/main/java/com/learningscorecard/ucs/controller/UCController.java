package com.learningscorecard.ucs.controller;

import com.learningscorecard.ucs.model.dto.JournalEntry;
import com.learningscorecard.ucs.model.dto.LeaderboardEntry;
import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.dto.teacher.UCDTO4Teacher;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
import com.learningscorecard.ucs.model.response.LSResponse;
import com.learningscorecard.ucs.service.UCService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
public class UCController {

    private final UCService service;

    public UCController(UCService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LSResponse<List<UCDTO4Teacher>> getAll() {
        return new LSResponse<>(service.getAll());
    }

    @GetMapping("/{id}")
    public LSResponse<UCDTO> getById(@PathVariable UUID id, Authentication authentication) {
        return new LSResponse<>(service.getByID(id, authentication));
    }

    @GetMapping("/leaderboard/{id}/{type}")
    public LSResponse<List<LeaderboardEntry>> getLeaderboard(@PathVariable UUID id, @PathVariable String type) {
        return new LSResponse<>(service.getLeaderboard(id, type.toLowerCase()));
    }

    @GetMapping("/journal/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public LSResponse<List<JournalEntry>> getJournal(@PathVariable UUID id) {
        return new LSResponse<>(service.getJournal(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public LSResponse<String> create(@RequestBody CreateUCRequest request, Authentication authentication) {
        return new LSResponse<>(service.create(request, authentication));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public LSResponse<String> delete(@PathVariable UUID id) {
        return new LSResponse<>(service.delete(id));
    }
}
