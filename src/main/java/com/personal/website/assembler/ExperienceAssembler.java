package com.personal.website.assembler;

import com.google.common.collect.Iterables;
import com.personal.website.controller.UserController;
import com.personal.website.entity.ExperienceEntity;
import com.personal.website.dto.ExperienceDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ExperienceAssembler implements RepresentationModelAssembler<ExperienceEntity, ExperienceDto> {
    @Override
    public ExperienceDto toModel(ExperienceEntity entity) {
        return  ExperienceDto.builder()
                .years(entity.getYears())
                .name(entity.getName())
                .months(entity.getMonths())
                .years(entity.getYears())
                .uuid(entity.getUuid())
                .beganOn(entity.getBeganOn())
                .build();
    }

    @Override
    public CollectionModel<ExperienceDto> toCollectionModel(Iterable<? extends ExperienceEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
