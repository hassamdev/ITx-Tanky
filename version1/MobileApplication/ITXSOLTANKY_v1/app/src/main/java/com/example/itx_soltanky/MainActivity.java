package com.example.itx_soltanky;



//import static androidx.core.app.ActivityRecreator.mainHandler;
import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button main_btn,on_btn,off_btn,arrow_left,arrow_right;
    Animation left_in,right_in,top_in,left_out,right_out,top_out,main_btn_anim,arrow_anim;
    TextView press_btn_view,water_level_var,status_var,tank_name_var,life_time_var,total_vol_var,line_water_var,pump_var;
    LottieAnimationView water_fill_anim;
    ViewGroup.MarginLayoutParams margin_params;
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client.set_current_Activivy(this);
        main_btn = findViewById(R.id.main_btn);
        on_btn = findViewById(R.id.on_button);
        off_btn = findViewById(R.id.off_button);
        arrow_left = findViewById(R.id.arrow_left_button);
        arrow_right = findViewById(R.id.arrow_right_button);

        arrow_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.arrow_anim);

        arrow_right.startAnimation(arrow_anim);

        // condition on arrow keys;
        if(client.cur_tank_index==client.tank_list.size()-1) {
            arrow_right.setVisibility(View.INVISIBLE);
            arrow_right.clearAnimation();
        }
        if(client.cur_tank_index>0) {
            arrow_left.setVisibility(View.VISIBLE);
            arrow_left.startAnimation(arrow_anim);
        }else {
            arrow_left.setVisibility(View.INVISIBLE);
//            arrow_left.clearAnimation();
        }
//        lottie animation
        water_fill_anim = findViewById(R.id.water_fill_anim1);
        margin_params = (ViewGroup.MarginLayoutParams) water_fill_anim.getLayoutParams();

        // text views
        press_btn_view = findViewById(R.id.press_btn);
        tank_name_var = findViewById(R.id.tank_name_var);
        water_level_var = findViewById(R.id.water_level_var);
        status_var = findViewById(R.id.status_var);
        life_time_var = findViewById(R.id.life_time_var);
        line_water_var = findViewById(R.id.line_water_var);
        pump_var = findViewById(R.id.pump_var);


        // animation
        left_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in);
        right_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in);
        top_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_in);
        left_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);
        right_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_out);
        top_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_out);
        main_btn_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
        //changes all variables
        do_changes();
        // horizontal layout , below code creates the bottom dots as number of tank
        LinearLayout dot_layout = findViewById(R.id.dotview);
        for (int i = 0; i < client.tank_list.size(); i++) {
            View v = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(25, 25);
            layoutParams.leftMargin = 8;
            v.setLayoutParams(layoutParams);
            if (i == client.cur_tank_index) {
                v.setBackground(getDrawable(R.drawable._selected));
                System.out.println("Selected");
            } else {
                v.setBackground(getDrawable(R.drawable._unselected));
            }
            dot_layout.addView(v);
        }


        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_btn.startAnimation(main_btn_anim);
                toggleVisibility();
            }
        });



        off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                off_btn.startAnimation(main_btn_anim);
                if(client.main_pump.get_pump_status()=="OFF") // 0 means pump already off
                    return;
                client.pump_of();
//                do_changes();
            }
        });
        on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_btn.startAnimation(main_btn_anim);
                tank temp_tank = (tank) client.tank_list.get(client.cur_tank_index);
                if(client.main_pump.get_pump_status() == "ON") { // 1 means pump already on
                    Toast.makeText(MainActivity.this, R.string.pump_on_msg, Toast.LENGTH_SHORT).show();
                    return;
                }else if(temp_tank.get_water_level()>=100){ // 100 means when tank already full
                    Toast.makeText(MainActivity.this, R.string.tank_full_msg, Toast.LENGTH_SHORT).show();
                    return;
                }else if(temp_tank.getValve()==0){ // 0 means when valve not set , 0 is default value
                    Toast.makeText(MainActivity.this, R.string.valve_not_set_msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                client.pump_on();
//                do_changes();
            }
        });


        arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_tank();
            }
        });

        arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous_tank();
            }
        });
    }

    public void do_changes(){
        set_variables((tank) client.tank_list.get(client.cur_tank_index));
    }
    public void next_tank() {

        System.out.println(client.cur_tank_index);
        // Get the next key-value pair
        if (client.cur_tank_index < client.tank_list.size() - 1) {
            client.cur_tank_index++;
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            Animatoo.INSTANCE.animateSwipeLeft(MainActivity.this);
            finish();
        } else {
            System.out.println("No next key-value pair");
        }
    }

    public void previous_tank(){
        // Get the previous key-value pair
        if (client.cur_tank_index > 0) {
            client.cur_tank_index--;
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            Animatoo.INSTANCE.animateSwipeRight(MainActivity.this);
            finish();
        } else {
            System.out.println("No previous key-value pair");
        }
    }



    public void set_variables(tank main_tank){
        try{
            tank_name_var.setText(main_tank.get_name());
            life_time_var.setText(main_tank.get_life_time());
            line_water_var.setText(client.main_pump.get_line_water());
            pump_var.setText(client.main_pump.get_pump_status());
            status_var.setText(main_tank.get_status());
            change_water_level(main_tank.get_water_level());
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public void change_water_level(int i){
        handler.post(new Runnable() {
            @Override
            public void run() {
                margin_params.topMargin = calculate_water_level(i);
                water_level_var.setText(""+i+"%");
            }
        });

//        System.out.println(screenutils.getScreenHeight(this));

    }
    public int calculate_water_level(int tanky_level){
        tanky_level = tanky_level + 10;
        int actual_screen_height = screenutils.getScreenHeight(this);
        float percent = (float) (tanky_level) / 100;
        float _level_to_subtract = (actual_screen_height * percent);
        int virtual_level = actual_screen_height - (int) _level_to_subtract;
        return virtual_level;
    }

    public void error_activity(int err_head,int err_var){
        errorActivity.set_error(err_head,err_var);
        Intent intent = new Intent(MainActivity.this, errorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
        // Finish all activities in the application
//        finishAffinity();
    }



    private void toggleVisibility() {
        if (press_btn_view.getVisibility() == View.VISIBLE) {
            on_btn.setVisibility(View.VISIBLE);
            off_btn.setVisibility(View.VISIBLE);
            on_btn.startAnimation(right_out);
            off_btn.startAnimation(left_out);
            press_btn_view.setVisibility(View.INVISIBLE);
        } else {
            off_btn.startAnimation(left_in);
            on_btn.startAnimation(right_in);
            on_btn.setVisibility(View.INVISIBLE);
            off_btn.setVisibility(View.INVISIBLE);
            press_btn_view.setVisibility(View.VISIBLE);
        }
    }
}