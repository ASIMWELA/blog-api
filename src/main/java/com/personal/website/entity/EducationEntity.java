package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity(name="education")
@Table(name="education_table")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationEntity extends BaseEntity
{

    @Column(name="institution", nullable = false)
    String institution;

    @ElementCollection
    @CollectionTable(name="user_education_awards_table", joinColumns=@JoinColumn(name="uuid"))
    @Column(name="awards", nullable = false)
    List<String> awards;

    @Column(name="period")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String period;


    @JsonIgnore
    @ManyToOne(targetEntity = UserEntity.class,cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id")
    UserEntity user;
}
