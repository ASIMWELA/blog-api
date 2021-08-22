package com.personal.website.dto;

import com.personal.website.entity.ProjectEntity;
import com.personal.website.entity.UserEntity;
import lombok.Builder;
import org.springframework.hateoas.RepresentationModel;

@Builder
public class ResourceDto extends RepresentationModel<ResourceDto> {
    private UserEntity users;
    private ProjectEntity projects;
    public static ResourceDto build(UserEntity users, ProjectEntity projects) {
        return ResourceDto.builder().users(users).projects(projects).build();
    }
}
