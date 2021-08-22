package com.personal.website.service;

import com.personal.website.entity.ProjectEntity;
import com.personal.website.event.ProjectSavedEvent;
import com.personal.website.exception.EntityAlreadyExistException;
import com.personal.website.exception.EntityNotFoundException;
import com.personal.website.repository.ProjectDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ProjectDetailsService
{
    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public ProjectEntity saveProject(ProjectEntity projectEntity)
    {
        if(projectDetailsRepository.existsByName(projectEntity.getName()))
            throw new EntityAlreadyExistException("ProjectDto already added");

        ProjectEntity project = projectEntity.builder()
                                                            .name(projectEntity.getName())
                                                            .description(projectEntity.getDescription())
                                                            .role(projectEntity.getRole())
                                                            .locationLink(projectEntity.getLocationLink())
                                                            .collaborators(projectEntity.getCollaborators())
                                                            .build();

        projectDetailsRepository.save(project);
        ProjectSavedEvent event = new ProjectSavedEvent(project);
        applicationEventPublisher.publishEvent(event);

        return project;
    }
    public void editProject(String projectName, ProjectEntity projectEntity)
    {
        ProjectEntity project =projectDetailsRepository.findByName(projectName).orElseThrow(()->new EntityNotFoundException("No project found with name " + projectEntity.getName()));

//       projectDetailsRepository.updateUpdateProject(
//                        projectEntity.getName(),
//                        projectEntity.getDescription(),
//                        projectEntity.getRole(),
//                        projectEntity.getCollaborators(),
//                        projectEntity.getLocationLink(),
//                        project.getName()

       // );

    }
}
