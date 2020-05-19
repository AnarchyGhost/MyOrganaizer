package com.anarchyghost.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.anarchyghost.myapplication.db.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.anarchyghost.myapplication.db.DBHelper.DB_NAME;
import static com.anarchyghost.myapplication.db.DBHelper.TB_NAME;

public class MainActivity extends AppCompatActivity {
Button button;
DBHelper dbHelper;
NotesAdapter notesAdapter;
RecyclerView recyclerView;
String chosendata;
Parser parser;
    boolean bound = false;
    public int getYear(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy", Locale.getDefault());
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }
    public int getMonth(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM", Locale.getDefault());
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }
    public int getDay(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd", Locale.getDefault());
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }
    @Override
    protected void onResume() {
        try {
            reloadApps();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServiceConnection sConn;
        Intent intent;
        intent = new Intent(MainActivity.this,NotificationService.class);
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("LOG_TAG", "MainActivity onServiceConnected");
            bound = true;
        }

            public void onServiceDisconnected(ComponentName name) {
                Log.d("LOG_TAG", "MainActivity onServiceDisconnected");
                bound = false;
            }
        };
        startService(intent);
        parser=new Parser();
        notesAdapter = new NotesAdapter();
        chosendata=parser.getDate();

        recyclerView=findViewById(R.id.shown);

        dbHelper= new DBHelper(this);

       // SQLiteDatabase database=dbHelper.getWritableDatabase();
       this.deleteDatabase(DB_NAME);

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.shown);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(notesAdapter);
        List<Notes> installedApps = null;
        try {
            installedApps = notesAdapter.getNotes(dbHelper,parser.getDate(),true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notesAdapter.setApps(installedApps);
        notesAdapter.notifyDataSetChanged();

        DatePickerTimeline datePickerTimeline = findViewById(R.id.datePickerTimeline);

        datePickerTimeline.setInitialDate(getYear(),getMonth()-1,getDay()-4);

        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                String nday,nmonth;
                month++;
                if(day<10) nday="0"+day;
                else nday=String.valueOf(day);
                if(month<10) nmonth="0"+month;
                else nmonth=String.valueOf(month);
                chosendata=year+"-"+nmonth+"-"+nday;
                try {
                    reloadApps();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
                String nday,nmonth;
                month++;
                if(day<10) nday="0"+day;
                else nday=String.valueOf(day);
                if(month<10) nmonth="0"+month;
                else nmonth=String.valueOf(month);
                chosendata=year+"-"+nmonth+"-"+nday;
                try {
                    reloadApps();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        Date[] dates = {Calendar.getInstance().getTime()};
        datePickerTimeline.deactivateDates(dates);


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdderActivity.class);
                intent.putExtra("date",chosendata);
                startActivity(intent);
                try {
                    reloadApps();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });




       button=findViewById(R.id.button7);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent=new Intent(MainActivity.this,TextNotesActivity.class);
                  startActivity(intent);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        recyclerView.addItemDecoration(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        try {
            reloadApps();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,NotDoneActivity.class);
            startActivity(intent);
            return true;
        } else if(id== R.id.action_birthdays){
            Intent intent=new Intent(this,BirthdayActivity.class);
            startActivity(intent);
        } else{
            Intent intent=new Intent(this,raspisanieActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private void reloadApps() throws ParseException {
        List<Notes> installedApps = notesAdapter.getNotes(dbHelper,chosendata,true);

        notesAdapter.setApps(installedApps);
        notesAdapter.notifyDataSetChanged();
    }
    private final ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.END);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Notes info = (Notes) viewHolder.itemView.getTag();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TB_NAME, "_id=" + info.getId(), null);
            try {
                reloadApps();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

}
