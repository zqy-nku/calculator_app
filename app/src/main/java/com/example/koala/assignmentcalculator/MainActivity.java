package com.example.koala.assignmentcalculator;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    private TextView questionTxt;
    private TextView questionnumTxt;
    private TextView answerTxt;
    private EditText rootTxt1;
    private EditText rootTxt2;
    private Button submitBtn;
    private Button nextBtn;

    private long startTime;
    private long endTime;
    private long timePeriod;
    private long period;
    private int state;
    private Handler handler = new Handler();

    private int count;
    private int mode;
    private int[] paras;

    private int correct;
    private int wrong;
    private int grade;
    ArrayList<String> questionlist = new ArrayList<String>();
    ArrayList<String> questionresult = new ArrayList<String>();

    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;

    private Generator generator;

    //Generate newQuestion
    private int[] newQuestion(int mode) {
        if (mode == 0) {
            int[] paras = generator.generateFirst();
            String question;
            if (paras[0] == 1) {
                question = "x";
            }
            if (paras[0] == -1) {
                question = "-x";
            } else {
                question = paras[0] + "x";
            }
            if (paras[1] < 0) question += paras[1];  //B<0
            else question += "+" + paras[1];
            question += "=" + 0;
            questionTxt.setText(question);
            questionlist.add(question);

            return paras;
        }
        else {
            int[] paras = generator.generateSecond();
            String question;
            if (paras[0] == 1) {
                question = "x²";
            }
            if (paras[0] == -1) {
                question = "-x²";
            }
            else {
                question = paras[0] + "x²";
            }
            if(paras[1]==1) {
                question += "x";
            }
            if(paras[1]==-1){
                question += "-x";
            }
            if (paras[1] < 0) {
                question += paras[1] + "x";      //B<0 and B!=-1
            }
            if(paras[1]>0 ) {
                question += "+" + paras[1] + "x"; //B>0 and B!=1
            }
            if (paras[2] < 0) question += paras[2];
            else question += "+" + paras[2];
            question += "=" + 0;
            questionTxt.setText(question);
            questionlist.add(question);

            return paras;
        }
    }

    //Solve question
    private void solve(int mode, int[] paras, double[] input) {
        if (mode == 0) {
            double res;
            try {
                res = generator.solveFirst(paras);
            } catch (Generator.WrongException e) {
                e.printStackTrace();
                answerTxt.setText("Wrong Input!");
                return;
            }
            if (abs(res - input[0]) < 1e-6) {
                correct++;
                grade += 10;
                questionresult.add(count - 1, "✔");
                mediaPlayer1.start();
                Toast.makeText(MainActivity.this, " ☺ +10 !", Toast.LENGTH_LONG).show();
                answerTxt.setText("Correct! " + "x=" + res);
            } else {
                wrong++;
                questionresult.add(count - 1, "✘");
                mediaPlayer2.start();
                Toast.makeText(MainActivity.this, " ☹ Fighting!", Toast.LENGTH_LONG).show();
                answerTxt.setText("Wrong! " + "x=" + res);
            }
        }
        else {
            double[] res;
            try {
                res = generator.solveSecond(paras);
            } catch (Generator.WrongException e) {
                e.printStackTrace();
                answerTxt.setText("Wrong Input!");
                return;
            }

            if (abs(Math.max(res[0], res[1]) - Math.max(input[0], input[1])) < 1e-6 && abs(Math.min(res[0], res[1]) - Math.min(input[0], input[1])) < 1e-6) {
                correct++;
                grade += 20;
                questionresult.add(count - 1, "✔");
                mediaPlayer1.start();
                Toast.makeText(MainActivity.this, "☺ +20 !", Toast.LENGTH_LONG).show();
                answerTxt.setText("Correct! " + "x₁=" + res[0] + ", x₂=" + res[1]);


            } else {
                wrong++;
                questionresult.add(count - 1, "✘");
                mediaPlayer2.start();
                Toast.makeText(MainActivity.this, "☹ Fighting!", Toast.LENGTH_LONG).show();
                answerTxt.setText("Wrong! " + "x₁=" + res[0] + ", x₂=" + res[1]);
            }
        }
    }

    private void submit() {

        rootTxt1.setEnabled(false);
        rootTxt2.setEnabled(false);
        submitBtn.setEnabled(false);
        nextBtn.setEnabled(true);

        handler.removeCallbacks(updateTimer);
        endTime = System.currentTimeMillis();  /*obtain end time for each question*/

        state = 1;     //press submit button state = 1, else state = 0.

        double[] input = new double[2];
        if (mode == 0) {
            try {
                input[0] = Double.parseDouble(rootTxt1.getText().toString());
            } catch (NumberFormatException e) {
                answerTxt.setText("Wrong format");
                submitBtn.setEnabled(false);
            }
        }
        else {
            try {
                input[0] = Double.parseDouble(rootTxt1.getText().toString());
                input[1] = Double.parseDouble(rootTxt2.getText().toString());
            } catch (NumberFormatException e) {
                answerTxt.setText("Wrong format");
            }
        }
        solve(mode, paras, input);
    }

    private void update() {

        questionTxt.setText("");
        answerTxt.setText("");
        rootTxt1.setText("");
        rootTxt2.setText("");

        rootTxt1.setEnabled(true);
        rootTxt2.setEnabled(false);

        submitBtn.setEnabled(true);
        nextBtn.setEnabled(true);

        if (count > 4) mode = 1;

        paras = newQuestion(mode);

        if (count > 4) {
            double[] res;

            try {
                res = generator.solveSecond(paras);
                if (res[0] == res[1]) {
                    rootTxt2.setEnabled(false);
                    answerTxt.setText("Only one root!");
                } else {
                    rootTxt2.setEnabled(true);
                    answerTxt.setText("");
                }
            } catch (Generator.WrongException e) {
                e.printStackTrace();
                return;
            }
        }

        if (count == 9) {
            nextBtn.setText("Summary ☞");
        }

        if (count == 10) {
            Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("correct", correct);       //correct number;
            intent.putExtra("wrong", wrong);           //wrong number;
            intent.putExtra("timePeriod", timePeriod); //total timePeriod;
            intent.putExtra("grade", grade);           //grade = mark;
            intent.putExtra("questionlist", questionlist); //questions list array;
            intent.putExtra("questionresult", questionresult); //quetions results array;
            intent.putExtra("go2", "Student UID");        //in order to activity transfer.
            startActivityForResult(intent, 2);
        }

        if(count ==10) {
            submitBtn.setEnabled(false);
            nextBtn.setText("TRY AGAIN");
        }

        count++;
        questionnumTxt.setText("Question" + " " + count);

        if(count !=1){
            if(state == 1){
                period = endTime-startTime;

            }
            else{
                period = 0; /*if give up, period ==0. or period = System.currentTimeMillis()-startTime??*/
            }

        }
        System.out.println(count+" "+period/1000);

        timePeriod+=period;

        startTime = System.currentTimeMillis();  /*obtain start time*/
        handler.postDelayed(updateTimer, 1000);  /*display start time*/

        state=0;       //click next button, state =0;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generator = new Generator();
        count = 0;
        mode = 0;
        state = 0;

        for (int i = 0; i < 11; i++) {
            questionresult.add(i, "Give up");     //questionresult array add values;
        }

        questionTxt = (TextView) findViewById(R.id.questionTxt);
        questionnumTxt = (TextView) findViewById(R.id.questionnumTxt);
        answerTxt = (TextView) findViewById(R.id.answerTxt);
        rootTxt1 = (EditText) findViewById(R.id.rootTxt1);
        rootTxt2 = (EditText) findViewById(R.id.rootTxt2);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        questionnumTxt = (TextView) findViewById(R.id.questionnumTxt);

        mediaPlayer1 = MediaPlayer.create(this, R.raw.correct);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.wrong);

        rootTxt1.addTextChangedListener(textWatcher1);
        rootTxt2.addTextChangedListener(textWatcher2);

        if (getIntent().getStringExtra("go1") != null) {
            String text = getIntent().getStringExtra("go1");
            questionnumTxt.setText(text);
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count > 10) {
                    MainActivity.this.finish();

                } else {
                    update();
                }


            }
        });

        if (count <= 10) update();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 3 && data != null) {
            data.getStringExtra("back");
            setResult(4, data);
            finish();
        }
    }

    private TextWatcher textWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    rootTxt1.setText(s);
                    rootTxt1.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                rootTxt1.setText(s);
                rootTxt1.setSelection(2);
            }

           if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    rootTxt1.setText(s.subSequence(0, 1));
                    rootTxt1.setSelection(1);
                    return;
                }
            }
            if (rootTxt1.getText().length() > 0) {
                try {
                    submitBtn.setEnabled(true);
                    Double.parseDouble(rootTxt1.getText().toString());
                } catch (NumberFormatException e) {
                    submitBtn.setEnabled(false);
                    if (!rootTxt1.getText().toString().equals("-")) showDialog();
                }

            }
        }
    };

    private TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    rootTxt2.setText(s);
                    rootTxt2.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                rootTxt2.setText(s);
                rootTxt2.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    rootTxt2.setText(s.subSequence(0, 1));
                    rootTxt2.setSelection(1);
                    return;
                }
            }

            if (rootTxt2.getText().length() > 0) {
                try {
                    submitBtn.setEnabled(true);
                    Double.parseDouble(rootTxt2.getText().toString());
                } catch (NumberFormatException e) {
                    submitBtn.setEnabled(false);
                    if (!rootTxt2.getText().toString().equals("-")) showDialog();
                }

            }

        }
    };

    private void showDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Message").setIcon(android.R.drawable.stat_notify_error);
        builder.setMessage("Your input is not of proper format，please correct ☺");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView time = (TextView) findViewById(R.id.timerTxt);
            long spentTime = System.currentTimeMillis() - startTime;
            long minius = (spentTime / 1000) / 60;
            long seconds = (spentTime / 1000) % 60;
            time.setText(minius + ":" + seconds);    /*display time*/
            handler.postDelayed(this, 1000);
        }
    };

}

