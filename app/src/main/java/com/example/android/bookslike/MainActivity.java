package com.example.android.bookslike;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    //Variable for the BookAdapter of the list of the books
    private BookAdapter mAdapter;

    //Variable for the search field
    private EditText searchField;

    //Variable for the query given by the user
    private String mQuery;

    //Variable for the first line of the empty text
    private TextView empty1;

    //Variable for the second line of the empty text
    private TextView empty2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a new list for the books
        final List<Book> books = new ArrayList<Book>();

        //Create a BookAdapter for the book list
        mAdapter = new BookAdapter(this, books);

        //Get the ListView with the list ID and set the adapter to it
        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setAdapter(mAdapter);

        //Get the LinerLayout with the empty_view ID and set it as EmptyView
        LinearLayout mEmptyStateView = (LinearLayout) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateView);

        //Get the two TextViews in the EmptyView
        empty1 = (TextView) findViewById(R.id.empty_1);
        empty2 = (TextView) findViewById(R.id.empty_2);

        //Get the search field
        searchField = (EditText) findViewById(R.id.search_field);

        //Create a variable for the search button
        Button searchButton = (Button) findViewById(R.id.search_button);

        //Create a ConnectivityManager and get the NetworkInfo from it
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //Create a boolean variable for the connectivity status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        //If the device is connected to the network
        if (isConnected) {

            //Set an OnClickListener to the search button
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Initialize the Loader (execute the search)
                    getLoaderManager().initLoader(0, null, MainActivity.this);
                }
            });
        }

        //If the device is not connected to the network
        else {

            //Set the text of first line of he EmptyView and hide the second line (not needed)
            empty1.setText(R.string.no_internet);
            empty2.setVisibility(View.GONE);
        }

        //Set an OnClickListener on every item of the ListView
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Create a Uri with the link of the current Book Object
                Uri link = Uri.parse(books.get(position).getLink());

                //Create an Intent with the link
                Intent webIntent = new Intent(Intent.ACTION_VIEW, link);

                //If there is an App to handle the Intent, start it
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
            }
        });


    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        //Get the query given by the user
        mQuery = searchField.getText().toString();

        //Create a BookLoader with it and return it
        BookLoader loader = new BookLoader(this, mQuery);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();

        //If there is a List of Book Objects which is not empty, after the loading has finished, add the List to the adapter
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

        //If the List is empty, set the empty texts to EmptyView lines
        empty1.setText(R.string.no_item_found);
        empty2.setText(R.string.insert_other);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Get the query given by the user
        mQuery = searchField.getText().toString();
        outState.putString("query", mQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mQuery = savedInstanceState.getString("query");
        //Initialize the Loader (execute the search)
        getLoaderManager().initLoader(0, null, MainActivity.this);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
