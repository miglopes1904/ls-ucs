package com.learningscorecard.ucs.service;

import com.learningscorecard.ucs.model.request.UpdateGradeRequest;
import com.learningscorecard.ucs.model.request.UpdateRankRequest;
import com.learningscorecard.ucs.model.request.UpdateXPRequest;

import java.util.UUID;

public interface ManagementService {

    String start(UUID id);
    String close(UUID id);
    String updateRanks(UpdateRankRequest request);
    String updateGrades(UpdateGradeRequest request);
    String updateXPs(UpdateXPRequest request);

}
