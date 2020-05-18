package com.anarchyghost.myapplication;

import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.Calendar;

import static com.anarchyghost.myapplication.db.DBHelper.KEY_DATE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_ID;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_ISDONE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TEXT;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEBEG;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEEND;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TYPE;
import static com.anarchyghost.myapplication.db.DBHelper.TB_NAME;

public class EditorActivity extends AppCompatActivity {
DBHelper dbHelper;
EditText name;
TextView date;
TextView start;
TextView end;
TextView textView;
TextInputEditText title;
Button btn;
Parser parser=new Parser();
DatePickerDialog.OnDateSetListener et_date_set;
TimePickerDialog.OnTimeSetListener et_beg_set;
TimePickerDialog.OnTimeSetListener et_end_set;
int id;
int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Bundle info = getIntent().getExtras();
        id = info.getInt("id");
        Log.d("EA", "" + id);

        dbHelper = new DBHelper(this);

        name = findViewById(R.id.name_redo);
        date = findViewById(R.id.date_redo);
        start = findViewById(R.id.start_redo);
        end = findViewById(R.id.end_redo);
        title = findViewById(R.id.title_redo);
        btn = findViewById(R.id.button6);
        textView = findViewById(R.id.texts);

        final Notes note = getNote();
        type = note.getType();
        if (note.getType() == NotesAdapter.TYPE_LESSON) {
            end.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            title.setHint("Домашнее задание");
        }

        name.setText(note.getName());
        date.setText(parser.parseToDate(note.getDead()));
        start.setText(parser.parseToTime(note.getStart()));
        end.setText(parser.parseToTime(note.getEnd()));
        title.setText(note.getText());

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dat = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditorActivity.this, R.style.Theme_AppCompat_Dialog, et_date_set,
                        year, month, dat);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                int sec = calendar.get(Calendar.SECOND);

                TimePickerDialog dialog = new TimePickerDialog(EditorActivity.this, R.style.Theme_AppCompat_Dialog, et_beg_set,
                        hour, minute, true);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                int sec = calendar.get(Calendar.SECOND);

                TimePickerDialog dialog = new TimePickerDialog(EditorActivity.this, R.style.Theme_AppCompat_Dialog, et_beg_set,
                        hour, minute, true);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        et_beg_set = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String nhour = String.valueOf(hourOfDay);
                String nminute = String.valueOf(minute);
                if (hourOfDay < 10) nhour = "0" + nhour;
                if (minute < 10) nminute = "0" + nminute;
                String time = nhour + ":" + nminute;
                start.setText(time);
            }
        };
        et_beg_set = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String nhour = String.valueOf(hourOfDay);
                String nminute = String.valueOf(minute);
                if (hourOfDay < 10) nhour = "0" + nhour;
                if (minute < 10) nminute = "0" + nminute;
                String time = nhour + ":" + nminute;
                start.setText(time);
            }
        };

        et_end_set = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String nhour = String.valueOf(hourOfDay);
                String nminute = String.valueOf(minute);
                if (hourOfDay < 10) nhour = "0" + nhour;
                if (minute < 10) nminute = "0" + nminute;
                String time = nhour + ":" + nminute;
                end.setText(time);
            }
        };


        et_date_set = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String nday = String.valueOf(dayOfMonth);
                month++;
                String nmonth = String.valueOf(month);

                if (dayOfMonth < 10) nday = "0" + dayOfMonth;
                if (month < 10) nmonth = "0" + String.valueOf(month);
                String date1 = year + "-" + nmonth + "-" + nday;
                date.setText(date1);
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long d = 0;
                long s = 0;
                long e = 0;
                try {
                    d = parser.parseToMillDate(date.getText().toString());
                    s = parser.parseToMillTime(start.getText().toString());
                    e = parser.parseToMillTime(end.getText().toString());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                String n = name.getText().toString();
                String t = title.getText().toString();
                if (!TextUtils.isEmpty(n)) {
                    SQLiteDatabase database = dbHelper.getReadableDatabase();

                    database.delete(TB_NAME, KEY_ID + " = ?", new String[]{String.valueOf(id)});

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(KEY_ID, id);
                    contentValues.put(DBHelper.KEY_NAME, n);
                    contentValues.put(KEY_DATE, d);
                    contentValues.put(KEY_TIMEBEG, s);
                    contentValues.put(KEY_TIMEEND, e);
                    contentValues.put(KEY_TEXT, t);
                    contentValues.put(KEY_TYPE, type);
                    contentValues.put(KEY_ISDONE, note.getIsdone());
                    database.insert(DBHelper.TB_NAME, null, contentValues);

                    finish();
                }
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
