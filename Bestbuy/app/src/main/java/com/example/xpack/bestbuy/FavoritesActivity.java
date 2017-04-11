package com.example.xpack.bestbuy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.xpack.bestbuy.db.Favorite;


public class FavoritesActivity extends Fragment {
    private SQLiteDatabase db;

    ListView list;
    TextView title;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.activity_favorites, container, false);


        list = (ListView) v.findViewById(R.id.list);
        // title = (TextView) findViewById(R.id.title);

        db = new DBHelper(getActivity()).getDB();

        Cursor c = Favorite.list(db);

        // title.setText("" + c.getCount() + " favorit(s)");



        CursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.products, c,
                new String[]{"key", "added"},
                new int[]{android.R.id.text1, android.R.id.text2},
                0);

        list.setAdapter(adapter);

        return v;
    }





}