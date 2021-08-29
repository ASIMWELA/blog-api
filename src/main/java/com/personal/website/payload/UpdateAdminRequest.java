package com.personal.website.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminRequest
{
    private String password;
    private String userName;
    private String lastName;
    private String email;
    private String firstName;
    private String sex;
    private LocalDate dateOfBirth;
}
