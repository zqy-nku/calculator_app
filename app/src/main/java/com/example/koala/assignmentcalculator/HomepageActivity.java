package com.example.koala.assignmentcalculator;


import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class HomepageActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;               //start bgm
    private TextView studentnumTxt;        //from homeactivity to mainactivity
    private Switch MusicStc;               //music switch button
    private Button btn;                    //start quiz button
    private Button button3;                //quiz rules button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mediaPlayer = MediaPlayer.create(this, R.raw.music1);

        studentnumTxt = (TextView) findViewById(R.id.studentnumTxt);
        btn = (Button) findViewById(R.id.startquizBtn);
        button3 = (Button) findViewById(R.id.button3);
        MusicStc = (Switch) findViewById(R.id.MusicStc);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomepageActivity.this, MainActivity.class);
                intent.putExtra("go1", "Student UID");
                startActivityForResult(intent, 1);

            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        MusicStc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    Toast.makeText(HomepageActivity.this, "Start Music", Toast.LENGTH_LONG).show();
                } else {
                    mediaPlayer.pause();
                    Toast.makeText(HomepageActivity.this, "Pause Music", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void showDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(HomepageActivity.this);
        builder.setTitle("Math Quiz Rules").setIcon(android.R.drawable.stat_notify_error);
        builder.setMessage("Please round to 2 decimal places if your answers are not integers! \n\nLV.1 Bronze tier \n-> Mark= 0-25;\n\nLV.2 Silver tier \n-> Mark= 26-50;" +
                "\n\nLV.3 Gold tier \n-> Mark= 51-75;\n\nLV.4 Platinum tier \n-> Mark= 76-100;\n\nLV.5 Diamond tier \n-> Mark= 101-125;" +
                "\n\nLV.6 Challenger tier \n-> Mark= 126-150.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            if (data != null) {
                studentnumTxt.setText(data.getStringExtra("back"));
            }
        }
    }


}


