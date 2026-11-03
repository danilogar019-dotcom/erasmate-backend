package com.erasmate.erasmate_backend.repositories;

import com.erasmate.erasmate_backend.models.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
}
