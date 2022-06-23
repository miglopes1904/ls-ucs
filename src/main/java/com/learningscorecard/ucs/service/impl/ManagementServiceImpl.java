package com.learningscorecard.ucs.service.impl;

import com.learningscorecard.ucs.exception.LSException;
import com.learningscorecard.ucs.model.entity.UC;
import com.learningscorecard.ucs.model.request.UpdateGradeRequest;
import com.learningscorecard.ucs.model.request.UpdateRankRequest;
import com.learningscorecard.ucs.model.request.UpdateXPRequest;
import com.learningscorecard.ucs.repository.UCRepository;
import com.learningscorecard.ucs.service.ManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class ManagementServiceImpl implements ManagementService {

    private final UCRepository repository;

    public ManagementServiceImpl(UCRepository repository) {
        this.repository = repository;
    }

    @Override
    public String start(UUID id) {
        UC uc = getOrElseThrow(id);

        if (uc.getStarted())
            throw new LSException("UC is already started", HttpStatus.BAD_REQUEST);

        uc.setStarted(Boolean.TRUE);

        repository.save(uc);

        return "UC was successfully started";
    }

    @Override
    public String close(UUID id) {
        UC uc = getOrElseThrow(id);

        if (!uc.getActive())
            throw new LSException("UC is already closed", HttpStatus.BAD_REQUEST);

        uc.setActive(Boolean.FALSE);

        repository.save(uc);

        return "UC was successfully closed";
    }

    @Override
    public String updateRanks(UpdateRankRequest request) {

        UC uc = getOrElseThrow(request.getId());

        uc.setRanks(request.getRanks());

        //TODO: Update student XPs for the new ranks
        //if call to students is successful:
        repository.save(uc);

        return "UC ranks successfully added";
    }

    @Override
    public String updateGrades(UpdateGradeRequest request) {

        UC uc = getOrElseThrow(request.getId());

        uc.setGradeValues(request.getGrades());

        //TODO: Update student XPs for the new ranks
        //if call to students is successful:
        repository.save(uc);

        return "UC grades successfully added";
    }

    @Override
    public String updateXPs(UpdateXPRequest request) {

        UC uc = getOrElseThrow(request.getId());

        uc.setXPs(request.getXPs());

        //TODO: Update student XPs for the new ranks
        //if call to students is successful:
        repository.save(uc);

        return "UC XPs successfully added";
    }

    private UC getOrElseThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new LSException("UC does not exist", HttpStatus.BAD_REQUEST));
    }
}
