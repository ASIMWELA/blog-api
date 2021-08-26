package com.personal.website.controller;

import com.personal.website.entity.ProjectEntity;
import com.personal.website.exception.EntityNotFoundException;
import com.personal.website.payload.ApiResponse;
import com.personal.website.repository.ProjectDetailsRepository;
import com.personal.website.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(
            value = "/{adminUuid}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PagedModel<?>> getAdminProjects(@PositiveOrZero(message = "page number cannot be negative") @RequestParam(defaultValue = "0") Integer page, @PositiveOrZero(message = "page number cannot be negative") @RequestParam(defaultValue = "20") Integer size, @PathVariable String adminUuid) {
       return projectService.getAdminProjects(page, size, adminUuid);
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<ApiResponse> addProject(@NotNull @RequestBody ProjectEntity projectEntity) throws InterruptedException
    {

        projectService.saveProject(projectEntity);

        return new ResponseEntity<>(new ApiResponse(true, "project added successfully" ),HttpStatus.CREATED);

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

        projectService.editProject(projectName, projectEntity);

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

        return new ResponseEntity<>(new ApiResponse(true, "project deletion successful" ),HttpStatus.OK);

    }
}
