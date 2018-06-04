package com.aliosm.examsandquizzesreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBConnections extends SQLiteOpenHelper {
    public DBConnections(Context context) {
        super(context, "ExamsAndQuizzesReminder", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS exams_and_quizzes (id INTEGER PRIMARY KEY, type BOOLEAN, name TEXT, datetime DATETIME)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS exams_and_quizzes");
        onCreate(db);
    }

    public void addNewExamQuiz(boolean type, String name, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("type", type);
        contentValues.put("name", name);
        contentValues.put("datetime", date + " " + time);

        db.insert("exams_and_quizzes", null, contentValues);
    }

    public ArrayList getExamsAndQuizzes(int option) {
        ArrayList all = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if(option == 2) // ALL
            cursor = db.rawQuery("SELECT * FROM exams_and_quizzes ORDER BY datetime", null);
        else if(option == 1) // EXAMS
            cursor = db.rawQuery("SELECT * FROM exams_and_quizzes WHERE type = 0 ORDER BY datetime", null);
        else // QUIZZES
            cursor = db.rawQuery("SELECT * FROM exams_and_quizzes WHERE type = 1 ORDER BY datetime", null);

        cursor.moveToFirst();

        SimpleDateFormat simpleDateFormat;
        Date date = null;
        while(!cursor.isAfterLast()) {
            String type;
            if(cursor.getInt(cursor.getColumnIndex("type")) == 0)
                type = " Exam";
            else
                type = " Quiz";

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex("datetime")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            all.add(cursor.getString(cursor.getColumnIndex("name")) +
                    type + ", " +
                    DateUtils.getRelativeTimeSpanString(date.getTime(),
                            System.currentTimeMillis(),
                            0L,
                            DateUtils.FORMAT_ABBREV_ALL) +
                    "\nAt: " + cursor.getString(cursor.getColumnIndex("datetime")));

            cursor.moveToNext();
        }

        return all;
    }

    public ArrayList getIds(int option) {
        ArrayList all = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if(option == 2) // ALL
            cursor = db.rawQuery("SELECT * FROM exams_and_quizzes ORDER BY datetime", null);
        else if(option == 1) // EXAMS
            cursor = db.rawQuery("SELECT * FROM exams_and_quizzes WHERE type = 0 ORDER BY datetime", null);
        else // QUIZZES
            cursor = db.rawQuery("SELECT * FROM exams_and_quizzes WHERE type = 1 ORDER BY datetime", null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            all.add(cursor.getInt(cursor.getColumnIndex("id")));
            cursor.moveToNext();
        }

        return all;
    }

    public ArrayList getExamQuiz(String id) {
        ArrayList examQuiz = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM exams_and_quizzes WHERE id = " + id, null);
        cursor.moveToFirst();

        examQuiz.add(cursor.getString(cursor.getColumnIndex("type")));
        examQuiz.add(cursor.getString(cursor.getColumnIndex("name")));
        examQuiz.add(cursor.getString(cursor.getColumnIndex("datetime")));

        return examQuiz;
    }

    public void editExamQuiz(String id, boolean type, String name, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE exams_and_quizzes SET type = " + (type ? 1 : 0) + ", name = '" + name + "', datetime = '" + (date + " " + time) + "' WHERE id = " + id);
    }

    public void deleteExamQuiz(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM exams_and_quizzes WHERE id = " + id);
    }

    public void removeOldExamsAndQuizzes() {
        ArrayList ids = new ArrayList();
        ArrayList datetimes = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, datetime FROM exams_and_quizzes", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            ids.add(cursor.getString(cursor.getColumnIndex("id")));
            datetimes.add(cursor.getString(cursor.getColumnIndex("datetime")));
            cursor.moveToNext();
        }

        for(int i = 0; i < datetimes.size(); ++i)
            try {
                if(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datetimes.get(i).toString()).before(new Date()))
                    db.execSQL("DELETE FROM exams_and_quizzes WHERE id = " + ids.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }

    public String getLastId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM exams_and_quizzes", null);
        cursor.moveToLast();
        return cursor.getString(cursor.getColumnIndex("id"));
    }
}
