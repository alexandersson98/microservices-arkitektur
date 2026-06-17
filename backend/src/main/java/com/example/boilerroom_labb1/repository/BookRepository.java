package com.example.boilerroom_labb1.repository;

import com.example.boilerroom_labb1.entity.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAuthorId(Long authorId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book>findByIdWithLock(Long id);

}
