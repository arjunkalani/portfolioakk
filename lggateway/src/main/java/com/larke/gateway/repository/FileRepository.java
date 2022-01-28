package com.larke.gateway.repository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.larke.gateway.model.File;

@Repository
@ComponentScan(basePackages = "com.larke.gateway")
public interface FileRepository extends JpaRepository<File, String> {

}
