package com.example.itx_soltanky;

public class tank {
    private String name,life_time,filling_status;
    private int water_level,valve;
    public void  tank(){
        set_on_default();
    }
    public void set_all_attribute(String _name,
                                  String _life_time,int _water_level,int _valve,String _filling_status){

        name = _name;
        life_time= _life_time;
        water_level = _water_level;
        valve = _valve;
        filling_status = _filling_status;
    }
    public void set_on_default(){
        name = "default";
        life_time= "default";
        water_level = 0;
        valve = 0;
        filling_status = "default";
    }

    public void set_variable_from_raw(String _text){
        String[] attribute_array = _text.split(",");
        set_name(attribute_array[0]);
        set_valve(Integer.parseInt(attribute_array[2]));
        set_water_level( (int) Float.parseFloat(attribute_array[1]));
        set_status(0);
//        if(filling_status=="default"){
//            set_water_level( (int) Float.parseFloat(attribute_array[1]));
//
//        }else if(filling_status=="Filling"){
//
//            if(water_level< (int) Float.parseFloat(attribute_array[1])){
//                set_water_level( (int) Float.parseFloat(attribute_array[1]));
//            }
//        }else if(filling_status=="N-F"){
//            if(water_level> (int) Float.parseFloat(attribute_array[1])){
//                set_water_level( (int) Float.parseFloat(attribute_array[1]));
//            }
//        }


    }
    public void set_name(String _name){ name = _name;}
    public void calculate_life_time(String _life_time){ life_time = _life_time;}
    public void set_status(int _status){
        if(_status ==1)
            filling_status = "Filling";
        else
            filling_status = "N-F";
    }
    public void set_water_level(int _water_level){ water_level = _water_level;}
    public void set_valve(int _valve){
        valve = _valve;
    }
    public String get_name(){return name;}
    public String get_life_time(){return life_time;}
    public String get_status(){return filling_status;}
    public int get_water_level(){return water_level;}
    public int getValve(){
        return valve;
    }
}
