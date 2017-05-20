package com.example.sutheres.booklisting;

/**
 * Created by Sutheres on 12/9/2016.
 */

public class Book {
    // Title of book
    private String mTitle;

    // Author of book
    private String mAuthor;


    // Default Constructor
    public Book(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

}
