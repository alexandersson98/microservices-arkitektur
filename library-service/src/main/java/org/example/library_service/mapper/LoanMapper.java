package org.example.library_service.mapper;


import org.example.library_service.dto.loan.LoanResponseDTO;
import org.example.library_service.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
    private final MemberMapper memberMapper;

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