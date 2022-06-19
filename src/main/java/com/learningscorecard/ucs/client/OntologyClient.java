package com.learningscorecard.ucs.client;

import com.learningscorecard.ucs.model.request.ontology.CreateOntologyUCRequest;
import com.learningscorecard.ucs.model.request.ontology.Mapping;
import com.learningscorecard.ucs.model.request.ontology.SyllabusContent;
import com.learningscorecard.ucs.model.response.LSResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "ontology", url = "https://ls-ontology.herokuapp.com/")
public interface OntologyClient {

    @RequestMapping(method = RequestMethod.GET, value = "/mappings/{id}")
    List<Mapping> getMappings(@PathVariable("id") UUID id);

    @RequestMapping(method = RequestMethod.GET, value = "/contents/{id}")
    List<SyllabusContent> getContents(@PathVariable("id") UUID id);

    @RequestMapping(method = RequestMethod.POST, value = "/ucs")
    void createUC(@RequestBody CreateOntologyUCRequest request);

    @RequestMapping(method = RequestMethod.DELETE, value = "/ucs/{id}")
    void deleteUC(@PathVariable("id") UUID id);
}
