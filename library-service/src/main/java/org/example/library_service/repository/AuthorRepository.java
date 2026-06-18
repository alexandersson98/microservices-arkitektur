package org.example.library_service.repository;

import org.example.library_service.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long > {

}
