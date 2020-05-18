package com.anarchyghost.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.anarchyghost.myapplication.db.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.anarchyghost.myapplication.db.DBHelper.KEY_DATE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_ID;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_ISDONE;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TEXT;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEBEG;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TIMEEND;
import static com.anarchyghost.myapplication.db.DBHelper.KEY_TYPE;
import static com.anarchyghost.myapplication.db.DBHelper.TB_NAME;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    public static final int TYPE_LESSON=0;
    public static final int TYPE_TODO=2;
    public static final int TYPE_TEXT=1;
    Parser parser=new Parser();




    private List<Notes> notes = new ArrayList<>();

    public void setApps(List<Notes> notes) {
        this.notes=notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view;

        switch (viewType) {
            case TYPE_LESSON:
                view=layoutInflater.inflate(R.layout.view_lesson_layout, parent, false);
                break;
            case TYPE_TODO:
                view=layoutInflater.inflate(R.layout.view_todo_layout,parent,false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notes NoteInfo = notes.get(position);
        holder.itemView.setTag(NoteInfo);
        switch (NoteInfo.getType()){
            case TYPE_LESSON:
                holder.nameTv.setText(NoteInfo.getName());
                holder.begTv.setText(parser.parseToTime(NoteInfo.getStart()));
                holder.enTv.setText(parser.parseToTime(NoteInfo.getEnd()));
                break;
            case TYPE_TODO:
                holder.nameTv.setText(NoteInfo.getName());
                holder.versionTv.setText(NoteInfo.getText());
                holder.deadTv.setText(parser.parseToTime(NoteInfo.getStart()));
                break;
            case TYPE_TEXT:
                holder.nameTv.setText(NoteInfo.getName());
                holder.versionTv.setText(NoteInfo.getText());
                break;
        }
        if(NoteInfo.getIsdone()==0) holder.linearLayout.setBackgroundColor(Color.WHITE);
        else holder.linearLayout.setBackgroundColor(Color.GREEN);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTv;
        private final TextView versionTv;
        private final TextView begTv;
        private final TextView enTv;
        private final TextView deadTv;
        private final LinearLayout linearLayout;


        public ViewHolder(final View itemView) {
            super(itemView);
            final Context context = itemView.getContext();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            nameTv = itemView.findViewById(R.id.name_tv);
            versionTv = itemView.findViewById(R.id.version_tv);
            begTv = itemView.findViewById(R.id.start_tv);
            enTv = itemView.findViewById(R.id.end_tv);
            deadTv=itemView.findViewById(R.id.dead_tv);
            linearLayout=itemView.findViewById(R.id.liner);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Notes note=(Notes) v.getTag();
                    if(note.getIsdone()==1){
                        linearLayout.setBackgroundColor(Color.WHITE);}else{
                        linearLayout.setBackgroundColor(Color.GREEN);
                    }
                    note.setIsdone();
                    DBHelper dbHelper=new DBHelper(context);
                    SQLiteDatabase database=dbHelper.getWritableDatabase();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(KEY_ID,note.getId());
                    contentValues.put(DBHelper.KEY_NAME, note.getName());
                    contentValues.put(KEY_DATE, note.getDead());
                    contentValues.put(KEY_TIMEBEG,note.getStart());
                    contentValues.put(KEY_TIMEEND,note.getEnd());
                    contentValues.put(KEY_TEXT,note.getText());
                    contentValues.put(KEY_TYPE,note.getType());
                    contentValues.put(KEY_ISDONE,note.getIsdone());
                    database.update(TB_NAME,contentValues,KEY_ID+" = ?",  new String[] { String.valueOf(note.getId())});
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notes note=(Notes) view.getTag();
                        Intent intent=new Intent(context,EditorActivity.class);
                        intent.putExtra("id",note.getId());
                        context.startActivity(intent);

                    }
                });
        }

    }



    public List<Notes> getNotes(DBHelper dbHelper,String data,boolean done) throws ParseException {
        List<Notes> notes=new ArrayList<>();
        String data1=data;
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        Cursor cursor;
        if(done) {
            cursor = database.query(DBHelper.TB_NAME, null, KEY_DATE +
                    " = " + parser.parseToMillDate(data1) + " AND (" + KEY_TYPE + " = " + TYPE_LESSON + " OR " + KEY_TYPE + " = " + TYPE_TODO + ")", null, null, null, KEY_TIMEBEG, null);
        } else{
            cursor = database.query(DBHelper.TB_NAME, null, KEY_DATE +
                    " < " + parser.parseToMillDate(data1) + " AND (" + "("+KEY_TYPE + " = " + TYPE_LESSON + " AND "+KEY_TEXT+" > "+"''"+") OR " + KEY_TYPE + " = " + TYPE_TODO + ")"+" AND "+KEY_ISDONE+" = 0", null, null, null, KEY_DATE+","+KEY_TIMEBEG, null);
        }


        if(cursor.moveToFirst()){
            int idIndex=cursor.getColumnIndex(DBHelper.KEY_ID);
            int TypeIndex=cursor.getColumnIndex(DBHelper.KEY_TYPE);
            int NameIndex=cursor.getColumnIndex(DBHelper.KEY_NAME);
            int textIndex=cursor.getColumnIndex(DBHelper.KEY_TEXT);
            int textStart=cursor.getColumnIndex(KEY_TIMEBEG);
            int textEnd=cursor.getColumnIndex(DBHelper.KEY_TIMEEND);
            int textDead=cursor.getColumnIndex(DBHelper.KEY_DATE);
            int textIsdone=cursor.getColumnIndex(DBHelper.KEY_ISDONE);
            do{
                Notes notes1=new Notes(cursor.getInt(idIndex),cursor.getString(NameIndex),cursor.getString((textIndex)), cursor.getInt(TypeIndex),
                        cursor.getLong(textStart), cursor.getLong(textEnd), cursor.getLong(textDead),cursor.getInt(textIsdone));

                notes.add(notes1);
            }while (cursor.moveToNext());
        }else{
            Log.d("mLog","0 rows");
        }
        return notes;
    }

    @Override
    public int getItemViewType(int position) {
        int type= notes.get(position).getType();
        switch (type) {
            case 0:
                return TYPE_LESSON;
            case 2:
                return TYPE_TODO;
        }
        return TYPE_TEXT;
    }
    
}

