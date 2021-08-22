package com.personal.website.repository;

import com.personal.website.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectDetailsRepository extends JpaRepository<ProjectEntity, Long>
{

    Optional<ProjectEntity> findByName(String name);
    boolean existsByName(String name);
}
