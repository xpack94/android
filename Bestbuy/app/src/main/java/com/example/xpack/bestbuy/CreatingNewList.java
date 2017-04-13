package com.example.xpack.bestbuy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by xpack on 12/04/17.
 */
public class CreatingNewList  extends Activity implements View.OnClickListener{

    ImageView cancel,confirm;
    EditText t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.new_list);
        Intent intent=getIntent();
        String text=intent.getExtras().getString("text");
        confirm=(ImageView) findViewById(R.id.confirm);
        cancel=(ImageView) findViewById(R.id.cancel);
        t=(EditText) findViewById(R.id.listName);
        t.setText(text);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {


        if(view.getId()==R.id.cancel){
            Intent intent = new Intent(CreatingNewList.this,MyLists.class);
            intent.putExtra("added","cancel");
            intent.putExtra("text",t.getText() );
            setResult(Activity.RESULT_OK, intent);
            finish();

        }else{
            Intent intent = new Intent(CreatingNewList.this,MyLists.class);
            intent.putExtra("added", "add");
            intent.putExtra("text",String.valueOf(t.getText()));
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
    }
}
