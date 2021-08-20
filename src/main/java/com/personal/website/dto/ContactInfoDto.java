package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.entity.ContactInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@Getter
public class ContactInfoDto extends RepresentationModel<ContactInfoDto> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String phoneNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String physicalAddress;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String city;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String country;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String message;
    public static ContactInfoDto build(ContactInfoEntity entity){
        return ContactInfoDto.builder()
                .phoneNumber(entity.getPhoneNumber())
                .physicalAddress(entity.getPhysicalAddress())
                .city(entity.getCity())
                .country(entity.getCountry())
                .build();
    }
}
