package com.example.android.bookslike;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MátéZoltán on 2017. 05. 25..
 */

public class BookAdapter extends ArrayAdapter<Book> {

    /* Public constructor for the BookAdapter Object
     * @param context: the context of the BookAdapter
     * @param books: the list of the books
     */
    public BookAdapter(@NonNull Context context, @NonNull List<Book> books) {
        super(context, 0, books);
    }

    /*
     * Override of the getView method
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        //If there is not recyclable listItemView, create a new one
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //Get the current Book Object
        Book currentBook = getItem(position);

        //Get the TextView with the title_view ID and set the title of the current book as text
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_view);
        titleTextView.setText(currentBook.getTitle());

        //Get the TextView with the author_view ID and set the author of the current book as text
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_view);
        authorTextView.setText(currentBook.getAuthor());

        return listItemView;

    }
}
