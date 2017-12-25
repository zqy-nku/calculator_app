package com.example.koala.assignmentcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    private TextView correctTxt;
    private TextView wrongTxt;
    private TextView giveupTxt;
    private TextView totaltimeTxt;
    private TextView gradeTxt;
    private TextView totalmarkTxt;
    private Button tryBtn;
    private ListView questionLvw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        correctTxt = (TextView) findViewById(R.id.correctTxt);
        wrongTxt = (TextView) findViewById(R.id.wrongTxt);
        giveupTxt = (TextView) findViewById(R.id.giveupTxt);
        totaltimeTxt = (TextView) findViewById(R.id.totaltimeTxt);
        gradeTxt = (TextView) findViewById(R.id.gradeTxt);
        totalmarkTxt = (TextView) findViewById(R.id.totalmarkTxt);
        questionLvw = (ListView) findViewById(R.id.questionLvw);
        tryBtn = (Button) findViewById(R.id.tryBtn);


        Intent intent = getIntent();
        int correct = intent.getIntExtra("correct", 10);
        int wrong = intent.getIntExtra("wrong", 10);
        int giveup = 10 - correct - wrong;
        int grade = intent.getIntExtra("grade", 10);
        long totaltime = intent.getLongExtra("timePeriod", 10);
        ArrayList<String> questionlist = intent.getStringArrayListExtra("questionlist");
        ArrayList<String> questionresult = intent.getStringArrayListExtra("questionresult");

        long avaragetime;
        if(giveup==10){
            avaragetime = 0;
        }else{
            avaragetime = totaltime / (10-giveup);
        }
        long minius = (avaragetime / 1000) / 60;
        long seconds = (avaragetime / 1000) % 60;
        if (minius > 0) {
            totaltimeTxt.setText(minius + ":" + seconds + "m");
        } else {
            totaltimeTxt.setText(minius + ":" + seconds + "s");
        }

        correctTxt.setText(correct + "");
        wrongTxt.setText(wrong + "");
        giveupTxt.setText(giveup + "");
        totalmarkTxt.setText(grade + "");

        if (grade >= 0 & grade <= 25) gradeTxt.setText("LV.1 Bronze tier!");
        if (grade > 25 & grade <= 50) gradeTxt.setText("LV.2 Silver tier!");
        if (grade > 50 & grade <= 75) gradeTxt.setText("LV.3 Gold tier!");
        if (grade > 75 & grade <= 100) gradeTxt.setText("LV.4 Platinum tier!");
        if (grade > 100 & grade <= 125) gradeTxt.setText("LV.5 Diamond tier!");
        if (grade > 125 & grade <= 150) gradeTxt.setText("LV.6 Challenger tier!");

        ArrayAdapter<String> ArrayList = new ArrayAdapter<String>(SummaryActivity.this, android.R.layout.simple_list_item_1);
        questionLvw.setAdapter(ArrayList);

        // add array
        if (questionresult != null && questionresult.size() > 0) {
            for (int i = 0; i < 10; i++) {
                ArrayList.add(String.valueOf(i + 1) + ". " + questionlist.get(i) + "     " + questionresult.get(i));
            }
        }
        tryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("back", "Student UID");
                setResult(3, i);
                finish();
            }
        });


    }

    public void test() {
    }
}
