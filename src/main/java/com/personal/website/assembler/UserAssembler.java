package com.personal.website.assembler;

import com.personal.website.controller.ResourceController;
import com.personal.website.controller.UserController;
import com.personal.website.dto.UserDto;
import com.personal.website.entity.UserEntity;
import com.personal.website.utils.CheckUserRole;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements RepresentationModelAssembler<UserEntity, UserDto> {
    @Override
    public UserDto toModel(UserEntity entity) {
        UserDto model = null;
        if (CheckUserRole.isAdmin(entity.getRoles())) {
            model = UserDto.builder()
                    .email(entity.getEmail())
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .userName(entity.getUserName())
                    .isAccountActive(entity.isAccountActive())
                    .roles(entity.getRoles())
                    .uid(entity.getUuid())
                    .profilePicPath(entity.getProfilePicPath())
                    .build()
                    .add(linkTo(methodOn(UserController.class).getUser(entity.getUuid())).withSelfRel());
        } else {
            model = UserDto.builder()
                    .email(entity.getEmail())
                    .userName(entity.getUserName())
                    .roles(entity.getRoles())
                    .isAccountActive(entity.isAccountActive())
                    .isOnline(entity.isOnline())
                    .uid(entity.getUuid())
                    .build()
                    .add(linkTo(methodOn(UserController.class).getUser(entity.getUuid())).withSelfRel());
        }
        return model;
    }

    @Override
    public CollectionModel<UserDto> toCollectionModel(Iterable<? extends UserEntity> entities) {
        CollectionModel<UserDto> users = RepresentationModelAssembler.super.toCollectionModel(entities);
        users.add(linkTo(methodOn(ResourceController.class).getResources()).withRel("resources"));
        return users;
    }
}
