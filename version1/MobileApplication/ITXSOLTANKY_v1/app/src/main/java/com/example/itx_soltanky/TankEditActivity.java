package com.example.itx_soltanky;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TankEditActivity extends AppCompatActivity {
    int maximum_height = 380;// in cm
    int minimum_height = 10;// in cm
    Button Update_btn;
    TextView Name_var,Height_var,Valve_var;
    EditText Tank_name,Tank_height;
    Spinner unit;
    String _unit_value;
    String[] units_array = new String[]{"","cm","in","ft"};
    String[] example_array = new String[]{"Tank 1","100 cm","1"};

    update_tank tank = new update_tank();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tank_edit);
        client.edit_Activity = this;
        connect.send_string(connect.connect_string());// sending 6,app to tank component

        Update_btn = findViewById(R.id.update_button);

        Name_var = findViewById(R.id.name_var);
        Height_var = findViewById(R.id.height_var);
        Valve_var = findViewById(R.id.valve_var);

        Tank_name = findViewById(R.id.name_edit_text);
        Tank_height = findViewById(R.id.height_edit_text);

        unit = findViewById(R.id.spinner_unit);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(adapter);
        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                _unit_value = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected
            }

        });

        Update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name, height;
                name = String.valueOf(Tank_name.getText());
                height = String.valueOf(Tank_height.getText());
                if(name.isEmpty() || height.isEmpty()){
                    Toast.makeText(TankEditActivity.this,R.string.empty_msg,Toast.LENGTH_SHORT).show();
                    return;
                } else if (name.length()>10) {
                    Toast.makeText(TankEditActivity.this,R.string.name_len,Toast.LENGTH_SHORT).show();
                    return;
                } else if(_unit_value.isEmpty()){
                    Toast.makeText(TankEditActivity.this,R.string.unit_msg, LENGTH_SHORT).show();
                    return;
                }

                int tank_height = calculate_height_in_cm(_unit_value,Integer.parseInt(height));
                if(tank_height > maximum_height || tank_height < minimum_height){
                    Toast.makeText(TankEditActivity.this,R.string.too_much_height, LENGTH_SHORT).show();
                    return;
                }
                String temp_str = "6,"+name+","+cm_to_mm(tank_height);
                Toast.makeText(TankEditActivity.this,temp_str,LENGTH_SHORT).show();
                connect.send_string(temp_str);
                Tank_name.setText("");
                Tank_height.setText("");
            }
        });


        set_attribute();
    }

    public  void set_attribute(){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                // Update or modify UI elements here
                Name_var.setText(tank.getName());
                Height_var.setText(tank.getHeight());
                Valve_var.setText(tank.getValve());
            }
        });

    }

    public int cm_to_mm(int len){
        return len * 10;
    }
    public int calculate_height_in_cm(String unit,int _height){
        if (unit=="cm")
            return _height;
        else if(unit=="in")
            return (int) (2.54 * _height);
        else if(unit=="ft")
            return (int) (30.48 * _height);
        return 0;
    }

    public void error_activity(int err_head,int err_var){
        errorActivity.set_error(err_head,err_var);
        Intent intent = new Intent(TankEditActivity.this, errorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
        // Finish all activities in the application
//        finishAffinity();
    }

}