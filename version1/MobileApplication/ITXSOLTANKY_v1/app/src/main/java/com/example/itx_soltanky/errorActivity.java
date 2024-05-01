package com.example.itx_soltanky;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class errorActivity extends AppCompatActivity {
    TextView err_var,err_heading;
    static int error_head;
    static int error_var;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        err_var= findViewById(R.id.error_var);
        err_heading = findViewById(R.id.err_heading);
        err_heading.setText(error_head);
        err_var.setText(error_var);

    }
    public static void set_error(int error_heading, int error_variable){
        error_var = error_variable;
        error_head = error_heading;

    }

}