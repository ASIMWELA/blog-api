package com.personal.website.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.controller.ProjectController;
import com.personal.website.controller.UserController;
import com.personal.website.entity.*;
import com.personal.website.utils.CheckRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
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
    List<RoleEntinty> roles;
    private boolean isAccountActive;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dateOfBirth;
    public static UserDto build(UserEntity entity) {
        UserDto model = null;
        //assign links to whether the person is an admin or not
        if (CheckRole.isAdmin(entity.getRoles())) {
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
                    .add(linkTo(methodOn(UserController.class).getUserContactInfo(entity.getUuid())).withRel("contact-info"));

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
