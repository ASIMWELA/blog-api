package com.personal.website.assembler;

import com.personal.website.dto.SkillsDto;
import com.personal.website.entity.SkillEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SkillAssembler implements RepresentationModelAssembler<SkillEntity, SkillsDto> {
    @Override
    public SkillsDto toModel(SkillEntity entity) {
        return SkillsDto.build(entity);
    }
    @Override
    public CollectionModel<SkillsDto> toCollectionModel(Iterable<? extends SkillEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
