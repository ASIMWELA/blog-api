package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Entity(name="employment_entity")
@Table(name="employment_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmploymentEntity extends BaseEntity
{
    @Column(name="company", nullable = false, unique = true)
    String company;

    @Column(name="duration", nullable = false)
    String duration;

    @Column(name="availability", nullable = false)
    String availability;

    @Column(name="accomplishments")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ElementCollection
    @CollectionTable(name="user_employment_accomplishments_table", joinColumns=@JoinColumn(name="uuid"))
    List<String> accomplishments;

    @JsonIgnore
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name="user_id")
    UserEntity user;

}
