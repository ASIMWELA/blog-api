package com.personal.website.assembler;

import com.google.common.collect.Iterables;
import com.personal.website.dto.EmploymentDto;
import com.personal.website.entity.EmploymentEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EmploymentAssembler implements RepresentationModelAssembler<EmploymentEntity, EmploymentDto> {
    @Override
    public EmploymentDto toModel(EmploymentEntity entity) {


        return EmploymentDto.build(entity);
    }
    @Override
    public CollectionModel<EmploymentDto> toCollectionModel(Iterable<? extends EmploymentEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
