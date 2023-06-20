package org.edupoll.repository;

import java.util.Optional;

import org.edupoll.entity.ValidCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidCodeRepository extends JpaRepository<ValidCode, Long>{
	Optional<ValidCode> findByEmail(String email);
}
