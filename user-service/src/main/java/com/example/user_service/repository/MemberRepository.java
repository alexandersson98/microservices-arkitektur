package com.example.user_service.repository;

import com.example.user_service.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.email = :identifier or m.phone = :identifier")
    Optional<Member> findByEmailOrPhone(@Param("identifier")String identifier);

}
