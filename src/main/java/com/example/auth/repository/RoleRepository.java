package com.example.auth.repository;

import com.example.auth.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<AppRole,Long> {

    AppRole findByName(String name);
}
