package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.controller.ProjectController;
import com.personal.website.controller.UserController;
import com.personal.website.entity.RoleEntinty;
import com.personal.website.entity.UserEntity;
import com.personal.website.utils.CheckUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
@Relation(itemRelation = "user", collectionRelation = "users")
public class UserDto extends RepresentationModel<UserDto> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profilePicPath;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int age;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isOnline;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sex;
    private boolean isAccountActive;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dateOfBirth;
    List<RoleEntinty> roles;

    public static UserDto build(UserEntity entity) {
        UserDto model = null;
        //assign links to whether the person is an admin or not
        if (CheckUserRole.isAdmin(entity.getRoles())) {
            model = UserDto.builder()
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .email(entity.getEmail())
                    .userName(entity.getUserName())
                    .sex(entity.getSex())
                    .isAccountActive(entity.isAccountActive())
                    .uid(entity.getUuid())
                    .age(entity.getAge())
                    .dateOfBirth(entity.getDateOfBirth())
                    .profilePicPath(entity.getProfilePicPath())
                    .isOnline(entity.isOnline())
                    .build()
                    .add(linkTo(methodOn(ProjectController.class).getAllProjects()).withRel("projects"))
                    .add(linkTo(methodOn(UserController.class).getUserExperience(entity.getUuid())).withRel("experience"))
                    .add(linkTo(methodOn(UserController.class).getUserContactInfo(entity.getUuid())).withRel("contact-info"))
                    .add(linkTo(methodOn(UserController.class).getUserSkillsDetails(entity.getUuid())).withRel("skills"))
                    .add(linkTo(methodOn(UserController.class).getUserEducationInfo(entity.getUuid())).withRel("education"))
                    .add(linkTo(methodOn(UserController.class).getUserEmploymentDetails(entity.getUuid())).withRel("employment"));
        } else {
            model = UserDto.builder().userName(entity.getUserName())
                    .email(entity.getEmail())
                    .isOnline(entity.isOnline())
                    .isAccountActive(entity.isAccountActive())
                    .uid(entity.getUuid())
                    .build();
        }
        return model;
    }
}
