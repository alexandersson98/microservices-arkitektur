package org.example.library_service.service;

import jakarta.transaction.Transactional;
import org.example.library_service.dto.loan.LoanHistoryResponseDTO;
import org.example.library_service.dto.loan.LoanRequestDTO;
import org.example.library_service.dto.loan.LoanResponseDTO;
import org.example.library_service.entity.Book;
import org.example.library_service.entity.Loan;
import org.example.library_service.entity.LoanHistory;
import org.example.library_service.exceptions.BookAlreadyLoanedException;
import org.example.library_service.exceptions.NotFoundException;
import org.example.library_service.exceptions.NotFoundWithIdException;
import org.example.library_service.exceptions.ValidationException;
import org.example.library_service.mapper.LoanHistoryMapper;
import org.example.library_service.mapper.LoanMapper;
import org.example.library_service.repository.BookRepository;
import org.example.library_service.repository.LoanHistoryRepository;
import org.example.library_service.repository.LoanRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;
    private final LoanHistoryRepository loanHistory;
    private final LoanHistoryMapper loanHistoryMapper;
    private final MemberRepository memberRepository;



    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       LoanMapper loanMapper, LoanHistoryRepository loanHistory, LoanHistoryMapper loanHistoryMapper, MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.loanMapper = loanMapper;
        this.loanHistory = loanHistory;
        this.loanHistoryMapper = loanHistoryMapper;
        this.memberRepository = memberRepository;
    }
    @Caching(evict = {
            @CacheEvict(value = "loan", allEntries = true),
            @CacheEvict(value = "book", allEntries = true)
    })
    @Transactional
    public LoanResponseDTO createLoan(LoanRequestDTO request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member =  memberRepository.findByEmailOrPhone(username).orElseThrow();
        Book book = bookRepository.findByIdWithLock(request.bookId())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        if (loanRepository.existsByBookId(book.getId())) {
            throw new BookAlreadyLoanedException("Book already loaned");
        }

        try {

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setMember(member);

        Loan savedLoan = loanRepository.saveAndFlush(loan);

        return loanMapper.toResponseDto(savedLoan);
    } catch (DataIntegrityViolationException e){
        throw new BookAlreadyLoanedException("Book already loaned");}
    }

    @Cacheable("loan")
    public Page<LoanResponseDTO> getAllActiveLoans(Pageable pageable){
           return loanRepository.findAll(pageable)
                .map(loanMapper::toResponseDto);

    }

    @Caching(evict = {
            @CacheEvict(value = "loan", allEntries = true),
            @CacheEvict(value = "book", allEntries = true),
            @CacheEvict(value = "loanHistory", allEntries = true),
    })
    @Transactional
    public LoanHistoryResponseDTO returnBook(Long id){
        if(loanHistory.existsById(id)){
            throw new ValidationException("Book already returned");
        }
        Loan loan = loanRepository.findById(id).orElseThrow(()-> new NotFoundWithIdException("Loan not found with id: ", id));
        LoanHistory response = loanHistory.save(loanHistoryMapper.toEntity(loan));
        loanRepository.deleteById(id);
         return loanHistoryMapper.toResponseDto(response);

    }


    @Cacheable("loanHistory")
    public Page<LoanHistoryResponseDTO>getLoanHistoryList(Pageable pageable){
        return loanHistory.findAll(pageable)
                .map(loanHistoryMapper:: toResponseDto);
    }
}
