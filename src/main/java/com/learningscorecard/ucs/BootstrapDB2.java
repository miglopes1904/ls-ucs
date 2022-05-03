package com.learningscorecard.ucs;

import com.learningscorecard.ucs.model.entity.Guild;
import com.learningscorecard.ucs.model.entity.UC;
import com.learningscorecard.ucs.repository.GuildRepository;
import com.learningscorecard.ucs.repository.UCRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;


@Component
@Order(2)
@Slf4j
public class BootstrapDB2 implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapDB2.class);
    final UUID UC_ID = UUID.randomUUID();
    final UUID STUDENT_ID = UUID.randomUUID();
    private final UCRepository UcRepository;
    private final GuildRepository guildRepository;


    public BootstrapDB2(UCRepository ucRepository, GuildRepository guildRepository) {
        UcRepository = ucRepository;
        this.guildRepository = guildRepository;
    }


    //@Override
    @Transactional
    public void run(String... args) throws Exception {

        //UC thisUC = UcRepository.findByName("Unit Course").get();
        // UCRepository.save(UC.builder().students(Sets.newHashSet(user1)).teachers(Sets.newHashSet(user2)).build());

        //Guild guild = Guild.builder().name("guild").ownerUC(thisUC).build();

        //guildRepository.save(guild);

    }

}
