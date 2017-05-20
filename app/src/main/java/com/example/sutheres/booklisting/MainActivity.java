package com.example.sutheres.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int BOOK_LOADER_ID = 1;
    private BookAdapter mAdapter;
    /**
     * URL to Query Google books API
     **/
    private static final String GOOGLE_BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView bookListView = (ListView) findViewById(R.id.list);

        bookListView.setEmptyView(findViewById(R.id.empty));

        // create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        /**
         * set the adapter on the {@link ListView}
         * so the list can be populated in the user interface.
         */
        bookListView.setAdapter(mAdapter);


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(BOOK_LOADER_ID, null, this).forceLoad();
        } else {
            ProgressBar loading = (ProgressBar) findViewById(R.id.loading_spinner);
            loading.setVisibility(View.GONE);
            TextView emptyView = (TextView) findViewById(R.id.empty);
            emptyView.setText("No Internet Connection");
        }

    }



    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle args) {

        EditText searchField = (EditText) findViewById(R.id.search_field);
         String searchText = searchField.getText().toString();
        if (searchField.getText().toString().isEmpty()) {
            searchText = "Android";
        }
        Uri baseUri = Uri.parse(GOOGLE_BOOKS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchText);
        uriBuilder.appendQueryParameter("maxResults", "14");
        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // clear the adapter of previous earthquake data
        mAdapter.clear();
        TextView emptyView = (TextView) findViewById(R.id.empty);
        emptyView.setText("No books found");

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);


        // If there is a valid list of {@link Book}s, then add them to the
        // adapters data set. This will trigger the ListView to update
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {mAdapter.clear();}

    public void searchBooks (View view) {
        /**
         * Restart the Loader with all the same data & parameters. Upon the app first loading, the list will be empty because no text has
         * been taken from the search field to be included in the Google Books query string.
         */
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, this);
    }


}
