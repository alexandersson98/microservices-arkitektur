package org.example.library_service.mapper;


import org.example.library_service.dto.loan.LoanResponseDTO;
import org.example.library_service.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponseDTO toResponseDto(Loan loan) {
        return new LoanResponseDTO(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                loan.getMemberId()
        );
    }
}
