package com.anarchyghost.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.anarchyghost.myapplication.db.DBHelper;

import java.text.ParseException;
import java.util.Calendar;

import static com.anarchyghost.myapplication.db.DBHelper.KEY_ID;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEBEG;

public class AdderBirthdayActivity extends AppCompatActivity {

    Button addbtn;
    TextView et_name;
    TextView et_date;
    DBHelper dbHelper;
    int type;
    int id;
    DatePickerDialog.OnDateSetListener et_date_set;
    Parser parser=new Parser();
    Birthday birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder_birthday);

        Bundle info=getIntent().getExtras();
        type=info.getInt("type");
        id=info.getInt("id");

        if(type==1) birthday=getBD();

        et_name=(EditText)findViewById(R.id.edit_name);
        if(type==1) et_name.setText(birthday.getName());
        et_date=findViewById(R.id.date);
        if(type==1)et_date.setText(parser.parseToMonth(birthday.getDate()));
        else et_date.setText(parser.getDateMonth());

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int dat=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AdderBirthdayActivity.this,R.style.Theme_AppCompat_Dialog , et_date_set,
                        year,month,dat);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        et_date_set=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String nday=String.valueOf(dayOfMonth);
                month++;
                String nmonth= String.valueOf(month);

                if(dayOfMonth<10)nday="0"+dayOfMonth;
                if(month<10)nmonth="0"+String.valueOf(month);
                String date=nmonth+"-"+nday;
                et_date.setText(date);
            }
        };



        addbtn = findViewById(R.id.button5);
        dbHelper=new DBHelper(this);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();

                SQLiteDatabase database = dbHelper.getWritableDatabase();
                if(!TextUtils.isEmpty(name)) {

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBHelper.KEY_NAME, name);
                    try {
                        contentValues.put(DBHelper.KEY_DATE, parser.parseToMillMonth(et_date.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(type==1)database.delete(DBHelper.TB_BD_NAME,KEY_ID + " = ?", new String[]{String.valueOf(id)});
                    database.insert(DBHelper.TB_BD_NAME, null, contentValues);

                    finish();
                }
            }
        });
    }
    Birthday getBD(){
       Birthday bd=null;
        dbHelper=new DBHelper(this);
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        Cursor cursor=database.query(DBHelper.TB_BD_NAME,null, KEY_ID +
                " = "+ id,null,null,null,null,null);


        if(cursor.moveToFirst()){
            int idINdex=cursor.getColumnIndex(KEY_ID);
            int NameINdex=cursor.getColumnIndex(DBHelper.KEY_NAME);
            int textDead=cursor.getColumnIndex(DBHelper.KEY_DATE);
            do{
                bd=new Birthday(cursor.getInt(idINdex),cursor.getString(NameINdex), cursor.getLong(textDead));

            }while (cursor.moveToNext());
        }else{
            Log.d("mLog","0 rows");
        }
        return bd;
    }
}

