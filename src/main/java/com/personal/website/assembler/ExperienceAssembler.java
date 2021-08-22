package com.personal.website.assembler;

import com.personal.website.dto.ExperienceDto;
import com.personal.website.entity.ExperienceEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ExperienceAssembler implements RepresentationModelAssembler<ExperienceEntity, ExperienceDto> {
    @Override
    public ExperienceDto toModel(ExperienceEntity entity) {
        return  ExperienceDto.build(entity);
    }

    @Override
    public CollectionModel<ExperienceDto> toCollectionModel(Iterable<? extends ExperienceEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
