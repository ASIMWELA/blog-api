package com.personal.website.service;

import com.personal.website.dto.ProjectDto;
import com.personal.website.entity.ProjectEntity;
import com.personal.website.payload.ApiResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface ProjectService {
    ResponseEntity<ApiResponse> saveProject(ProjectEntity projectEntity);
    ResponseEntity<PagedModel<?>> getAdminProjects(int page, int size, String adminUuid);
    ResponseEntity<ApiResponse> editProject(String projectName, ProjectEntity projectEntity);
}
