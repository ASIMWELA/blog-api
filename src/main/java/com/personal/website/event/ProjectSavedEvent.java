package com.personal.website.event;

import com.personal.website.entity.ProjectDetailsEntity;
import org.springframework.context.ApplicationEvent;

public class ProjectSavedEvent extends ApplicationEvent
{
    private ProjectDetailsEntity projectDetailsEntity;

    public ProjectSavedEvent( ProjectDetailsEntity projectDetailsEntity)
    {
        super(projectDetailsEntity);
        this.projectDetailsEntity = projectDetailsEntity;
    }

    public ProjectDetailsEntity getProjectDetailsEntity()
    {
        return projectDetailsEntity;
    }

}
