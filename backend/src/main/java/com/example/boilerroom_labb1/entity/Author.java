package com.example.boilerroom_labb1.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();


    public Author(){}
    public Author(String name){this.name = name;}


    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
