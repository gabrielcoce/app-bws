package com.gcoce.bc.ws.repositories.beneficio;
import com.gcoce.bc.ws.entities.beneficio.ERole;
import com.gcoce.bc.ws.entities.beneficio.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole name);
}