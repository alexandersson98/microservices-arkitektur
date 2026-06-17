package com.example.boilerroom_labb1.repository;

import com.example.boilerroom_labb1.entity.Loan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByBookId(Long bookId);
}
