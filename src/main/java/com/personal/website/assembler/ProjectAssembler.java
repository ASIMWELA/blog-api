package com.personal.website.assembler;

import com.personal.website.controller.UserController;
import com.personal.website.dto.ProjectDto;
import com.personal.website.entity.ProjectEntity;
import com.personal.website.utils.AppConstants;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectAssembler implements RepresentationModelAssembler<ProjectEntity, ProjectDto> {
    @Override
    public ProjectDto toModel(ProjectEntity entity) {
        return ProjectDto.builder().name(entity.getName())
                .description(entity.getDescription())
                .locationLink(entity.getLocationLink())
                .role(entity.getRole())
                .collaborators(entity.getCollaborators())
                .build();
    }

    @Override
    public CollectionModel<ProjectDto> toCollectionModel(Iterable<? extends ProjectEntity> entities) {
        CollectionModel<ProjectDto> projectModels = RepresentationModelAssembler.super.toCollectionModel(entities);
        projectModels.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
        return projectModels;
    }
}
