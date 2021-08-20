package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.entity.ExperienceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class ExperienceDto extends RepresentationModel<ExperienceDto> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate beganOn;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int years;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int months;
    public static ExperienceDto build(ExperienceEntity entity){
        return ExperienceDto.builder()
                .uuid(entity.getUuid())
                .name(entity.getName())
                .beganOn(entity.getBeganOn())
                .months(entity.getMonths())
                .years(entity.getYears())
                .build();
    }
}
