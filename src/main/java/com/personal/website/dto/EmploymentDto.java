package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.entity.EmploymentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Relation(collectionRelation = "employmentDetails")
public class EmploymentDto extends RepresentationModel<EmploymentDto>{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String company;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String duration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String availability;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> accomplishments;
    public static EmploymentDto build(EmploymentEntity entity){
        return EmploymentDto.builder()
                .company(entity.getCompany())
                .accomplishments(entity.getAccomplishments())
                .availability(entity.getAvailability())
                .duration(entity.getDuration())
                .build();
    }
}
