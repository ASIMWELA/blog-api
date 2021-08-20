package com.personal.website.controller;

import com.personal.website.assembler.ResourceAssembler;
import com.personal.website.entity.ResourceEntityCollection;
import com.personal.website.dto.ResourceDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {


    ResourceAssembler resourceAssembler;

    public ResourceController(ResourceAssembler resourceAssembler) {
        this.resourceAssembler = resourceAssembler;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<ResourceDto>> getResources(){
        List<ResourceEntityCollection> resources = new ArrayList<>();

        return new ResponseEntity<>(resourceAssembler.toCollectionModel(resources), HttpStatus.OK);
    }

}
