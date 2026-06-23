package org.example.library_service.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
    @Entity
    public class LoanHistory {

        @Id
        private Long id;

        @ManyToOne
        @JoinColumn(name = "book_id", nullable = false)
        private Book book;

        private Long memberId;
        private String memberName;


        private LocalDate loanDate;
        private LocalDate returnDate;

        public LoanHistory() {
        }

        public Long getId() {
            return id;
        }

        public Book getBook() {
            return book;
        }

        public LocalDate getLoanDate() {
            return loanDate;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setBook(Book book) {
            this.book = book;
        }

        public void setLoanDate(LocalDate loanDate) {
            this.loanDate = loanDate;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }
    }

