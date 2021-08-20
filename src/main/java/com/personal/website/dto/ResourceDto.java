package com.personal.website.dto;

import com.personal.website.entity.ProjectDetailsEntity;
import com.personal.website.entity.UserEntity;
import lombok.Builder;
import org.springframework.hateoas.RepresentationModel;

@Builder
public class ResourceDto extends RepresentationModel<ResourceDto> {
    private UserEntity users;
    private ProjectDetailsEntity projects;
    public static ResourceDto build(UserEntity users, ProjectDetailsEntity projects) {
        return ResourceDto.builder().users(users).projects(projects).build();
    }
}
