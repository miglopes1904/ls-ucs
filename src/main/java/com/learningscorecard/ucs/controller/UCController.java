package com.learningscorecard.ucs.controller;

import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
import com.learningscorecard.ucs.model.response.LSResponse;
import com.learningscorecard.ucs.service.UCService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class UCController {

    private final UCService service;

    public UCController(UCService service) {
        this.service = service;
    }

    @GetMapping
    public LSResponse<List<UCDTO>> getAll() {
        return new LSResponse<>(service.getAll());
    }

    @GetMapping("/{id}")
    public LSResponse<UCDTO> getById(@PathVariable String id) {
        return new LSResponse<>(service.getByID(UUID.fromString(id)));
    }

    @PostMapping
    public LSResponse<String> create(@RequestBody CreateUCRequest request, Authentication authentication) {
        return new LSResponse<>(service.create(request, authentication));
    }

    @DeleteMapping("/{id}")
    public LSResponse<String> delete(@PathVariable String id) {
        return new LSResponse<>(service.delete(UUID.fromString(id)));
    }
}
