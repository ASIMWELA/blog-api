package com.personal.website.assembler;

import com.personal.website.dto.EducationDto;
import com.personal.website.entity.EducationEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EducationAssembler implements RepresentationModelAssembler<EducationEntity, EducationDto> {
    @Override
    public EducationDto toModel(EducationEntity entity) {
        return EducationDto.build(entity);
    }
    @Override
    public CollectionModel<EducationDto> toCollectionModel(Iterable<? extends EducationEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
