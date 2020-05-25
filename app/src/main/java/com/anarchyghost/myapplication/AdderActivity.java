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
    ImageButton todo;
    ImageButton lesson;
    DBHelper dbHelper;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);

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
