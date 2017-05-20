package com.example.sutheres.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sutheres on 12/10/2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a ListView.
        // Because this is a custom adapter, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, books);
    }


    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView in the list_item.xml file with the ID title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);

        // Get the current Title from the Book object and set it on this TextView
        titleTextView.setText(currentBook.getTitle());

        // Find the TextView in the list_item.xml file with the ID author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);

        // Get the current author from the Book object and set it on this TextView
        authorTextView.setText("by: " + currentBook.getAuthor());

        // Return the whole list item layout (Containing 2 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }

}
