package com.personal.website.assembler;

import com.personal.website.controller.UserController;
import com.personal.website.entity.ResourceEntityCollection;
import com.personal.website.dto.ResourceDto;
import com.personal.website.utils.AppConstants;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ResourceAssembler implements RepresentationModelAssembler<ResourceEntityCollection, ResourceDto>
{
    @Override
    public ResourceDto toModel(ResourceEntityCollection entity)
    {
        return null;
    }

    @Override
    public CollectionModel<ResourceDto> toCollectionModel(Iterable<? extends ResourceEntityCollection> entities)
    {
        CollectionModel<ResourceDto> resources = RepresentationModelAssembler.super.toCollectionModel(entities);
        resources.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
        return resources;
    }
}
