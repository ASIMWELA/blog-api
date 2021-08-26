package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.entity.SkillEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Relation("skillsDetails")
public class SkillsDto extends RepresentationModel<SkillsDto> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String technology;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> skills;
    String message;
    public static SkillsDto build(SkillEntity entity){
        return SkillsDto.builder()
                .technology(entity.getTechnology())
                .skills(entity.getSkills())
                .build();
    }
}
