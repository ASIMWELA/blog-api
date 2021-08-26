package com.personal.website.repository;

import com.personal.website.entity.ProjectEntity;
import com.personal.website.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectDetailsRepository extends JpaRepository<ProjectEntity, Long>
{

    Optional<ProjectEntity> findByName(String name);
    boolean existsByName(String name);
    @Query(value = "SELECT * FROM projects_table as p WHERE p.user_id = :userId", nativeQuery = true)
    Page<ProjectEntity> findByUserId(@Param("userId")Long userId, Pageable pageable);
}
