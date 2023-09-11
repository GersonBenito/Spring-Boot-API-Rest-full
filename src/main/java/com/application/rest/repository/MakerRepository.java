package com.application.rest.repository;

import com.application.rest.entities.Maker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository // para que sea administrado por Spring Boot
public interface MakerRepository extends CrudRepository<Maker, Long> {
}
