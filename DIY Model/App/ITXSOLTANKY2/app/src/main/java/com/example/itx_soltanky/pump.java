package com.example.itx_soltanky;


public class pump {
    private int pump_status,water_status;
    private String name;
    public pump(){
        pump_status = 0; // 0 means off and 1 means on
        water_status = 0; // 0 means available and 1 means NA
        name = "pump";
    }
    public int get_pump_status(){return  pump_status;}
    public String get_name(){return  name;}
    public int get_line_water(){return water_status;}
    public void set_pump_status(int _pump_status){  pump_status=_pump_status;}
    public void set_line_water(int _w_status){water_status = _w_status;}
    public void set_name(String _name){ name = _name;}

}
