package org.example.library_service.repository;

import org.example.library_service.entity.LoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
}
