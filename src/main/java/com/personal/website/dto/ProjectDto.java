package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.entity.ProjectDetailsEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
public class ProjectDto extends RepresentationModel<ProjectDto> {

    //projects
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> collaborators;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String locationLink;

    public static ProjectDto build(ProjectDetailsEntity entity) {
        ProjectDto projectDto = null;
        if (entity.getCollaborators() == null) {
            projectDto = ProjectDto.builder()
                    .locationLink(entity.getLocationLink())
                    .role(entity.getRole())
                    .description(entity.getDescription())
                    .name(entity.getName())
                    .build();
        } else {
            projectDto = ProjectDto.builder()
                    .locationLink(entity.getLocationLink())
                    .collaborators(entity.getCollaborators())
                    .role(entity.getRole())
                    .description(entity.getDescription())
                    .name(entity.getName())
                    .build();

        }
        return projectDto;
    }

}
