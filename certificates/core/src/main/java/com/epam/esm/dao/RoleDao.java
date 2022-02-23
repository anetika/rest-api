package com.epam.esm.dao;

import com.epam.esm.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface that contains functionality for RoleDao
 */
@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
    /**
     * Find by role's name role.
     *
     * @param roleName the name of a role
     * @return the role
     */
    Role findByRoleName(String roleName);
}
