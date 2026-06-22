package com.example.user_service.service;

import com.example.user_service.dto.member.MemberRequestDTO;
import com.example.user_service.dto.member.MemberResponseDTO;
import com.example.user_service.entity.member.Member;
import com.example.user_service.entity.member.Role;
import com.example.user_service.mapper.MemberMapper;
import com.example.user_service.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    public MemberService(MemberMapper memberMapper, MemberRepository memberRepository) {
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    public MemberResponseDTO createMember(MemberRequestDTO memberRequestDto){
        Member member = memberMapper.toMemberEntity(memberRequestDto, Role.USER);
        memberRepository.save(member);
        return memberMapper.toResponse(member);
    }

    public MemberResponseDTO createAdmin(MemberRequestDTO memberRequestDto){
        Member member = memberMapper.toMemberEntity(memberRequestDto, Role.ADMIN);
        memberRepository.save(member);
        return memberMapper.toResponse(member);
    }

    public MemberResponseDTO createLibrarian(MemberRequestDTO memberRequestDto){
        Member member = memberMapper.toMemberEntity(memberRequestDto, Role.LIBRARIAN);
        memberRepository.save(member);
        return memberMapper.toResponse(member);
    }
}
