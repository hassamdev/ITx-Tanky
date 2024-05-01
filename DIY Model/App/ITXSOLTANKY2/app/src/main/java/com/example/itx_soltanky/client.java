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
    static int container_id = 1;
    static pump main_pump = new pump();
    static LinkedHashMap<String, Object> tanks_data = new LinkedHashMap<>();
    static List<Map.Entry<String, Object>> tank_list;
    static int cur_tank_index = 0;
    static MainActivity cur_Activity ;
    public static  void set_current_Activivy(MainActivity _this){
        cur_Activity = _this;
    }
    public static void set_data_in_list(JSONObject json_dict) throws JSONException {
        // clearing data before storing updated data.
        tanks_data.clear();
        Iterator<String> keys = json_dict.keys();
        for (; keys.hasNext(); ) {
            String value = json_dict.getString(keys.next());
            JSONObject json_value = new JSONObject(value);
            tank temp = new tank();
            if(json_value.has(constants.PUMP_STRING)){
                main_pump.set_pump_status(Integer.parseInt(json_value.getString(constants.PUMP_STRING)));
                main_pump.set_line_water(Integer.parseInt(json_value.getString(constants.W_STATUS_STRING)));
            }
            temp.set_all_attribute(
                    json_value.getString(constants.NAME_STRING),
                    json_value.getString(constants.VOLUME_STRING),
                    json_value.getString(constants.LIFE_TIME_STRING),
                    Integer.parseInt(json_value.getString(constants.STATUS_STRING)),
                    Integer.parseInt(json_value.getString(constants.WATER_LEVEL)),
                    Integer.parseInt(json_value.getString(constants.id_string)),
                    Integer.parseInt(json_value.getString(constants.VALVE_STRING))
            );
            tanks_data.put(temp.get_name(), temp);
            tank_list = new ArrayList<>(tanks_data.entrySet());
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
//        {"Id":1, "Ind_Id":1, "Pump":true, "Status":1}
        tank temp_tank = (tank) tank_list.get(cur_tank_index).getValue();
        String temp_string = "{\""+constants.id_string+"\":"+client.container_id+",\""+constants.IND_ID_STRING+"\":"+temp_tank.getTank_id()+",\""+constants.PUMP_STRING +"\":true,\""+constants.STATUS_STRING+"\":"+_status+"}";
        return temp_string;
    }

    public static boolean check_name(String _name){
        for(int i=0;i<tank_list.size();i++){
            tank temp_tank = (tank) tank_list.get(i).getValue();
//            System.out.println(temp_tank.get_name()+_name);
            if(temp_tank.get_name().equals(_name))
                return true;
        }
        return false;
    }

    public static boolean check_valve(String _valve){
        for(int i=0;i<tank_list.size();i++){
            tank temp_tank = (tank) tank_list.get(i).getValue();
            if(temp_tank.getValve()==Integer.parseInt(_valve) && cur_tank_index != i)
                return true;
        }
        return false;
    }
}
