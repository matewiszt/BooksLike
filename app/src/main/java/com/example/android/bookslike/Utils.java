package com.example.android.bookslike;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MátéZoltán on 2017. 05. 25..
 */

public class Utils {

    //Constant for the log messages
    private static final String LOG_TAG = MainActivity.class.getName();

    /*
     * Public constructor (not needed because we don't create instances of the Utils class)
     */
    public Utils() {
    }

    //Get the resources (is needed to be able to use the getString method)
    private static Resources res = Resources.getSystem();

    /*
     * Put all the methods in this class together to perform the fetch of the data
     * @param query: the search term inserted into the search field by the user
     * @return the list of the Book Objects or null
     */
    public static List<Book> fetchData(String query) {

        //Create a URL from the query
        URL url = createUrl(query);

        //Create a variable for the response
        String response = null;

        try {

            //Try to make an HTTP Request with the created URL
            response = makeHttpRequest(url);
        } catch (IOException e) {

            //If it fails, log it
            Log.e(LOG_TAG, res.getString(R.string.error_http), e);
        }

        //Extract the data needed from the response and create the List from the data
        List<Book> books = extractFromJson(response);

        return books;

    }

        /*
         * Create a URL from the the search term inserted into the search field by the user
         * @param query: the search term inserted into the search field by the user
         * @return the URL created or null
         */
    private static URL createUrl(String query) {

        //Create a variable for the URL
        URL url = null;

        try {

            //Put the search term given by the user into a GoogleBooks API-standarded request URL and create a URL from it
            url = new URL("https://www.googleapis.com/books/v1/volumes?q=intitle:" + query + "&maxResults=10");
        } catch (MalformedURLException e) {

            //If it fails, log it
            Log.e(LOG_TAG, res.getString(R.string.error_url), e);
        }
        return url;
    }

        /*
         * Create an HTTP Request and get the response from it
         * @param url: the URL created by the createURL method
         * @return the response of the request or null
         */
    private static String makeHttpRequest(URL url) throws IOException {

        //Create a variable for the response
        String jsonResponse = "";

        //If no valid URL provided, return early
        if (url == null) {
            return jsonResponse;
        }

        //Create a variable for the connection and the InputStream
        HttpURLConnection connection = null;
        InputStream stream = null;

        try {

            //Try to create a request with the created URL and the settings below and connect
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {

                //If the connection is successful, get the InputStream and the response from the InputStream
                stream = connection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else {

                //If the connection is unsuccessful, log it
                Log.e(LOG_TAG, res.getString(R.string.error_response) + " " + connection.getResponseCode());
            }
        } catch (IOException e) {

            //If it fails, log it
            Log.e(LOG_TAG, res.getString(R.string.error_reading), e);
        } finally {

            //If the connection is on yet and the InputStream is open yet, disconnect and close the InputStream
            if (connection != null) {
                connection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }
        }

        return jsonResponse;
    }

        /*
         * Helper method to read the response data from the InputStream
         * @param stream: the InputStream given back by the server
         * @return the response of the request or null
         */
    private static String readFromStream(InputStream stream) throws IOException {

        //Create a new StringBuilder for the response
        StringBuilder builder = new StringBuilder();

        //If the InputStream is not null, create an InputStreamReader and a BufferedReader to read the data from the InputStream
        if (stream != null) {

            InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            //Read the data line by line and add it to the StringBuilder
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }

        //Return the response in String
        return builder.toString();
    }

        /*
         * Extract the data from the JSON response and create a List of Book Objects from it
         * @param responseJson: the JSON response read from the InputStream
         * @return the List of the Book Objects created from the JSON response
         */
    private static List<Book> extractFromJson(String responseJson) {

        //If the response is empty, return early
        if (TextUtils.isEmpty(responseJson)) {
            return null;
        }

        //Create a List for the Book Objects
        List<Book> books = new ArrayList<Book>();

        try {

            //Try to get the main JSONObject (the collection of the queried books) from the response and get the items of it (the unique books)
            JSONObject volumes = new JSONObject(responseJson);
            JSONArray items = volumes.getJSONArray("items");

            //Loop through all the books, get the title, author and info link of all the books in the JSON response
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject volume = item.getJSONObject("volumeInfo");
                String title = volume.getString("title");
                String authors = volume.getJSONArray("authors").get(0).toString();
                String link = volume.getString("infoLink");

                //Create a new Book Object with the data of a given book
                Book book = new Book(authors, title, link);

                //Add the book to the List of Book Objects
                books.add(book);
            }


        } catch (JSONException e) {

            //If it fails, log it
            Log.e(LOG_TAG, "Error extracting the data from the JSON response", e);
        }

        return books;

    }

}
