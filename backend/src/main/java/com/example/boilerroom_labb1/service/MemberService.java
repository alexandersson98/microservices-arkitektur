package com.example.boilerroom_labb1.service;

import com.example.boilerroom_labb1.dto.member.MemberRequestDTO;
import com.example.boilerroom_labb1.dto.member.MemberResponseDTO;
import com.example.boilerroom_labb1.entity.member.Member;
import com.example.boilerroom_labb1.entity.member.Role;
import com.example.boilerroom_labb1.mapper.MemberMapper;
import com.example.boilerroom_labb1.repository.MemberRepository;
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
