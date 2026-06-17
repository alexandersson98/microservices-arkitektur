package com.example.boilerroom_labb1.mapper;


import com.example.boilerroom_labb1.dto.loan.LoanResponseDTO;
import com.example.boilerroom_labb1.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
    private final  MemberMapper memberMapper;

    public LoanMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public LoanResponseDTO toResponseDto(Loan loan) {
        return new LoanResponseDTO(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                memberMapper.toLoanResponse(loan.getMember()

        ));
    }
}