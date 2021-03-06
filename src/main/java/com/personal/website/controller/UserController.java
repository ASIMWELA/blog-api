package com.personal.website.controller;

import com.google.common.collect.Lists;
import com.personal.website.assembler.ResourceAssembler;
import com.personal.website.assembler.UserAssembler;
import com.personal.website.dto.*;
import com.personal.website.entity.*;
import com.personal.website.exception.EntityNotFoundException;
import com.personal.website.exception.OperationNotAllowedException;
import com.personal.website.payload.*;
import com.personal.website.repository.*;
import com.personal.website.service.ChatMessageService;
import com.personal.website.service.UserService;
import com.personal.website.service.UserServiceImpl;
import com.personal.website.utils.AppConstants;
import com.personal.website.utils.CheckUserRole;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    UserService userService;
    PasswordEncoder encoder;
    ChatMessageService chatMessageService;
    MessageRepository messageRepository;
    ExperienceRepository experienceRepository;
    UserRepository userRepository;
    UserAssembler userAssembler;
    ResourceAssembler resourceAssembler;
    SkillsRepository skillsRepository;
    EducationRepository educationRepository;
    ContactInfoRepository contactInfoRepository;
    EmploymentRepository employmentRepository;
    JavaMailSender javaMailSender;

    public UserController(UserServiceImpl userService, PasswordEncoder encoder, ChatMessageService chatMessageService, MessageRepository messageRepository, ExperienceRepository experienceRepository, UserRepository userRepository, UserAssembler userAssembler, ResourceAssembler resourceAssembler, SkillsRepository skillsRepository, EducationRepository educationRepository, ContactInfoRepository contactInfoRepository, EmploymentRepository employmentRepository, JavaMailSender javaMailSender) {
        this.userService = userService;
        this.encoder = encoder;
        this.chatMessageService = chatMessageService;
        this.messageRepository = messageRepository;
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
        this.userAssembler = userAssembler;
        this.resourceAssembler = resourceAssembler;
        this.skillsRepository = skillsRepository;
        this.educationRepository = educationRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.employmentRepository = employmentRepository;
        this.javaMailSender = javaMailSender;
    }

    @Value("${app.emailReceiver}")
    String emailReceiver;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PagedModel<UserDto>> getAllUsers(@PositiveOrZero(message = "page number cannot be negative") @RequestParam(defaultValue = "0") Integer page,@PositiveOrZero(message = "page number cannot be negative") @RequestParam(defaultValue = "20") Integer size) {
        return userService.getAllUsers(page, size);
    }

    @RequestMapping(
            value = "/{userUuid}/experience-details",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<?>> getUserExperience(@PathVariable String userUuid) {
        return userService.getAdminExperience(userUuid);
    }


    @RequestMapping(
            value = "/admins",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> registerAdmin(@RequestBody @Valid SignUpRequest entity) {
        return userService.saveAdmin(entity);
    }

    @RequestMapping(
            value = "/subscribers",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> registerSubscriber(@Valid @RequestBody SignUpRequest entity) {
        return userService.saveSubscriber(entity);
    }

    @RequestMapping(
            value = "/{userName}",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateAdmin(@NotNull @RequestBody UserEntity userEntity, @PathVariable("userName") String userName) {
        UserEntity entity = userRepository.findByUserName(userName).orElseThrow(
                () -> new EntityNotFoundException("No user with username" + userName)
        );
        return userService.updateAdmin(userEntity, userName);
    }

    @RequestMapping(
            value = "/{userName}",
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAdmin(@PathVariable("userName") String userName) {
        UserEntity entity = userRepository.findByUserName(userName).orElseThrow(
                () -> new EntityNotFoundException("No user with username" + userName)
        );


        ContactInfoEntity cInfo = entity.getContactInfo();
        if (cInfo != null) {
            entity.getContactInfo().setUser(null);
        }
//        entity.getExperience().forEach(em->{
//            em.setUser(null);
//        });

        experienceRepository.deleteExperience(entity);
        //skillsRepository.deleteSkill(entity);
        educationRepository.deleteEducationDetails(entity);
        employmentRepository.deleteEmpDetails(entity);
        //de-link a user with its associated entities
        entity.setContactInfo(null);
        entity.setSkills(null);
        entity.setEmployment(null);
        entity.setExperience(null);
        entity.setRoles(null);

        userRepository.flush();
        userRepository.delete(entity);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Admin details deleted successfully"), HttpStatus.OK);
//
    }


    @RequestMapping(
            value = "/{uid}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable("uid") String iud) {
        return UserDto.build(userRepository.findByUuid(iud).orElseThrow(() -> new EntityNotFoundException("NO UserDto with id " + iud)))
                .add(linkTo(methodOn(UserController.class)
                        .getAllUsers(AppConstants.DEFAULT_PAGE_NUMBER, AppConstants.DEFAULT_PAGE_SIZE)).withRel("users"));
    }


    @RequestMapping(
            value = "/contact-info/{userName}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addContactInfo(@NotNull @RequestBody ContactInfoEntity contactInfoEntity, @PathVariable("userName") String userName) {
        userService.addContactInfo(contactInfoEntity, userName);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "contact details successfully added"), HttpStatus.OK);

    }

    @RequestMapping(
            value = "/contact-info/update/{phoneNumber}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateContactInfo(@NotNull @RequestBody ContactInfoEntity contactInfoEntity, @PathVariable("phoneNumber") String phoneNumber) {
        ContactInfoEntity contactInfo = contactInfoRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new EntityNotFoundException("No contact details found")
        );
        contactInfoRepository.updateContactInfo(
                contactInfo.getCity(),
                contactInfo.getPhysicalAddress(),
                contactInfo.getPhoneNumber(),
                contactInfo.getCountry(),
                phoneNumber
        );


        return new ResponseEntity<>(new ApiResponse(true, "contact details successfully updated"), HttpStatus.OK);

    }

    @RequestMapping(
            value = "/contact-info/{phoneNumber}/{userName}",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteContactDetails(@PathVariable("phoneNumber") String phoneNumber, @PathVariable("userName") String userName) {
        ContactInfoEntity contactInfo = contactInfoRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new EntityNotFoundException("No contact details found")
        );
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(
                () -> new EntityNotFoundException("No user found with name " + userName)
        );

        userEntity.setContactInfo(null);
        contactInfoRepository.delete(contactInfo);

        return new ResponseEntity<>(new ApiResponse(true, "contact details successfully deleted"), HttpStatus.OK);

    }

    @RequestMapping(
            value = "/experience/{userName}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addExperience(@NotNull @RequestBody ExperienceEntity experienceEntity, @PathVariable("userName") String userName) {
        userService.addExperience(experienceEntity, userName);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Experience details successfully added"), HttpStatus.OK);

    }

    @RequestMapping(value = "/employment/{userName}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addEmployment(@NotNull @RequestBody EmploymentEntity employmentEntity, @PathVariable("userName") String userName) {
        userService.addEmployment(employmentEntity, userName);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Employment details successfully added"), HttpStatus.OK);
    }

    @RequestMapping(value = "/employment/{userName}/{compNAme}",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteEmpDetails(@PathVariable("compNAme") String companyName, @PathVariable("userName") String userName) {
        UserEntity user = userRepository.findByUserName(userName).orElseThrow(
                () -> new EntityNotFoundException("No user with user name " + userName)
        );

        EmploymentEntity employmentEntity = employmentRepository.findByCompany(companyName).orElseThrow(
                () -> new EntityNotFoundException("No employment details for company " + companyName)
        );

        employmentRepository.deleteEmpDetails(employmentEntity.getCompany(), user);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Employment details successfully Deleted"), HttpStatus.OK);
    }

    @RequestMapping(value = "/employment/update/{compNAme}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateEmpDetails(@NotNull @RequestBody EmploymentEntity entity, @PathVariable("compNAme") String companyName) {

        EmploymentEntity emp = employmentRepository.findByCompany(companyName).orElseThrow(
                () -> new EntityNotFoundException("No employment details for company " + companyName)
        );

        employmentRepository.updateEmpInfo(entity.getCompany(), entity.getAccomplishments(), entity.getAvailability(), entity.getDuration(), emp.getCompany());
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Employment details successfully Updated"), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/skill/{userName}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addSkills(@NotNull @RequestBody SkillEntity skillEntity, @PathVariable("userName") String userName) {
        userService.addSkill(skillEntity, userName);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Skills details successfully added"), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/skill/update/{userName}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateSkill(@NotNull @RequestBody SkillEntity skillEntity, @PathVariable("userName") String userName) {
        userService.updateSkill(skillEntity, userName);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Skills details successfully updated"), HttpStatus.OK);
    }


    @RequestMapping(
            value = "/education/{userName}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addEducation(@NotNull @RequestBody EducationEntity educationEntity, @PathVariable("userName") String userName) {
        userService.addEducation(educationEntity, userName);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Education details successfully added"), HttpStatus.OK);

    }

    @RequestMapping(
            value = "/education/update/{institution}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateEducation(@NotNull @RequestBody EducationEntity educationEntity, @PathVariable("institution") String institution) {
        EducationEntity educationEntity1 = educationRepository.findByInstitution(institution).orElseThrow(
                () -> new EntityNotFoundException("No details on specified institution " + institution)
        );

        educationEntity1.setAwards(educationEntity.getAwards());
        educationEntity1.setInstitution(educationEntity.getInstitution());
        educationEntity1.setPeriod(educationEntity.getPeriod());
        educationRepository.save(educationEntity1);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Education details successfully updated"), HttpStatus.OK);

    }

    @RequestMapping(
            value = "/education/{userName}/{institution}",
            method = RequestMethod.DELETE,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteEducation(@PathVariable("userName") String userName, @PathVariable("institution") String institution) {
        EducationEntity educationEntity1 = educationRepository.findByInstitution(institution).orElseThrow(
                () -> new EntityNotFoundException("No details on specified institution " + institution)
        );

        UserEntity user = userRepository.findByUserName(userName).orElseThrow(
                () -> new EntityNotFoundException("No user with userName " + userName)
        );

        educationRepository.deleteEducationDetails(user, educationEntity1.getInstitution());

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Education details successfully deleted"), HttpStatus.OK);

    }


    @RequestMapping(
            value = "/skill/{tech}/{user}",
            method = RequestMethod.DELETE
    )
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<ApiResponse> deleteSkill(@PathVariable("tech") String tech, @PathVariable("user") String userName) {
        UserEntity user = userRepository.findByUserName(userName).orElseThrow(
                () -> new EntityNotFoundException("No user found with user name" + userName)
        );


        SkillEntity skill = skillsRepository.findByTechnology(tech).orElseThrow(
                () -> new EntityNotFoundException(("Skill not found with name" + tech))
        );
        //skillsRepository.deleteSkill(skill.getTechnology(), user);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Skill deleted successfully"), HttpStatus.OK);
    }
    @RequestMapping(value = "/chat/messages", method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    public List<MessageEntity> getMessages() {
        Pageable page = PageRequest.of(0, 40);
        List<MessageEntity> messages = messageRepository.getAllMessages(page);
        List<MessageEntity> recentMessages = Lists.reverse(messages);


        return recentMessages;
    }

    @RequestMapping(value = "/chat/totalUsersOnline", method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<JSONObject> getTotalOnlineUsers() {
        int total = 12;
        JSONObject j = new JSONObject();
        j.appendField("totalOnlineUsers", total);

        return new ResponseEntity<JSONObject>(j, HttpStatus.OK);
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        return ResponseEntity
                .ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages(@PathVariable String senderId,
                                              @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable Long id) {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }


    //send email directly
    @RequestMapping(
            value = "/sendEmail",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<ApiResponse> sendEmail(@NotNull @RequestBody EmailMessage emailMessage) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(emailReceiver);
        mail.setFrom(emailMessage.getSenderEmail());
        mail.setSubject(emailMessage.getTitle());
        mail.setText(emailMessage.getContent());

        javaMailSender.send(mail);
        return new ResponseEntity<>(new ApiResponse(true, "Email sent successfully"), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/upload-profile/{userName}",
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE}
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam("profileImage") MultipartFile imageFile, @PathVariable("userName") String userName) throws IOException {

        UserEntity user = userRepository.findByUserName(userName).orElseThrow(() ->
                new EntityNotFoundException("No user with id " + userName)
        );
        if (!CheckUserRole.isAdmin(user.getRoles())) {
            throw new OperationNotAllowedException("UserDto should only be an admin");
        }
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());


        //delete previous profile pic

        if (user.getProfilePicPath() != null) {
            Path p = Paths.get(user.getProfilePicPath());

            if (Files.exists(p)) {
                user.setProfilePicPath(null);
                Files.delete(p);
            }

        }

        String uploadDir = "user-photos/" + user.getUserName();

        String location = uploadDir + "/" + fileName;

        userService.uploadPhoto(uploadDir, fileName, imageFile);

        user.setProfilePicPath(location);

        userRepository.save(user);


        return new ResponseEntity<>(new ApiResponse(true, "Uploaded successfully"), HttpStatus.OK);

    }

    @GetMapping("/{userUuid}/contact-info")
    public ResponseEntity<ContactInfoDto> getUserContactInfo(@NonNull @PathVariable String userUuid) {
        return userService.getAdminContactInf(userUuid);
    }

    @GetMapping("/{userUuid}/education-details")
    public ResponseEntity<CollectionModel<?>> getUserEducationInfo(@NonNull @PathVariable String userUuid) {
        return userService.getAdminEducationDetails(userUuid);
    }

    @GetMapping("/{userUuid}/employment-details")
    public ResponseEntity<CollectionModel<?>> getUserEmploymentDetails(@NonNull @PathVariable String userUuid){
        return userService.getAdminEmploymentDetails(userUuid);
    }

    @GetMapping("/{userUuid}/skill-details")
    public ResponseEntity<CollectionModel<?>> getUserSkillsDetails(@NonNull @PathVariable String userUuid){
        return userService.getAdminSkillDetails(userUuid);
    }

}
