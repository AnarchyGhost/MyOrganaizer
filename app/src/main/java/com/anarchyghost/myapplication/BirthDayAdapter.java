package com.anarchyghost.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anarchyghost.myapplication.db.DBHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.anarchyghost.myapplication.db.DBHelper.KEY_DATE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEBEG;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TYPE;

public class BirthDayAdapter extends RecyclerView.Adapter<BirthDayAdapter.ViewHolder>{
    private List<Birthday> birthdays = new ArrayList<>();
    Parser parser=new Parser();

    public void setApps(List<Birthday> birthdays) {
        this.birthdays = birthdays;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;
        view = layoutInflater.inflate(R.layout.view_birthday_layout, parent, false);
        BirthDayAdapter.ViewHolder viewHolder = new BirthDayAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Birthday BirthdayInfo = birthdays.get(position);
        holder.itemView.setTag(BirthdayInfo);
        holder.nameTv.setText(BirthdayInfo.getName());
        holder.begTv.setText(parser.parseToMonth(BirthdayInfo.getDate()));
    }

    @Override
    public int getItemCount() {
        return birthdays.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTv;
        private final TextView begTv;


        public ViewHolder(final View itemView) {
            super(itemView);
            final Context context = itemView.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            nameTv = itemView.findViewById(R.id.name_tv);
            begTv = itemView.findViewById(R.id.dead_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Birthday bd = (Birthday) view.getTag();
                    Intent intent = new Intent(context, AdderBirthdayActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("id", bd.getId());
                    context.startActivity(intent);

                }
            });
        }

    }


    public List<Birthday> getBirthdays(DBHelper dbHelper) throws ParseException {
        List<Birthday> birthdays = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TB_BD_NAME, null,KEY_DATE+" >= "+parser.parseToMillMonth(parser.getDateMonth()) , null, null, null, KEY_DATE, null);
        if (cursor.moveToFirst()) {
            int idINdex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int NameINdex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int textDead = cursor.getColumnIndex(DBHelper.KEY_DATE);
            do {
                Birthday birthday = new Birthday(cursor.getInt(idINdex), cursor.getString(NameINdex) , cursor.getLong(textDead));

                birthdays.add(birthday);
            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
        }
        cursor = database.query(DBHelper.TB_BD_NAME, null,KEY_DATE+" < "+parser.parseToMillMonth(parser.getDateMonth()) , null, null, null, KEY_DATE, null);
        if (cursor.moveToFirst()) {
            int idINdex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int NameINdex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int textDead = cursor.getColumnIndex(DBHelper.KEY_DATE);
            do {
                Birthday birthday = new Birthday(cursor.getInt(idINdex), cursor.getString(NameINdex) , cursor.getLong(textDead));

                birthdays.add(birthday);
            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
        }
        return birthdays;
    }
}
