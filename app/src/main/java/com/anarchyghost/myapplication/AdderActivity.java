package com.anarchyghost.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.anarchyghost.myapplication.db.DBHelper;

public class AdderActivity extends AppCompatActivity implements View.OnClickListener {
    Button addbtn;
    ImageButton todo;
    ImageButton lesson;
    Button txt;
    EditText et_name;
    EditText et_text;
    DBHelper dbHelper;
    String date;
    private int mType=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);


        et_name = (EditText) findViewById(R.id.edit_name);
        et_text = (EditText) findViewById(R.id.edit_text);

        todo = findViewById(R.id.button3);
        lesson = findViewById(R.id.button);

        todo.setOnClickListener(this);
        lesson.setOnClickListener(this);

        Bundle putExtra=getIntent().getExtras();
        date=putExtra.getString("date");


    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button3:
                Intent intent = new Intent(AdderActivity.this, AdderLessonActivity.class);
                intent.putExtra("date1",date);
                startActivity(intent);
                break;
            case R.id.button:
                Intent intent1 = new Intent(AdderActivity.this, AdderTodoActivity.class);
                intent1.putExtra("date1",date);
                startActivity(intent1);
                break;
        }
        finish();

    }
}
