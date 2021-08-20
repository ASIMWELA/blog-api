package com.personal.website.entity;

import com.personal.website.enumconstants.ERole;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name="roles_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RoleEntinty extends BaseEntity
{
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 20)
    private ERole name;
}
