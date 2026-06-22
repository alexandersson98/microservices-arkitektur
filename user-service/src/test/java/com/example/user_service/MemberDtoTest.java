package com.example.user_service;

import com.example.user_service.dto.member.MemberRequestDTO;
import com.example.user_service.dto.member.MemberResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberDtoTest {

    @Test
    void memberRequestDTOShouldStoreValues() {
        MemberRequestDTO dto = new MemberRequestDTO(
                "Test Name",
                "0711112222",
                "19980101-1234",
                "test@test.com",
                "password123"
        );

        assertEquals("Test Name", dto.name());
        assertEquals("0711112222", dto.phone());
        assertEquals("19980101-1234", dto.personId());
        assertEquals("test@test.com", dto.email());
        assertEquals("password123", dto.password());
    }

    @Test
    void memberResponseDTOShouldStoreValues() {
        MemberResponseDTO dto = new MemberResponseDTO(
                "Test Name",
                "0711112222",
                "19980101-1234",
                "test@test.com"
        );

        assertEquals("Test Name", dto.name());
        assertEquals("0711112222", dto.phone());
        assertEquals("19980101-1234", dto.personId());
        assertEquals("test@test.com", dto.email());
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
}