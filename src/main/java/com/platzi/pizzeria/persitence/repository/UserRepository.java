package com.platzi.pizzeria.persitence.repository;

import com.platzi.pizzeria.persitence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {
}
