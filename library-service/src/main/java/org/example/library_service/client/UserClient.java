package org.example.library_service.client;

import org.example.library_service.dto.member.MemberLoanResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/v1/member/{id}")
    MemberLoanResponseDTO getMember(@PathVariable Long id);
}
