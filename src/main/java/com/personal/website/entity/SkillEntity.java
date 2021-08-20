package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Entity(name="skills")
@Table(name="skills_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillEntity extends BaseEntity
{
    @Column(name="tech", nullable = false, unique = true)
    private String technology;

    @Column(name="skills", nullable = false)
    @ElementCollection
    @CollectionTable(name="user_technology_skills_table", joinColumns=@JoinColumn(name="uuid"))
    List<String> skills;

    @JsonIgnore
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name="user_id")
    UserEntity user;
}
