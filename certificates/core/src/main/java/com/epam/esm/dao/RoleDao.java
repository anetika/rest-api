package com.epam.esm.dao;

import com.epam.esm.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
