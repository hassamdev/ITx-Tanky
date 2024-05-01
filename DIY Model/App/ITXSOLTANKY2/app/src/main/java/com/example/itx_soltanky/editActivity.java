package com.example.itx_soltanky;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class editActivity extends AppCompatActivity {
    EditText name,height,volume,valve;
    Spinner unit;
    Button save_btn;
    Animation save_btn_anim;
    String _unit;
    String[] units_array = new String[]{"cm","in","ft"};
    int tank_max_height = 350;
    int tank_min_height = 30;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        name = findViewById(R.id.input_name);
        unit = findViewById(R.id.spinner_unit);
        height = findViewById(R.id.input_height);
        volume = findViewById(R.id.input_volume);
        valve = findViewById(R.id.input_valve);
        save_btn = findViewById(R.id.save_button);
        save_btn_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        Spinner spinner = findViewById(R.id.spinner);
        unit.setAdapter(adapter);
        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the spinner
                _unit = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_btn.startAnimation(save_btn_anim);
                String _name = String.valueOf(name.getText());
                String _height =  height.getText().toString();
                String _vol = volume.getText().toString();
                String _valve = valve.getText().toString();
                if(_name.isEmpty() || _height.isEmpty() || _vol.isEmpty() || _valve.isEmpty()){
                    Toast.makeText(editActivity.this,R.string.empty_msg,Toast.LENGTH_SHORT).show();
                    return;
                } else if (client.check_name(_name)) {
                    Toast.makeText(editActivity.this,R.string.name_exists_msg,Toast.LENGTH_SHORT).show();
                    return;
                }else if(Integer.parseInt(_height)<0 || Integer.parseInt(_vol)<0){
                    Toast.makeText(editActivity.this,R.string.neg_val_msg,Toast.LENGTH_SHORT).show();
                    return;
                }else if(client.check_valve(_valve)){
                    Toast.makeText(editActivity.this,R.string.valve_msg,Toast.LENGTH_SHORT).show();
                    return;
                }else if(Integer.parseInt(_valve)<1 || Integer.parseInt(_valve)>4){
                    Toast.makeText(editActivity.this,R.string.valve_limit,Toast.LENGTH_SHORT).show();
                    return;
                }
                int cal_height = calculate_height_in_cm(_unit,Integer.parseInt(_height));
                if(cal_height>tank_max_height || cal_height<tank_min_height) {
                    Toast.makeText(editActivity.this, R.string.too_much_height, Toast.LENGTH_SHORT).show();
                    return;
                }

                connect.send_string(edit_string(_name,_vol,String.valueOf(cal_height),_valve));

                Toast.makeText(editActivity.this,
                        "Successfull",
                        Toast.LENGTH_SHORT).show();
                name.setText("");
                height.setText("");
                volume.setText("");
                valve.setText("");

            }
        });

    }

    public String edit_string(String _name,String vol,String _height, String valve){
        tank tank_ = (tank) client.tank_list.get(client.cur_tank_index).getValue();
        String temp_string = "{\""+constants.id_string+"\":"+client.container_id+",\""+constants.EDIT_STRING+"\":true,\""+constants.IND_ID_STRING +"\":"+tank_.getTank_id()+",\""+constants.NAME_STRING+"\":\""+_name+"\",\""+constants.VOLUME_STRING+"\":"+vol+",\""+constants.HEIGHT_STRING+"\":"+_height+",\""+constants.VALVE_STRING+"\":"+valve+"}";
        return temp_string;
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
}