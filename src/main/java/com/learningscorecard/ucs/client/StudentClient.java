package com.learningscorecard.ucs.client;

import com.learningscorecard.ucs.model.request.RemoveUsersRequest;
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

@FeignClient(value = "student", url = "https://ls-users.herokuapp.com")
public interface StudentClient {

    @RequestMapping(method = RequestMethod.DELETE, value = "/students/uc/all")
    LSResponse<String> removeStudents(@RequestBody RemoveUsersRequest request);

}
