package com.example.user_service.security;

import com.example.user_service.entity.member.Member;
import com.example.user_service.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService{
    private final MemberRepository memberRepository;



    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmailOrPhone(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return new CustomUserDetails(member);
    }
}
