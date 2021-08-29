package com.personal.website.service;

import com.personal.website.assembler.ProjectAssembler;
import com.personal.website.controller.UserController;
import com.personal.website.dto.ProjectDto;
import com.personal.website.dto.UserDto;
import com.personal.website.entity.ProjectEntity;
import com.personal.website.entity.UserEntity;
import com.personal.website.event.ProjectSavedEvent;
import com.personal.website.exception.EntityAlreadyExistException;
import com.personal.website.exception.EntityNotFoundException;
import com.personal.website.payload.ApiResponse;
import com.personal.website.repository.ProjectDetailsRepository;
import com.personal.website.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProjectServiceImpl implements ProjectService {
    ProjectDetailsRepository projectDetailsRepository;
    ApplicationEventPublisher applicationEventPublisher;
    PagedResourcesAssembler<ProjectEntity> pagedResourcesAssembler;
    UserRepository userRepository;
    ProjectAssembler projectAssembler;

    @Override
    public ResponseEntity<ApiResponse> saveProject(ProjectEntity projectEntity) {
        if(projectDetailsRepository.existsByName(projectEntity.getName()))
            throw new EntityAlreadyExistException("ProjectDto already added");

        ProjectEntity project = ProjectEntity.builder()
                                    .name(projectEntity.getName())
                                    .description(projectEntity.getDescription())
                                    .role(projectEntity.getRole())
                                    .locationLink(projectEntity.getLocationLink())
                                    .collaborators(projectEntity.getCollaborators())
                                    .build();

        projectDetailsRepository.save(project);
        ProjectSavedEvent event = new ProjectSavedEvent(project);
        applicationEventPublisher.publishEvent(event);

        return new ResponseEntity<>(new ApiResponse(true, "project saved"), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<PagedModel<?>> getAdminProjects(int page, int size, String adminUuid) {
        UserEntity user = userRepository.findByUuid(adminUuid).orElseThrow(
                ()->new EntityNotFoundException("No user with the given identifier")
        );

        List<ProjectEntity> projects = user.getProjects();
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectEntity> pages = projectDetailsRepository.findByUserId(user.getId(), pageable);

        PagedModel<ProjectDto> projectsPagedModel = pagedResourcesAssembler
                .toModel(pages, projectAssembler);
        projectsPagedModel.add(linkTo(methodOn(UserController.class).getUser(user.getUuid())).withRel("profile"));
        return new ResponseEntity<>(projectsPagedModel, HttpStatus.OK);
    }

    //TODO: implement edit if necessary
    @Override
    public ResponseEntity<ApiResponse> editProject(String projectName, ProjectEntity projectEntity)
    {
        ProjectEntity project =projectDetailsRepository.findByName(projectName).orElseThrow(()->new EntityNotFoundException("No project found with name " + projectEntity.getName()));

//       projectDetailsRepository.updateUpdateProject(
//                        projectEntity.getName(),
//                        projectEntity.getDescription(),
//                        projectEntity.getRole(),
//                        projectEntity.getCollaborators(),
//                        projectEntity.getLocationLink(),
//                        project.getName()

       // );

        return null;
    }
}
