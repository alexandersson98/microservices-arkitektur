package org.example.library_service.repository;

import org.example.library_service.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;



public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByBookId(Long bookId);
}
