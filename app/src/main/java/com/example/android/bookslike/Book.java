package com.example.android.bookslike;

/**
 * Created by MátéZoltán on 2017. 05. 25..
 */

public class Book {

    //Create a variable for the author of the book
    private String mAuthor;

    //Create a variable for the title of the book
    private String mTitle;

    //Create a variable for the info link of the book
    private String mLink;

    /* The public constructor of the Book Object
     * @param author: the author of the book
     * @param title: the title of the book
     * @param link: the info link of the book
     */
    public Book(String author, String title, String link) {
        mAuthor = author;
        mTitle = title;
        mLink = link;
    }

    //Returns the author of the book
    public String getAuthor() {
        return mAuthor;
    }

    //Returns the title of the book
    public String getTitle() {
        return mTitle;
    }

    //Returns the info link of the book
    public String getLink() {
        return mLink;
    }
}
