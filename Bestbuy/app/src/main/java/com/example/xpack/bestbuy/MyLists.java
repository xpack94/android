package com.example.xpack.bestbuy;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpack.bestbuy.db.Favorite;

/**
 * Created by xpack on 12/04/17.
 */

public class MyLists extends Fragment  {

    View v;
    FloatingActionButton add;
    LayoutInflater inflater;
    LinearLayout inlay;
    RelativeLayout parent;
    TextView t;
    static Cursor cur=null;
    public static String name="default";
    private static SQLiteDatabase db;
    String text;
    customAdapter adapter;
    ListView list;
    RelativeLayout relative;
    String id="0";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.my_lists, container, false);

        db = new DBHelper(getActivity()).getDB();
        Cursor c = Favorite.Mylist(db);
        adapter=new customAdapter(getActivity(),c);
        LinearLayout defaultList= (LinearLayout) v.findViewById(R.id.defaultlist);

        list = (ListView) v.findViewById(R.id.myLists);

        inlay=(LinearLayout) v.findViewById(R.id.addNewWishList);
        add=(FloatingActionButton) v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(),CreatingNewList.class);
                i.putExtra("text","");
                startActivityForResult(i, 1);

              //  onActivityResult(1, RESULT_OK, i);
            }
        });


        //afficher tous les produits qui son dans la list par defaut
        defaultList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),WishList.class);
                name="default";
                startActivity(intent);
            }
        });

        list.setAdapter(adapter);

        //afficher tous les produit qui son dans la list qui a eté cliqué
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   LinearLayout linear= (LinearLayout) adapterView.getChildAt(i);
                    RelativeLayout r =(RelativeLayout) linear.getChildAt(0);
                    TextView t= (TextView) r.getChildAt(0);
                    name=String.valueOf(t.getText());
                    Intent intent=new Intent(getActivity(),WishList.class);
                    startActivity(intent);
            }
        });

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful

            if (resultCode == Activity.RESULT_OK) {

                String pressed=data.getExtras().getString("added");
                text=data.getExtras().getString("text");

                if(pressed.equals("add")){
                    inflater=getActivity().getLayoutInflater();
                    View f= inflater.inflate(R.layout.wish_list_line,null);
                    t=(TextView) f.findViewById(R.id.wishListName);

                    if (Favorite.existsList(db,text)) {
                        Favorite.removeList(db, text);

                    } else {
                        Favorite.addList(db, text);
                    }
                    list.setAdapter(adapter);
                    getActivity().recreate();

                }

            }
        }else if(requestCode==2){

            if (resultCode == Activity.RESULT_OK) {
                String pressed=data.getExtras().getString("added");
                String text1=data.getExtras().getString("text");
                boolean b=!Favorite.existsList(db,text1);
                if(pressed.equals("add")){
                    if(!text1.equals("") && b){
                        ContentValues cv = new ContentValues();
                        cv.put("nameOfList",text1);
                        // ((TextView) relative.getChildAt(0)).setText(text1);

                        db.update("lists", cv, "nameOfList"+ "= ?", new String[] { String.valueOf(((TextView) relative.getChildAt(0)).getText())});
                        getActivity().recreate();
                    }else if(!b){
                        Toast.makeText(getActivity(),text1+" "+getResources().getString(R.string.alreadyExists),Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }






    public class customAdapter extends CursorAdapter {
        private Cursor mCursor;
        private Context mContext;
        private final LayoutInflater mInflater;


        public customAdapter(Context context, Cursor c) {
            super(context, c);
            mInflater=LayoutInflater.from(context);
            mContext=context;
            cur=c;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView name=(TextView) view.findViewById(R.id.wishListName);
            name.setText(cursor.getString(cursor.getColumnIndex("nameOfList")));
            final int rowId=cursor.getInt(cursor.getColumnIndex("_id"));
            ImageView delete=(ImageView) view.findViewById(R.id.delete);
            final ImageView edit=(ImageView) view.findViewById(R.id.edit);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout parent=(RelativeLayout) view.getParent();
                    TextView n=(TextView) parent.getChildAt(0);

                    Favorite.removeList(db,String.valueOf(n.getText()));
                    getActivity().recreate();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(getActivity(),CreatingNewList.class);
                    relative= (RelativeLayout) edit.getParent();
                    TextView te=(TextView) relative.getChildAt(0);
                    intent.putExtra("text", String.valueOf(te.getText()));
                    startActivityForResult(intent,2);
                    id=""+rowId;


                }
            });


        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final View view=mInflater.inflate(R.layout.wish_list_line,parent,false);
            return view;
        }

    }




}
