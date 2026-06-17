package com.example.boilerroom_labb1.entity;

import com.example.boilerroom_labb1.entity.member.Member;
import jakarta.persistence.*;

import java.time.LocalDate;
    @Entity
    public class LoanHistory {

        @Id
        private Long id;

        @ManyToOne
        @JoinColumn(name = "book_id", nullable = false)
        private Book book;
        @ManyToOne
        @JoinColumn(name = "memberId")
        private Member member;


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

        public Member getMember() {
            return member;
        }

        public void setMember(Member member) {
            this.member = member;
        }
    }

