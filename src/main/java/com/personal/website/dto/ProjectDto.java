package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.entity.ProjectEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Data
@Builder
@Relation(collectionRelation = "projects",itemRelation = "project")
public class ProjectDto extends RepresentationModel<ProjectDto> {

    //projects
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> collaborators;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String locationLink;
    public static ProjectDto build(ProjectEntity entity) {
        return ProjectDto.builder()
                    .locationLink(entity.getLocationLink())
                    .role(entity.getRole())
                    .collaborators(entity.getCollaborators())
                    .description(entity.getDescription())
                    .name(entity.getName())
                    .build();
    }

}
