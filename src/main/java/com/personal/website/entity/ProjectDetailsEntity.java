package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="projects_table")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Setter
@Getter
public class ProjectDetailsEntity extends BaseEntity
{
    @Column(name="name", unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @Column(name="description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Column(name="role")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;

    @Column(name="collaborators")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ElementCollection
    @CollectionTable(name="user_project_collaboratos_table", joinColumns=@JoinColumn(name="uuid"))
    private List<String> collaborators;

    @Column(name="location_link")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String locationLink;

    @JsonIgnore
    @ManyToOne(targetEntity = UserEntity.class,cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id")
    UserEntity user;

}
