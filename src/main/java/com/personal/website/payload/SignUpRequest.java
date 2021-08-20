package com.personal.website.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest
{

    @Size(min = 4, max = 40, message="first name too short")
    String firstName;

    @Size(min = 4, max = 40, message="last name too short")
    String lastName;

    @NotEmpty(message = "UserDto name cannot be empty")
    @Size(min = 3, max = 15, message = "user name should be between 3 to 15 chars")
    String userName;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 5, max = 20)
    String password;

    @NotEmpty(message = "Email cannot be empty")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email, please check your email")
    String email;

    boolean isAccountEnabled;

    String sex;

    boolean isOnline;

    int age;

    LocalDate dateOfBirth;

}
