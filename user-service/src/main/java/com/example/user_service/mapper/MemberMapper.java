package com.example.user_service.mapper;


import com.example.user_service.dto.member.MemberLoanResponseDTO;
import com.example.user_service.dto.member.MemberRequestDTO;
import com.example.user_service.dto.member.MemberResponseDTO;
import com.example.user_service.entity.member.Member;
import com.example.user_service.entity.member.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    private final PasswordEncoder passwordEncoder;

    public MemberMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponseDTO toResponse(Member member) {
        return new MemberResponseDTO(member.getName(), member.getPhone(), member.getPersonId(), member.getEmail());
    }

    public Member toMemberEntity(MemberRequestDTO request, Role role) {
        Member member = new Member();
        member.setName(request.name());
        member.setPhone(request.phone());
        member.setPersonId(request.personId());
        member.setEmail(request.email());
        member.setPassword(passwordEncoder.encode(request.password()));
        member.setRole(role);
        return member;
    }

    public MemberLoanResponseDTO toLoanResponse(Member member){
        return  new MemberLoanResponseDTO(member.getName(), member.getId());

    }
}
