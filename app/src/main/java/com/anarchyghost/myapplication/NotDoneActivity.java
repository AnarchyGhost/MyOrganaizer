package com.anarchyghost.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Barrier;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.anarchyghost.myapplication.db.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.anarchyghost.myapplication.db.DBHelper.TB_NAME;

public class NotDoneActivity extends AppCompatActivity {
        Button button;
        DBHelper dbHelper;
        NotesAdapter notesAdapter;
        RecyclerView recyclerView;
        String chosendata;
        Parser parser;

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
            setContentView(R.layout.activity_not_done);

            //this.deleteDatabase(DB_NAME);

            parser=new Parser();
            notesAdapter = new NotesAdapter();
            chosendata=parser.getDate();

            recyclerView=findViewById(R.id.shown);

            dbHelper= new DBHelper(this);

            RecyclerView recyclerView = findViewById(R.id.shown);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(notesAdapter);
            List<Notes> installedApps = null;
            try {
                installedApps = notesAdapter.getNotes(dbHelper,parser.getDate(),false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            notesAdapter.setApps(installedApps);
            notesAdapter.notifyDataSetChanged();

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            recyclerView.addItemDecoration(itemTouchHelper);
            itemTouchHelper.attachToRecyclerView(recyclerView);
            try {
                reloadApps();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Button button10=findViewById(R.id.button10);
            button10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }



        private void reloadApps() throws ParseException {
            List<Notes> installedApps = notesAdapter.getNotes(dbHelper,chosendata,false);

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
