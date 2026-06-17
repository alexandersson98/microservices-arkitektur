package com.example.boilerroom_labb1.repository;

import com.example.boilerroom_labb1.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long > {

}
