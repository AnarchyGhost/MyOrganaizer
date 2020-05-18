package com.anarchyghost.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.anarchyghost.myapplication.db.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.anarchyghost.myapplication.db.DBHelper.TB_BD_NAME;
import static com.anarchyghost.myapplication.db.DBHelper.TB_NAME;

public class BirthdayActivity extends AppCompatActivity {

    Button button;
    DBHelper dbHelper;
    BirthDayAdapter birthdayAdapter;
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
        setContentView(R.layout.activity_birthday);


        parser = new Parser();

        birthdayAdapter = new BirthDayAdapter();
        chosendata = parser.getDate();


        recyclerView = findViewById(R.id.showned);

        dbHelper = new DBHelper(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(birthdayAdapter);
        List<Birthday> installedApps = null;
        try {
            installedApps = birthdayAdapter.getBirthdays(dbHelper);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        birthdayAdapter.setApps(installedApps);
        birthdayAdapter.notifyDataSetChanged();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BirthdayActivity.this, AdderBirthdayActivity.class);
                intent.putExtra("id",0);
                intent.putExtra("type",0);
                startActivity(intent);
                try {
                    reloadApps();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
        button=findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void reloadApps() throws ParseException {
        List<Birthday> installedApps = birthdayAdapter.getBirthdays(dbHelper);

        birthdayAdapter.setApps(installedApps);
        birthdayAdapter.notifyDataSetChanged();
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
            Birthday info = (Birthday) viewHolder.itemView.getTag();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TB_BD_NAME, "_id=" + info.getId(), null);
            try {
                reloadApps();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
}
