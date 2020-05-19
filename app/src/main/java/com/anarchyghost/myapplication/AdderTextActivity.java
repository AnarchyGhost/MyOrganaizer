package com.anarchyghost.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anarchyghost.myapplication.db.DBHelper;

import java.text.ParseException;

public class AdderTextActivity extends AppCompatActivity {
    Button addbtn;
    EditText et_name;
    EditText et_text;
    DBHelper dbHelper;
    Parser parser=new Parser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder_text);

        et_text=(EditText)findViewById(R.id.edit_text) ;

        dbHelper=new DBHelper(this);

        addbtn = findViewById(R.id.button5);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_text.getText().toString().trim();
                if(!TextUtils.isEmpty(text)) {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBHelper.KEY_TEXT, text);
                    contentValues.put(DBHelper.KEY_TYPE, 1);
                    try {
                        contentValues.put(DBHelper.KEY_TIMEBEG, parser.parseToMillDateTime(parser.getCurDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    database.insert(DBHelper.TB_NAME, null, contentValues);

                    finish();
                }else{
                    et_text.setHint("Введите текст");
                }
            }
        });
    }
}
