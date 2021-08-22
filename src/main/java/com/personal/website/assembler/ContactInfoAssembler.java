package com.personal.website.assembler;

import com.personal.website.dto.ContactInfoDto;
import com.personal.website.entity.ContactInfoEntity;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ContactInfoAssembler implements RepresentationModelAssembler<ContactInfoEntity, ContactInfoDto> {
    @Override
    public ContactInfoDto toModel(ContactInfoEntity entity) {
       return ContactInfoDto.build(entity);
    }
}
