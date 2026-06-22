package org.example.library_service.mapper;


import org.example.library_service.dto.loan.LoanHistoryResponseDTO;
import org.example.library_service.entity.Loan;
import org.example.library_service.entity.LoanHistory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LoanHistoryMapper {

    private final MemberMapper memberMapper;

    public LoanHistoryMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }


    public LoanHistory toEntity(Loan loan) {
        LoanHistory history = new LoanHistory();
        history.setId(loan.getId());
        history.setBook(loan.getBook());
        history.setLoanDate(loan.getLoanDate());
        history.setReturnDate(LocalDate.now());
        history.setMember(loan.getMember());
        return history;
    }
    public LoanHistoryResponseDTO toResponseDto(LoanHistory history) {
        return new LoanHistoryResponseDTO(
                history.getId(),
                history.getBook().getTitle(),
                history.getLoanDate(),
                history.getReturnDate(),
                "Book has been returned",
                memberMapper.toLoanResponse(history.getMember())
        );
    }
}
