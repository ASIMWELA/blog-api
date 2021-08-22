package com.personal.website.controller;

import com.personal.website.assembler.ProjectAssembler;
import com.personal.website.dto.ProjectDto;
import com.personal.website.entity.ProjectEntity;
import com.personal.website.exception.EntityNotFoundException;
import com.personal.website.payload.ApiResponse;
import com.personal.website.repository.ProjectDetailsRepository;
import com.personal.website.service.ProjectDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private ProjectAssembler projectAssembler;

    @Autowired
    private ProjectDetailsService projectDetailsService;


    @RequestMapping(
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<ProjectDto>> getAllProjects() {
        List<ProjectEntity> projects = projectDetailsRepository.findAll();

        return new ResponseEntity<>(projectAssembler.toCollectionModel(projects), HttpStatus.OK);

    }

    @RequestMapping(
            value = "/{projectName}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
                    )
    public ProjectDto getProject(@PathVariable("projectName") String projectName) {

           ProjectEntity entity = projectDetailsRepository.findByName(projectName).orElseThrow(() -> new EntityNotFoundException("NO UserDto with id " + projectName));

           ProjectDto model = ProjectDto.build(entity);
           model.add(linkTo(methodOn(ProjectController.class)
                        .getAllProjects()).withRel("projects"));

        return model;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ApiResponse> addProject(@NotNull @RequestBody ProjectEntity projectEntity) throws InterruptedException
    {

        projectDetailsService.saveProject(projectEntity);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "project added successfully" ),HttpStatus.CREATED);

    }
    @RequestMapping(value="/{projectName}",
            method = RequestMethod.PUT,
            produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ApiResponse> editProjectEntity(@PathVariable("projectName") String projectName, @NotBlank @RequestBody ProjectEntity projectEntity) throws InterruptedException
    {

        projectDetailsService.editProject(projectName, projectEntity);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true,"project updated successfully" ),HttpStatus.OK);

    }
    @RequestMapping(value="/{projectName}",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ApiResponse> deleteProject(@PathVariable("projectName") String projectName) throws InterruptedException
    {
        ProjectEntity entity = projectDetailsRepository.findByName((projectName)).orElseThrow(()->new EntityNotFoundException("No project found with name "+ projectName));

        projectDetailsRepository.delete(entity);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "project deletion successful" ),HttpStatus.OK);

    }
}
