package com.anarchyghost.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anarchyghost.myapplication.db.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import static com.anarchyghost.myapplication.TextAdapter.TYPE_LESSON;
import static com.anarchyghost.myapplication.TextAdapter.TYPE_TEXT;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_DATE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_ID;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_ISDONE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TEXT;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEBEG;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEEND;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TYPE;
import static com.anarchyghost.myapplication.db.DBHelper.TB_NAME;

public class TextEditorActivity extends AppCompatActivity {
    DBHelper dbHelper;
EditText editText;
int id;
Button btn;
Parser parser=new Parser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);


        Bundle info=getIntent().getExtras();
        id=info.getInt("id");

        dbHelper=new DBHelper(this);
        editText=findViewById(R.id.title_redo);
        btn=findViewById(R.id.button8);
        final Notes note=getNote();
        editText.setText(note.getText());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t=editText.getText().toString();
                SQLiteDatabase database=dbHelper.getReadableDatabase();
                database.delete(TB_NAME,KEY_ID+" = ?",new String[]{String.valueOf(id)});

                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_TYPE, TYPE_TEXT);
                contentValues.put(KEY_ID,id);
                contentValues.put(KEY_ISDONE,note.getIsdone());
                try {
                    contentValues.put(KEY_TIMEBEG,parser.parseToMillDateTime(parser.getCurDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                contentValues.put(KEY_TEXT,t);

                database.insert(DBHelper.TB_NAME, null, contentValues);

                finish();
            }
        });
    }

    Notes getNote(){
        Notes note=null;

        SQLiteDatabase database=dbHelper.getReadableDatabase();
        Cursor cursor=database.query(DBHelper.TB_NAME,null, KEY_ID +
                " = "+ id,null,null,null,KEY_TIMEBEG,null);


        if(cursor.moveToFirst()){
            int idINdex=cursor.getColumnIndex(KEY_ID);
            int TypeINdex=cursor.getColumnIndex(DBHelper.KEY_TYPE);
            int NameINdex=cursor.getColumnIndex(DBHelper.KEY_NAME);
            int textINdex=cursor.getColumnIndex(DBHelper.KEY_TEXT);
            int textStart=cursor.getColumnIndex(KEY_TIMEBEG);
            int textEnd=cursor.getColumnIndex(DBHelper.KEY_TIMEEND);
            int textDead=cursor.getColumnIndex(DBHelper.KEY_DATE);
            int textIsdone=cursor.getColumnIndex(DBHelper.KEY_ISDONE);
            do{
                note=new Notes(cursor.getInt(idINdex),cursor.getString(NameINdex),cursor.getString((textINdex)), cursor.getInt(TypeINdex),
                        cursor.getLong(textStart), cursor.getLong(textEnd), cursor.getLong(textDead),cursor.getInt(textIsdone));

            }while (cursor.moveToNext());
        }else{
            Log.d("mLog","0 rows");
        }
        return note;
    }
}
