package com.example.itx_soltanky;

public class tank {
    private String name,total_vol,life_time;
    private int water_level,tank_id,status,valve;

    public void  tank(){
        name = null;total_vol = null;life_time = null;
        status  = 0;
        water_level = 0;
        tank_id=0;
        valve = 0;
    }
    public void set_all_attribute(String _name,String _total_vol,
                                  String _life_time,int _status,int _water_level,int _tank_id,int _valve){

        name = _name;total_vol =_total_vol;life_time= _life_time;status = _status;
        water_level = _water_level;
        tank_id = _tank_id;
        valve = _valve;
    }
    public void set_name(String _name){ name = _name;}
    public void set_total_vol(String _total_vol){ total_vol = _total_vol;}
    public void set_life_time(String _life_time){ life_time = _life_time;}
    public void set_status(int _status){ status = _status;}
    public void set_water_level(int _water_level){ water_level = _water_level;}
    public String get_name(){return name;}
    public String get_total_vol(){return total_vol;}
    public String get_life_time(){return life_time;}
    public int get_status(){return status;}
    public int get_water_level(){return water_level;}
    public int getTank_id(){return tank_id;}

    public int getValve(){
        return valve;
    }
}
