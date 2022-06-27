package com.learningscorecard.ucs.service;

import com.learningscorecard.ucs.model.dto.JournalEntry;
import com.learningscorecard.ucs.model.dto.LeaderboardEntry;
import com.learningscorecard.ucs.model.dto.UCDTO;
import com.learningscorecard.ucs.model.dto.teacher.UCDTO4Teacher;
import com.learningscorecard.ucs.model.request.CreateUCRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface UCService {

    List<UCDTO4Teacher> getAll();
    UCDTO getByID(UUID id, Authentication authentication);
    String create(CreateUCRequest request, Authentication authentication);
    String delete(UUID id);
    List<JournalEntry> getJournal(UUID id);
    List<LeaderboardEntry> getLeaderboard(UUID id, String type);
}
