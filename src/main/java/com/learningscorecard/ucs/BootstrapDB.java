package com.learningscorecard.ucs;

import com.learningscorecard.ucs.model.entity.Quest;
import com.learningscorecard.ucs.model.entity.UC;
import com.learningscorecard.ucs.repository.UCRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;


@Component
@Order(1)
@Slf4j
public class BootstrapDB implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapDB.class);
    final UUID UC_ID = UUID.randomUUID();
    final UUID STUDENT_ID = UUID.randomUUID();
    private final UCRepository UcRepository;

    public BootstrapDB(UCRepository ucRepository) {
        UcRepository = ucRepository;
    }


    //@Override
    @Transactional
    public void run(String... args) throws Exception {


        //UC uc = UC.builder().name("Unit Course").acronym("UC").semester(1).academicYear("2022/2023")
                //.alliances(Arrays.asList("MEI", "IGE", "ALLIANCE")).build();
        //UcRepository.save(uc);

        //Quest quest = Quest.builder().startDate(new Date()).description("desc").title("title").build();
        //Quest quest2 = Quest.builder().startDate(new Date()).description("desc1").title("title1").build();
        //questRepository.saveAll(Arrays.asList(quest, quest2));

    }

}
