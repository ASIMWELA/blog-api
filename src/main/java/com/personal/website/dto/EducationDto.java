package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.entity.EducationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "educationDetails")
public class EducationDto extends RepresentationModel<EducationDto> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String institution;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> awards;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String period;
    public static EducationDto build(EducationEntity entity){
        return EducationDto.builder().institution(entity.getInstitution())
                .awards(entity.getAwards())
                .period(entity.getPeriod())
                .build();
    }
}
