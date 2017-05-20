package com.example.sutheres.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Sutheres on 12/15/2016.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    List<Book> mBooks;

    public  BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() { forceLoad();}

    @Override
    public List<Book> loadInBackground() {
        // DOnt perform the request if there are no URLs, or the first URL is null
        if(mUrl == null) {
            return  null;
        }

        // Perform the HTTP request for earthquake data and receive a response
        mBooks = QueryUtils.extractBooks(mUrl);
        return mBooks;
    }

    public List<Book> getBooks() {
        return mBooks;
    }
}
