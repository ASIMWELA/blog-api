package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.utils.YearsCalculator;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name="user")
@Table(name="users_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity
{
    @Column(name="first_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String firstName;

    @Column(name="last_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String lastName;

    @Column(name="user_name", unique = true,length = 50, nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String userName;

    @Column(name="profile_pic_path")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String profilePicPath;

    @Column(name="email", unique = true, nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String email;

    @Column(name="password", nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    String password;

    @Column(name="sex", length = 20)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String sex;

    @Column(name="is_online")
    boolean isOnline ;

    @Column(name="is_account_active")
    boolean isAccountActive ;


    @Column(name="age")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    int age ;

    @Column(name="dob")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JoinColumn(name="contact_info_id", referencedColumnName = "id")
    ContactInfoEntity contactInfo;

    String message;


    @OneToMany(mappedBy = "user",cascade = {CascadeType.REMOVE})
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<ExperienceEntity> experience;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.REMOVE})
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    List<EmploymentEntity> employment;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.REMOVE})
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SkillEntity> skills;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.REMOVE})
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<EducationEntity> education;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.REMOVE})
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<ProjectEntity> projects;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JoinTable(
            name="user_roles_table",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    List<RoleEntinty> roles ;

    public int getAge()
    {
        this.age = YearsCalculator.calculateYears(getDateOfBirth(), LocalDate.now());
        return this.age;
    }

}
