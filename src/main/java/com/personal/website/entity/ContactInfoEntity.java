package com.personal.website.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity(name="contact_info")
@Table(name="contact_info_table")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactInfoEntity extends BaseEntity
{
    @Column(name="phone_number",unique=true, length = 20)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty(message = "phoneNumber cannot be empty")
    String phoneNumber;

    @Column(name="physical_address")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty(message = "physicalAddress cannot be empty")
    String physicalAddress;

    @Column(name="city")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty(message = "city cannot be empty")
    String city;

    @Column(name="country")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty(message = "country cannot be empty")
    String country;

    @JsonIgnore
    @OneToOne(mappedBy = "contactInfo")
    UserEntity user;
}
