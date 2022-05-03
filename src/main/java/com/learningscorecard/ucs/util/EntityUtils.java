package com.learningscorecard.ucs.util;

import com.learningscorecard.ucs.exception.LSException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
public class EntityUtils {

    private final EntityManager manager;

    public EntityUtils(EntityManager manager) {
        this.manager = manager;
    }

    public <T> T getReference(Class<T> type, UUID id) {

        T result = manager.getReference(type, id);

        try {
            result.toString();
        } catch (EntityNotFoundException exception) {
            throw new LSException("The entity of type " + type.getSimpleName() + " does not exist" );
        }

        return result;
    }

/*
    public <T> List<T> getReferences(Class<T> type, UUID id) {

        return manager.getReference(type, id);
    }*/

}
