package com.aliosm.examsandquizzesreminder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EditExamQuiz extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    RadioButton examRb;
    RadioButton quizRb;
    TextView nameTv;
    EditText nameEt;
    TextView dateTv;
    EditText dateEt;
    TextView timeTv;
    EditText timeEt;
    Button editExamQuizBtn;
    Button deleteExamQuizBtn;

    Typeface font;

    int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exam_quiz);

        examRb = findViewById(R.id.examRb);
        quizRb = findViewById(R.id.quizRb);
        nameTv = findViewById(R.id.nameTv);
        nameEt = findViewById(R.id.nameEt);
        dateTv = findViewById(R.id.dateTv);
        dateEt = findViewById(R.id.dateEt);
        timeTv = findViewById(R.id.timeTv);
        timeEt = findViewById(R.id.timeEt);
        editExamQuizBtn = findViewById(R.id.editExamQuizBtn);
        deleteExamQuizBtn = findViewById(R.id.deleteExamQuizBtn);

        // Get the new font
        font = Typeface.createFromAsset(getAssets(), "fonts/fa.ttf");

        setIcon(getString(R.string.calendar), getString(R.string.pick_date), font, (Button)findViewById(R.id.pickDateBtn));
        setIcon(getString(R.string.clock), getString(R.string.pick_time), font, (Button)findViewById(R.id.pickTimeBtn));
        setIcon(getString(R.string.pencil), getString(R.string.edit_exam), font, editExamQuizBtn);
        setIcon(getString(R.string.trash), getString(R.string.delete_exam), font, deleteExamQuizBtn);

        DBConnections db = new DBConnections(this);
        ArrayList examQuiz = db.getExamQuiz(getIntent().getExtras().getString("id"));

        if(examQuiz.get(0).toString().equals("1")) {
            quizRb.setChecked(true);
            quizRbClick(quizRb);
        }

        nameEt.setText(examQuiz.get(1).toString());
        dateEt.setText(examQuiz.get(2).toString().substring(0, 10));
        timeEt.setText(examQuiz.get(2).toString().substring(11, 16));
    }

    public void examRbClick(View view) {
        nameTv.setText(R.string.exam_name);
        dateTv.setText(R.string.exam_date);
        timeTv.setText(R.string.exam_time);
        setIcon(getString(R.string.pencil), getString(R.string.edit_exam), font, editExamQuizBtn);
        setIcon(getString(R.string.trash), getString(R.string.delete_exam), font, deleteExamQuizBtn);
    }

    public void quizRbClick(View view) {
        nameTv.setText(R.string.quiz_name);
        dateTv.setText(R.string.quiz_date);
        timeTv.setText(R.string.quiz_time);
        setIcon(getString(R.string.pencil), getString(R.string.edit_quiz), font, editExamQuizBtn);
        setIcon(getString(R.string.trash), getString(R.string.delete_quiz), font, deleteExamQuizBtn);
    }

    public void pickDateBtnClick(View view) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                year, month, day);
        datePickerDialog.show();
    }

    public void pickTimeBtnClick(View view) {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void editExamQuizBtnClick(View view) {
        if(nameEt.getText().toString().trim().isEmpty()) {
            if(examRb.isChecked())
                Toast.makeText(this, "You should enter Exam name", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "You should enter Quiz name", Toast.LENGTH_LONG).show();
            return;
        }

        if(dateEt.getText().toString().equals(getString(R.string.yyyy_mm_dd))) {
            if(examRb.isChecked())
                Toast.makeText(this, "You should enter Exam date", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "You should enter Quiz date", Toast.LENGTH_LONG).show();
            return;
        }

        if(timeEt.getText().toString().equals(getString(R.string.hh_mm))) {
            if(examRb.isChecked())
                Toast.makeText(this, "You should enter Exam time", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "You should enter Quiz time", Toast.LENGTH_LONG).show();
            return;
        }

        boolean type = false;
        if(quizRb.isChecked())
            type = true;

        String name = nameEt.getText().toString().trim();
        String date = dateEt.getText().toString();
        String time = timeEt.getText().toString();

        DBConnections db = new DBConnections(this);
        db.editExamQuiz(getIntent().getExtras().getString("id"), type, name, date, time);

        if(!type)
            Toast.makeText(this, "Exam edited successfully", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Quiz edited successfully", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void deleteExamQuizBtnClick(View view) {
        String type = "Exam";
        if(quizRb.isChecked())
            type = "Quiz";

        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this " + type + "?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int idx) {
                        DBConnections db = new DBConnections(EditExamQuiz.this);
                        db.deleteExamQuiz(getIntent().getExtras().getString("id"));
                        if(examRb.isChecked())
                            Toast.makeText(EditExamQuiz.this, "Exam deleted successfully", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(EditExamQuiz.this, "Quiz deleted successfully", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(EditExamQuiz.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if(i < year || (i >= year && i1 < month) || (i >= year && i1 >= month && i2 < day)) {
            Toast.makeText(this, "Invalid Date", Toast.LENGTH_LONG).show();
            return;
        }

        ++i1;
        dateEt.setText(i + "-" + (i1 < 10 ? "0" + i1 : i1) + "-" + (i2 < 10 ? "0" + i2 : i2));
        timeEt.setText("HH:MM");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String date = dateEt.getText().toString();

        int picked_year = Integer.valueOf(date.substring(0, 4));
        int picked_month = Integer.valueOf(date.substring(5, 7));
        int picked_day = Integer.valueOf(date.substring(8, 10));

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        if(year == picked_year && month == picked_month && day == picked_day &&
                i < hour || (i >= hour && i1 < minute)) {
            Toast.makeText(this, "Invalid Time", Toast.LENGTH_LONG).show();
            return;
        }

        timeEt.setText((i < 10 ? "0" + i : i) + ":" + (i1 < 10 ? "0" + i1 : i1));
    }

    private void setIcon(String icon, String text, Typeface font, Button target) {
        // Create new span to merge the icon with the text
        Spannable span = new SpannableString(icon + "  " + text);

        // Set the custom font for the icon and a regular font for the text
        span.setSpan(new CustomTypefaceSpan("", font), 0, icon.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new TypefaceSpan("sans-serif-smallcaps"), icon.length(), icon.length() + 2 + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the button text
        target.setText(span);
    }
}
