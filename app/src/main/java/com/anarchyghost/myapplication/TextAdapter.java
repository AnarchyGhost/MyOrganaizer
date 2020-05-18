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

import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEBEG;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TYPE;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    public static final int TYPE_LESSON = 0;
    public static final int TYPE_TODO = 2;
    public static final int TYPE_TEXT = 1;
    Parser parser = new Parser();


    private List<Notes> notes = new ArrayList<>();

    public void setApps(List<Notes> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;
        view = layoutInflater.inflate(R.layout.view_text_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notes NoteInfo = notes.get(position);
        holder.itemView.setTag(NoteInfo);
        holder.versionTv.setText(NoteInfo.getText());
        holder.begTv.setText(parser.parseToDateTime(NoteInfo.getStart()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView versionTv;
        private final TextView begTv;


        public ViewHolder(final View itemView) {
            super(itemView);
            final Context context = itemView.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            versionTv = itemView.findViewById(R.id.version_tv);
            begTv = itemView.findViewById(R.id.start_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notes note = (Notes) view.getTag();
                    Intent intent = new Intent(context, TextEditorActivity.class);
                    intent.putExtra("id", note.getId());
                    context.startActivity(intent);

                }
            });
        }

    }


    public List<Notes> getNotes(DBHelper dbHelper) throws ParseException {
        List<Notes> notes = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TB_NAME, null,KEY_TYPE+" = "+TYPE_TEXT , null, null, null, KEY_TIMEBEG, null);


        if (cursor.moveToFirst()) {
            int idINdex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int TypeINdex = cursor.getColumnIndex(DBHelper.KEY_TYPE);
            int NameINdex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int textINdex = cursor.getColumnIndex(DBHelper.KEY_TEXT);
            int textStart = cursor.getColumnIndex(KEY_TIMEBEG);
            int textEnd = cursor.getColumnIndex(DBHelper.KEY_TIMEEND);
            int textDead = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int textIsdone=cursor.getColumnIndex(DBHelper.KEY_ISDONE);
            do {
                Notes notes1 = new Notes(cursor.getInt(idINdex), cursor.getString(NameINdex), cursor.getString((textINdex)), cursor.getInt(TypeINdex),
                        cursor.getLong(textStart), cursor.getLong(textEnd), cursor.getLong(textDead),cursor.getInt(textIsdone));

                notes.add(notes1);
            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
        }
        return notes;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
}
