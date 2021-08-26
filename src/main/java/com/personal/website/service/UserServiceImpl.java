package com.personal.website.service;

import com.personal.website.assembler.*;
import com.personal.website.controller.UserController;
import com.personal.website.dto.*;
import com.personal.website.entity.*;
import com.personal.website.enumconstants.ERole;
import com.personal.website.exception.EntityAlreadyExistException;
import com.personal.website.exception.EntityNotFoundException;
import com.personal.website.exception.FailedToSaveProfile;
import com.personal.website.exception.OperationNotAllowedException;
import com.personal.website.payload.ApiResponse;
import com.personal.website.payload.SignUpRequest;
import com.personal.website.repository.*;
import com.personal.website.utils.AppConstants;
import com.personal.website.utils.CheckUserRole;
import com.personal.website.utils.UidGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    PasswordEncoder encoder;
    EducationRepository educationRepository;
    ContactInfoService contactInfoService;
    SendEmail sendEmail;
    EmploymentRepository employmentRepository;
    ExperienceRepository experienceRepository;
    RoleRepository roleRepository;
    SkillsRepository skillsRepository;
    ExperienceAssembler experienceAssembler;
    ContactInfoAssembler contactInfoAssembler;
    UserAssembler userAssembler;
    EducationAssembler educationAssembler;
    EmploymentAssembler employmentAssembler;
    SkillAssembler skillAssembler;
    PagedResourcesAssembler<UserEntity> pagedResourcesAssembler;

    @Override
    public ResponseEntity<ApiResponse> saveAdmin(SignUpRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName()))
            throw new EntityAlreadyExistException("UserDto Name  already taken. Try different one");

        if (userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new EntityAlreadyExistException("Email already taken. Try different one");

        UserEntity user = UserEntity.builder()
                .dateOfBirth(signUpRequest.getDateOfBirth())
                .email(signUpRequest.getEmail())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(signUpRequest.getPassword())
                .sex(signUpRequest.getSex())
                .userName(signUpRequest.getUserName())
                .isOnline(false)
                .build();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setUuid(UidGenerator.generateRandomString(12));
        RoleEntinty adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(
                () -> new EntityNotFoundException("Role not set")
        );
        List<RoleEntinty> roles = new ArrayList<>();
        roles.add(adminRole);
        user.setRoles(roles);
        userRepository.save(user);
        return new ResponseEntity<>(new ApiResponse(true, "Admin saved successfully"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse> updateAdmin(UserEntity user, String adminUsername) {
        UserEntity entity = userRepository.findByUserName(adminUsername).orElseThrow(
                () -> new EntityNotFoundException("No user with username" + adminUsername)
        );

        if (CheckUserRole.isAdmin(user.getRoles())) {

            userRepository.updateUser(user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), entity.getUserName());
            userRepository.save(user);
            return new ResponseEntity<>(new ApiResponse(true, "Admin updated"), HttpStatus.OK);
        } else {
            throw new OperationNotAllowedException("Subscribers cannot have profile picture");
        }
    }

    @Override
    @SneakyThrows
    public ResponseEntity<ApiResponse> saveSubscriber(SignUpRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName()))
            throw new EntityAlreadyExistException("UserDto Name  already taken. Try different one");

        if (userRepository.existsByEmail(signUpRequest.getEmail()))
            throw new EntityAlreadyExistException("Email already taken. Try different one");

        //allowing subscribers to have only user roles
        RoleEntinty userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(
                () -> new EntityNotFoundException("No role found")
        );
        UserEntity user = UserEntity.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .userName(signUpRequest.getUserName())
                .isAccountActive(true)
                .isOnline(false)
                .build();
        user.setUuid(UidGenerator.generateRandomString(12));
        user.setRoles(Collections.singletonList(userRole));
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        sendEmail.sendSuccessEmail(user);
        return new ResponseEntity<>(new ApiResponse(true, "Subscription successful"), HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<ApiResponse> addContactInfo(ContactInfoEntity contactInfoEntity, String adminUsername) {
                UserEntity user = userRepository.findByUserName(adminUsername)
                .orElseThrow(() -> new EntityNotFoundException("No user with username " + adminUsername));
        if (CheckUserRole.isAdmin(user.getRoles())) {
            contactInfoEntity.setUser(user);
            contactInfoEntity.setUuid(UidGenerator.generateRandomString(12));
            user.setContactInfo(contactInfoEntity);
            contactInfoService.save(contactInfoEntity);
            return new ResponseEntity<>(new ApiResponse(true, "Contact information updated"), HttpStatus.OK);
        } else {
            throw new OperationNotAllowedException("Subscribers cannot have contact information");
        }

    }

    @Override
    public ResponseEntity<ApiResponse> addExperience(ExperienceEntity experienceEntity, String adminUsername) {
        UserEntity user = userRepository.findByUserName(adminUsername)
                .orElseThrow(() -> new EntityNotFoundException("No user with username " + adminUsername));

        if (experienceRepository.existsByName(experienceEntity.getName()))
            throw new EntityAlreadyExistException("Experience name already exist. Try a different one");

        if (CheckUserRole.isAdmin(user.getRoles())) {
            List<ExperienceEntity> experiences = user.getExperience();
            experienceEntity.setUser(user);
            experiences.add(experienceEntity);
            user.setExperience(experiences);
            experienceRepository.save(experienceEntity);
            userRepository.save(user);
            return new ResponseEntity<>(new ApiResponse(true, "Experience details added"), HttpStatus.OK);
        } else {
            throw new OperationNotAllowedException("Subscribers cannot have Experience entity");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addEmployment(EmploymentEntity employmentEntity, String adminUsername) {
        UserEntity user = userRepository.findByUserName(adminUsername)
                .orElseThrow(() -> new EntityNotFoundException("No user with username " + adminUsername));
        if (employmentRepository.existsByCompany(employmentEntity.getCompany()))
            throw new EntityAlreadyExistException("Company already added");
        if (CheckUserRole.isAdmin(user.getRoles())) {
            List<EmploymentEntity> employmentEntities = user.getEmployment();
            employmentEntity.setUser(user);
            employmentEntities.add(employmentEntity);
            user.setEmployment(employmentEntities);
            employmentRepository.save(employmentEntity);
            userRepository.save(user);
            return new ResponseEntity<>(new ApiResponse(true, "Employment details added"), HttpStatus.OK);
        } else {
            throw new OperationNotAllowedException("Subscribers cannot have employment details");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addSkill(@NotBlank SkillEntity skillEntity, String adminUsername) {
        UserEntity user = userRepository.findByUserName(adminUsername)
                .orElseThrow(() -> new EntityNotFoundException("No user with username " + adminUsername));

        if (skillsRepository.existsByTechnology(skillEntity.getTechnology()))
            throw new EntityAlreadyExistException("Technology already added");

        if (CheckUserRole.isAdmin(user.getRoles())) {
            List<SkillEntity> skills = user.getSkills();
            skillEntity.setUser(user);
            skills.add(skillEntity);
            user.setSkills(skills);
            skillsRepository.save(skillEntity);
            userRepository.save(user);
            return new ResponseEntity<>(new ApiResponse(true, "Skills added successly"), HttpStatus.OK);
        } else {
            throw new OperationNotAllowedException("Subscribers cannot have skills details");
        }

    }

    @Override
    public ResponseEntity<ApiResponse> updateSkill(SkillEntity entity, String adminUsername) {
       //TODO: UPDATE COLLECTLY

        UserEntity user = userRepository.findByUserName(adminUsername)
                .orElseThrow(() -> new EntityNotFoundException("No user with username " + adminUsername));

        if (CheckUserRole.isAdmin(user.getRoles())) {
            SkillEntity skill = skillsRepository.findByTechnology(entity.getTechnology()).orElseThrow(
                    () -> new EntityNotFoundException("No skill with that technology found")
            );
            skillsRepository.updateUpdateSkill(entity.getSkills(), entity.getTechnology());
            //skillsRepository.save(newSkill);
            userRepository.save(user);
            return new ResponseEntity<>(new ApiResponse(true, "Skills updated"), HttpStatus.OK);
        } else {
            throw new OperationNotAllowedException("Subscribers cannot have skills details");
        }
    }

    @Override
    public ResponseEntity<ApiResponse> addEducation(@NotBlank EducationEntity educationEntity, String adminUsername) {
        UserEntity user = userRepository.findByUserName(adminUsername)
                .orElseThrow(() -> new EntityNotFoundException("No user with username " + adminUsername));

        if (educationRepository.existsByInstitution(educationEntity.getInstitution()))
            throw new EntityAlreadyExistException("Institution  " + educationEntity.getInstitution() + " already added");

        if (CheckUserRole.isAdmin(user.getRoles())) {
            List<EducationEntity> educationEntityList = user.getEducation();
            educationEntity.setUser(user);
            educationEntityList.add(educationEntity);
            user.setEducation(educationEntityList);
            educationRepository.save(educationEntity);
            userRepository.save(user);
            return new ResponseEntity<>(new ApiResponse(true, "education details added"), HttpStatus.OK);
        } else {
            throw new OperationNotAllowedException("Subscribers cannot education background");
        }

    }

    @Override
    public ResponseEntity<ApiResponse> deleteAdminExperience(String expName, String adminUsername) {
        UserEntity user = userRepository.findByUserName(adminUsername).orElseThrow(
                () -> new EntityNotFoundException("No user with name" + adminUsername)
        );

        ExperienceEntity experienceEntity = experienceRepository.findByName(expName).orElseThrow(
                () -> new EntityNotFoundException("No experience found with name " + expName)
        );
        experienceRepository.deleteExperience(experienceEntity.getName(), user);

        return new ResponseEntity<>(new ApiResponse(true, "Experience datails deleted"), HttpStatus.OK);
    }

    @Override
    @SneakyThrows
    public ResponseEntity<ApiResponse> uploadPhoto(String uploadDir, String fileName, MultipartFile imageFile) {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = imageFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new FailedToSaveProfile("Failed to update your profile pic");
        }

        return new ResponseEntity<>(new ApiResponse(true, "Photo uploaded"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollectionModel<?>> getAdminExperience(String adminUsername) {
        UserEntity userEntity = userRepository.findByUuid(adminUsername).orElseThrow(() ->
                new EntityNotFoundException("No user with the given identifier")
        );
        List<ExperienceEntity> experienceEntityList = userEntity.getExperience();
        if(experienceEntityList.isEmpty()){
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(ExperienceDto.class);
            CollectionModel<Object> expDetails = new CollectionModel<>(Collections.singletonList(wrapper));
            expDetails.add(linkTo(methodOn(UserController.class).getUser(userEntity.getUuid())).withRel("profile"));
            expDetails.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
            return new ResponseEntity<>(expDetails, HttpStatus.OK);
        }else {
            CollectionModel<ExperienceDto> experienceDtos = experienceAssembler.toCollectionModel(experienceEntityList);
            experienceDtos.add(linkTo(methodOn(UserController.class).getUser(userEntity.getUuid())).withRel("profile"));
            experienceDtos.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
            return new ResponseEntity<>(experienceDtos, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ContactInfoDto> getAdminContactInf(String adminUuid) {
        UserEntity entity = userRepository.findByUuid(adminUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the given identy")
        );
        ContactInfoEntity contactInfoEntity = entity.getContactInfo();
        ContactInfoDto contactInfoDto = contactInfoAssembler.toModel(contactInfoEntity);
        contactInfoDto.add(linkTo(methodOn(UserController.class).getUser(entity.getUuid())).withRel("profile"));
        contactInfoDto.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
        return new ResponseEntity<>(contactInfoDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PagedModel<UserDto>> getAllUsers(int page, int size) {
        Pageable paged = PageRequest.of(page, size);
        Page<UserEntity> users = userRepository.findAll(paged);
        PagedModel<UserDto> userPagedModel = pagedResourcesAssembler
                .toModel(users, userAssembler);
        return new ResponseEntity<>(userPagedModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CollectionModel<?>> getAdminEducationDetails(String adminUuid) {
        UserEntity userEntity = userRepository.findByUuid(adminUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the provided identifier")
        );
        if (!CheckUserRole.isAdmin(userEntity.getRoles())) {
            throw new OperationNotAllowedException("You do not have permission to have education details");
        }

        List<EducationEntity> educationEntities = userEntity.getEducation();

        if(educationEntities.isEmpty()){
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(EducationDto.class);
            CollectionModel<Object> educationDetails = new CollectionModel<>(Collections.singletonList(wrapper));
            educationDetails.add(linkTo(methodOn(UserController.class).getUser(userEntity.getUuid())).withRel("profile"));
            educationDetails.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
            return new ResponseEntity<>(educationDetails, HttpStatus.OK);
        }
        else {
            CollectionModel<EducationDto> educationDtoCollectionModel = educationAssembler.toCollectionModel(educationEntities);


            educationDtoCollectionModel.add(linkTo(methodOn(UserController.class).getUser(userEntity.getUuid())).withRel("profile"));
           educationDtoCollectionModel.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
            return new ResponseEntity<>(educationDtoCollectionModel, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<CollectionModel<?>> getAdminEmploymentDetails(String adminUuid) {
        UserEntity user = userRepository.findByUuid(adminUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the provided identifier")
        );
        if (!CheckUserRole.isAdmin(user.getRoles())) {
            throw new OperationNotAllowedException("You do not have permission to have education details");
        }
        List<EmploymentEntity> employmentEntities = user.getEmployment();
        if(employmentEntities.isEmpty()){
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(EmploymentDto.class);
            CollectionModel<Object> emptyEmpDetails = new CollectionModel<>(Collections.singletonList(wrapper));
            emptyEmpDetails.add(linkTo(methodOn(UserController.class).getUser(user.getUuid())).withRel("profile"));
            emptyEmpDetails.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
            return new ResponseEntity<>(emptyEmpDetails, HttpStatus.OK);
        }else {
            CollectionModel<EmploymentDto> employmentDtoCollectionModel = employmentAssembler.toCollectionModel(employmentEntities);
            employmentDtoCollectionModel.add(linkTo(methodOn(UserController.class).getUser(user.getUuid())).withRel("profile"));
            employmentDtoCollectionModel.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
            return new ResponseEntity<>(employmentDtoCollectionModel, HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<CollectionModel<?>> getAdminSkillDetails(String adminUuid) {
        UserEntity userEntity = userRepository.findByUuid(adminUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the provided identifier")
        );
        if (!CheckUserRole.isAdmin(userEntity.getRoles())) {
            throw new OperationNotAllowedException("You do not have permission to have education details");
        }
        List<SkillEntity> skillEntities = userEntity.getSkills();
          if(skillEntities.isEmpty()){
             EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
             EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(SkillsDto.class);
             CollectionModel<Object> skillsDetails = new CollectionModel<>(Collections.singletonList(wrapper));
             skillsDetails.add(linkTo(methodOn(UserController.class).getUser(userEntity.getUuid())).withRel("profile"));
             skillsDetails.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
             return new ResponseEntity<>(skillsDetails, HttpStatus.OK);
        }else {
              CollectionModel<SkillsDto> skillsDtoCollectionModel = skillAssembler.toCollectionModel(skillEntities);
              skillsDtoCollectionModel.add(linkTo(methodOn(UserController.class).getUser(userEntity.getUuid())).withRel("profile"));
              skillsDtoCollectionModel.add(linkTo(methodOn(UserController.class).getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
              return new ResponseEntity<>(skillsDtoCollectionModel, HttpStatus.OK);
         }
    }

}

