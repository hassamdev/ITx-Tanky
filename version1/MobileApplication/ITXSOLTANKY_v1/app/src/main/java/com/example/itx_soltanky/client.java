package com.example.itx_soltanky;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONException;

public class client {
//    Map<String, Object> tanks_data = new HashMap<>();
    static int max_tank = 4;
    static int container_id = 1;
    static pump main_pump = new pump();
    static List<tank> tank_list = new ArrayList<tank>();
    static int cur_tank_index = 0;
    static MainActivity cur_Activity ;
    static TankEditActivity edit_Activity ;
    public static  void set_current_Activivy(MainActivity _this){
        cur_Activity = _this;
    }
    public static void set_data_in_list(String _text){
        int no_of_tanks = _text.split("\\$").length - 1;

        for(int i=0;i<no_of_tanks;i++){
            int _dollar_index = _text.indexOf('$');
            tank_list.get(i).set_variable_from_raw(_text.substring(0,_dollar_index));
            _text = _text.substring(_dollar_index+1);
        }

        // to set the remaining tanks to default
        for(int i=no_of_tanks;i<max_tank;i++){
            tank_list.get(i).set_on_default();
        }

        String array[] = _text.split("#");
        //array =[String(line_water),PumpStatus.OnValve]
        main_pump.set_pump_status(Integer.parseInt(array[1]));
        main_pump.set_line_water(Integer.parseInt(array[0]));

        if(Integer.parseInt(array[1]) != -1){
            for(int i=0;i<max_tank;i++){
                if(tank_list.get(i).getValve()==Integer.parseInt(array[1])){
                    tank_list.get(i).set_status(1);
                    break;
                }
            }
        }


    }
    public static void create_tank_objects(){
        for (int i = 0; i < max_tank; i++) {
            tank_list.add(new tank());
        }
    }
    public static void pump_on() {
        String data=on_off_String(1);
        connect.curr_websocket.send(data);
    }

    public static void pump_of() {
        String data=on_off_String(0);
        connect.curr_websocket.send(data);
    }
//    Ind_id means tank_id

    public static String on_off_String(int _status){ // status presents the on / off command i.e 1 / 0

        String temp_string = "6,"+Integer.toString(_status)+","+Integer.toString(client.tank_list.get(client.cur_tank_index).getValve());
        return temp_string;
    }


}
