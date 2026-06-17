package com.example.boilerroom_labb1.service;

import com.example.boilerroom_labb1.dto.auth.LoginRequestDTO;
import com.example.boilerroom_labb1.dto.auth.LoginResponseDTO;
import com.example.boilerroom_labb1.entity.member.Member;
import com.example.boilerroom_labb1.entity.member.Role;
import com.example.boilerroom_labb1.repository.MemberRepository;
import com.example.boilerroom_labb1.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;




    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }


    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(), loginRequest.password()));

        Member member = memberRepository.findByEmailOrPhone(loginRequest.username())
                .orElseThrow();

        return new LoginResponseDTO(jwtUtil.generateToken(loginRequest.username()), member.getRole());

    }
}
