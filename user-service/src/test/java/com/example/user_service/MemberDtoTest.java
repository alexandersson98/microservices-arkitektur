package com.example.user_service;

import com.example.user_service.dto.member.MemberLoanResponseDTO;
import com.example.user_service.dto.member.MemberRequestDTO;
import com.example.user_service.dto.member.MemberResponseDTO;
import com.example.user_service.entity.member.Member;
import com.example.user_service.entity.member.Role;
import com.example.user_service.mapper.MemberMapper;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class MemberDtoTest {

    private final MemberMapper memberMapper = new MemberMapper(new BCryptPasswordEncoder());

    @Test
    void memberRequestDTOShouldStoreValues() {
        MemberRequestDTO dto = new MemberRequestDTO(
                "Test Name", "0711112222", "19980101-1234", "test@test.com", "password123");
        assertEquals("Test Name", dto.name());
        assertEquals("0711112222", dto.phone());
        assertEquals("19980101-1234", dto.personId());
        assertEquals("test@test.com", dto.email());
        assertEquals("password123", dto.password());
    }

    @Test
    void memberResponseDTOShouldStoreValues() {
        MemberResponseDTO dto = new MemberResponseDTO(
                "Test Name", "0711112222", "19980101-1234", "test@test.com");
        assertEquals("Test Name", dto.name());
        assertEquals("0711112222", dto.phone());
        assertEquals("19980101-1234", dto.personId());
        assertEquals("test@test.com", dto.email());
    }

    @Test
    void memberLoanResponseDTOShouldStoreValues() {
        MemberLoanResponseDTO dto = new MemberLoanResponseDTO("Test Name", 1L);
        assertEquals("Test Name", dto.name());
        assertEquals(1L, dto.id());
    }

    @Test
    void memberLoanResponseDTOEqualityShouldWork() {
        MemberLoanResponseDTO dto1 = new MemberLoanResponseDTO("Test", 1L);
        MemberLoanResponseDTO dto2 = new MemberLoanResponseDTO("Test", 1L);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void memberRequestDTOEqualityShouldWork() {
        MemberRequestDTO dto1 = new MemberRequestDTO(
                "Test", "0711112222", "19980101-1234", "test@test.com", "password123");
        MemberRequestDTO dto2 = new MemberRequestDTO(
                "Test", "0711112222", "19980101-1234", "test@test.com", "password123");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void memberResponseDTOEqualityShouldWork() {
        MemberResponseDTO dto1 = new MemberResponseDTO(
                "Test", "0711112222", "19980101-1234", "test@test.com");
        MemberResponseDTO dto2 = new MemberResponseDTO(
                "Test", "0711112222", "19980101-1234", "test@test.com");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void mapperToResponseShouldMapCorrectly() {
        Member member = new Member();
        member.setName("Test Name");
        member.setPhone("0711112222");
        member.setPersonId("19980101-1234");
        member.setEmail("test@test.com");

        MemberResponseDTO response = memberMapper.toResponse(member);

        assertEquals("Test Name", response.name());
        assertEquals("0711112222", response.phone());
        assertEquals("19980101-1234", response.personId());
        assertEquals("test@test.com", response.email());
    }

    @Test
    void mapperToLoanResponseShouldMapCorrectly() {
        Member member = new Member();
        member.setName("Loan User");

        MemberLoanResponseDTO response = memberMapper.toLoanResponse(member);

        assertEquals("Loan User", response.name());
    }

    @Test
    void mapperToMemberEntityShouldMapCorrectly() {
        MemberRequestDTO dto = new MemberRequestDTO(
                "Test Name", "0711112222", "19980101-1234", "test@test.com", "password123");

        Member member = memberMapper.toMemberEntity(dto, Role.USER);

        assertEquals("Test Name", member.getName());
        assertEquals("0711112222", member.getPhone());
        assertEquals("19980101-1234", member.getPersonId());
        assertEquals("test@test.com", member.getEmail());
        assertEquals(Role.USER, member.getRole());
        assertNotNull(member.getPassword());
    }

    @Test
    void mapperToMemberEntityWithAdminRoleShouldWork() {
        MemberRequestDTO dto = new MemberRequestDTO(
                "Admin", "0711112222", "19980101-1234", "admin@test.com", "password123");

        Member member = memberMapper.toMemberEntity(dto, Role.ADMIN);

        assertEquals(Role.ADMIN, member.getRole());
    }

    @Test
    void mapperToMemberEntityWithLibrarianRoleShouldWork() {
        MemberRequestDTO dto = new MemberRequestDTO(
                "Librarian", "0711112222", "19980101-1234", "lib@test.com", "password123");

        Member member = memberMapper.toMemberEntity(dto, Role.LIBRARIAN);

        assertEquals(Role.LIBRARIAN, member.getRole());
    }
}