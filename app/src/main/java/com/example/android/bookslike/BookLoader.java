package com.example.android.bookslike;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by MátéZoltán on 2017. 05. 25..
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    //Create a variable for the query of the user
    private String mQuery;

    /*
     * Public constructor of the BookLoader Object
     * @param context: the context of the Loader
     * @param query: the query of the user
     */
    public BookLoader(Context context, String query) {
        super(context);
        mQuery = query;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        //If there is no query yet, return early
        if (mQuery == null) {
            return null;
        }

        //If there is a query, fetch the data and create a list of books from it
        List<Book> books = Utils.fetchData(mQuery);

        return books;
    }
}
