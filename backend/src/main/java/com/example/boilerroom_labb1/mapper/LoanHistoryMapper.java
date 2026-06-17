package com.example.boilerroom_labb1.mapper;

import com.example.boilerroom_labb1.dto.loan.LoanHistoryResponseDTO;
import com.example.boilerroom_labb1.entity.Loan;
import com.example.boilerroom_labb1.entity.LoanHistory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LoanHistoryMapper {

    private final  MemberMapper memberMapper;

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
