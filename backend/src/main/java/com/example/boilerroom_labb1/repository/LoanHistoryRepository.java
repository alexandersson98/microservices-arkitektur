package com.example.boilerroom_labb1.repository;

import com.example.boilerroom_labb1.entity.LoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanHistoryRepository extends JpaRepository<LoanHistory, Long> {
}
