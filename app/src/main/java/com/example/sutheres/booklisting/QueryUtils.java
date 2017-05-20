package com.example.sutheres.booklisting;

/**
 * Created by Sutheres on 12/15/2016.
 */

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

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
 * helper methods related to requesting and receiving data from Google Books API
 */

public class QueryUtils {

    private  static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }

    public static List<Book> extractBooks(String stringURL) {
        // Create URL object
        URL url = createURL(stringURL);

        // Perform HTTP Request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {

        }


        // Extract relevant fields from JSON response and create an ArrayList of {@link Book}s
        List<Book> books = extractFeaturesFromJSON(jsonResponse);

        return books;
    }

    /**
     * Returns new URL object from the given URL
     */
    private static URL createURL(String stringURL) {

        Uri baseUri = Uri.parse(stringURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("maxResults", "14");

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
            return null;
        }

        return url;

    }

    /**
     * Make an HTTP Request with the given URL and receive a string response
     */
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If URL is null return an empty JSON response
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error: " + urlConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Problem receiving JSON result", exception);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a string which contains the whole
     * JSON response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an Arraylist of {@link Book}s by parsing out information
     * about the books from the input bookJSON string.
     */
    private static List<Book> extractFeaturesFromJSON(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();

        // Try to parse the books API JSON response. if there's a problem with the way the json
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Parse the response given by the books API response string and
            // build up a list of book objects with the corresponding data
            JSONObject rootJSONResponse = new JSONObject(bookJSON);

            // Get the instance of JSONArray that contains the books (items)
            JSONArray bookArray = rootJSONResponse.getJSONArray("items");

            // Loop through each book in the array
            for (int i = 0; i < bookArray.length(); i++) {
                // access current book
                JSONObject rootJSONObject = bookArray.getJSONObject(i);

                // access volume info which contains author and title
                JSONObject volumeInfo = rootJSONObject.getJSONObject("volumeInfo");

                // access author
                String author = volumeInfo.getString("authors");

                // access title
                String title = volumeInfo.getString("title");

                // Create Book object from author and title above and add it to the list of Books
                Book book = new Book(title, author);
                books.add(book);
            }


        } catch (JSONException exception) {

            // If an error is thrown when executing any of the above statements in the try block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing JSON results: ", exception);
        }

        // return list of books
        return books;
    }

}
