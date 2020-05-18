package com.anarchyghost.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.anarchyghost.myapplication.db.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.anarchyghost.myapplication.db.DBHelper.TB_NAME;

public class TextNotesActivity extends AppCompatActivity {
    Button button;
    DBHelper dbHelper;
    TextAdapter notesAdapter;
    RecyclerView recyclerView;
    String chosendata;
    Parser parser;

    public int getYear() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy", Locale.getDefault());
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }

    public int getMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM", Locale.getDefault());
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }

    public int getDay() {
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
        setContentView(R.layout.activity_text_notes);


        parser = new Parser();

        notesAdapter = new TextAdapter();
        chosendata = parser.getDate();


        recyclerView = findViewById(R.id.showne);

        dbHelper = new DBHelper(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(notesAdapter);
        List<Notes> installedApps = null;
        try {
            installedApps = notesAdapter.getNotes(dbHelper);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notesAdapter.setApps(installedApps);
        notesAdapter.notifyDataSetChanged();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TextNotesActivity.this, AdderTextActivity.class);
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
    }

    private void reloadApps() throws ParseException {
        List<Notes> installedApps = notesAdapter.getNotes(dbHelper);

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
