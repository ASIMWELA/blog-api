package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.personal.website.utils.YearsCalculator;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="experience")
@Table(name="experience_table")
@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperienceEntity extends BaseEntity
{
    @Column(name="exp_name", unique = true)
    String name;

    @Column(name="beganOn")
    LocalDate beganOn;

    @Column(name="years")
    int years;

    @Column(name="months")
    int months;

    public int getYears()
    {
        this.years = YearsCalculator.calculateYears(getBeganOn(), LocalDate.now());
        return this.years;
    }

    public  int getMonths()
    {
        this.months = YearsCalculator.calculateMonths(getBeganOn(), LocalDate.now());
        return this.months;
    }

    @ManyToOne(targetEntity = UserEntity.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    UserEntity user;
}
