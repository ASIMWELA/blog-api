package com.personal.website.event;

import com.personal.website.entity.ProjectEntity;
import org.springframework.context.ApplicationEvent;

public class ProjectSavedEvent extends ApplicationEvent
{
    private final ProjectEntity projectEntity;

    public ProjectSavedEvent( ProjectEntity projectEntity)
    {
        super(projectEntity);
        this.projectEntity = projectEntity;
    }

    public ProjectEntity getProjectDetailsEntity()
    {
        return projectEntity;
    }

}
