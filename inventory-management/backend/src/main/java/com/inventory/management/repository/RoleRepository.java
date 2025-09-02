package com.inventory.management.repository;

import java.util.Optional;

import com.inventory.management.models.ERole;
import com.inventory.management.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
