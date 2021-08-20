package com.personal.website.assembler;

import com.personal.website.dto.ContactInfoDto;
import com.personal.website.entity.ContactInfoEntity;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ContactInfoAssembler implements RepresentationModelAssembler<ContactInfoEntity, ContactInfoDto> {
    @Override
    public ContactInfoDto toModel(ContactInfoEntity entity) {
        ContactInfoDto contactInfoDto;
        if(entity==null){
            contactInfoDto = ContactInfoDto.builder().message("You don't currently have contact information").build();
        }else {
            contactInfoDto = ContactInfoDto.builder()
                    .city(entity.getCity())
                    .country(entity.getCountry())
                    .phoneNumber(entity.getPhoneNumber())
                    .physicalAddress(entity.getPhysicalAddress())
                    .build();

        }
        return contactInfoDto;
    }
}
