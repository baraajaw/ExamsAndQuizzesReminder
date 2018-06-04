package com.aliosm.examsandquizzesreminder;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

    public class MainActivity extends AppCompatActivity {
    ArrayAdapter arrayAdapter;
    ArrayList ids;

    TextView mainTitleTv;
    ListView examsAndQuizzesLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainTitleTv = findViewById(R.id.mainTitleTv);
        examsAndQuizzesLv = findViewById(R.id.examsAndQuizzesLv);
        examsAndQuizzesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {
                Intent i = new Intent(MainActivity.this, EditExamQuiz.class);
                    i.putExtra("id", ids.get(idx).toString());
                startActivity(i);
            }
        });

        // Get the new font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fa.ttf");


        // Get button icon and button text
        String icon = getString(R.string.plus);
        String text = getString(R.string.add_new_exam_quiz);



        // Create new span to merge the icon with the text
        Spannable span = new SpannableString(icon + "  " + text);

        // Set the custom font for the icon and a regular font for the text
        span.setSpan(new CustomTypefaceSpan("", font), 0, icon.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new TypefaceSpan("sans-serif-smallcaps"), icon.length(), icon.length() + 2 + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the button text
        ((Button) findViewById(R.id.addNewExamQuizBtn)).setText(span);

        DBConnections db = new DBConnections(this);

        db.removeOldExamsAndQuizzes();

        ArrayList all = db.getExamsAndQuizzes(2);
        ids = db.getIds(2);

        // Show all Exams and Quizzes on the list view
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all);
        examsAndQuizzesLv.setAdapter(arrayAdapter);
    }

    public void addNewExamQuizBtnClick(View view) {
        Intent i = new Intent(this, AddNewExamQuiz.class);
        startActivity(i);
    }


    public void allRbClick(View view) {
        mainTitleTv.setText(R.string.your_exams_quizzes);

        // Get all Exams and Quizzes
        DBConnections db = new DBConnections(this);
        ArrayList all = db.getExamsAndQuizzes(2);
        ids = db.getIds(2);

        // Show all Exams and Quizzes on the list view
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all);
        examsAndQuizzesLv.setAdapter(arrayAdapter);
    }

    public void examsRbClick(View view) {
        mainTitleTv.setText(R.string.your_exams);

        // Get all Exams
        DBConnections db = new DBConnections(this);
        ArrayList all = db.getExamsAndQuizzes(1);
        ids = db.getIds(1);

        // Show all Exams on the list view
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all);
        examsAndQuizzesLv.setAdapter(arrayAdapter);
    }

    public void quizzesRbClick(View view) {
        mainTitleTv.setText(R.string.your_quizzes);

        // Get all Quizzes
        DBConnections db = new DBConnections(this);
        ArrayList all = db.getExamsAndQuizzes(0);
        ids = db.getIds(0);

        // Show all Quizzes on the list view
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all);
        examsAndQuizzesLv.setAdapter(arrayAdapter);
    }
}
