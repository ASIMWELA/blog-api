package com.personal.website.service;

import com.personal.website.dto.ContactInfoDto;
import com.personal.website.dto.UserDto;
import com.personal.website.entity.*;
import com.personal.website.payload.ApiResponse;
import com.personal.website.payload.SignUpRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public interface UserService {
    ResponseEntity<ApiResponse> saveAdmin(SignUpRequest signUpRequest);
    ResponseEntity<ApiResponse> updateAdmin(UserEntity user, String adminUsername);
    ResponseEntity<ApiResponse> saveSubscriber(SignUpRequest signUpRequest);
    ResponseEntity<ApiResponse> addContactInfo(ContactInfoEntity contactInfoEntity, String adminUsername);
    ResponseEntity<ApiResponse> addExperience(ExperienceEntity experienceEntity, String adminUsername);
    ResponseEntity<ApiResponse> addEmployment(EmploymentEntity employmentEntity, String adminUsername);
    ResponseEntity<ApiResponse> addSkill(@NotBlank SkillEntity skillEntity, String adminUsername);
    ResponseEntity<ApiResponse> updateSkill(SkillEntity entity, String adminUsername);
    ResponseEntity<ApiResponse> addEducation(@NotBlank EducationEntity educationEntity, String adminUsername);
    ResponseEntity<ApiResponse> editEducationDetails(@NotBlank EducationEntity educationEntity, String adminUsername);
    ResponseEntity<ApiResponse> uploadPhoto(String uploadDir, String fileName, MultipartFile imageFile);
    ResponseEntity<CollectionModel<?>> getAdminExperience(String adminUsername);
    ResponseEntity<ContactInfoDto> getAdminContactInf(String adminUuid);
    ResponseEntity<PagedModel<UserDto>> getAllUsers(int page, int size);
    ResponseEntity<CollectionModel<?>> getAdminEducationDetails(String adminUuid);
    ResponseEntity<CollectionModel<?>> getAdminEmploymentDetails(String adminUuid);
    ResponseEntity<CollectionModel<?>> getAdminSkillDetails(String adminUuid);
}
