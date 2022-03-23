package com.spring.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.rest.model.FileDbModel;

@Repository
public interface StaticFileDbRepository extends JpaRepository<FileDbModel, String> {

}
