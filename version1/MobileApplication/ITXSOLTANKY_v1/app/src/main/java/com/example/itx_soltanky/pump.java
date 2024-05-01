package com.example.itx_soltanky;


public class pump {
    private String line_water,pump_status;
    public pump(){
        pump_status = null; // 0 means off and 1 means on
        line_water = null; // 0 means available and 1 means NA

    }
    public String get_pump_status(){return  pump_status;}
    public String get_line_water(){return line_water;}
    public void set_pump_status(int _pump_status){
        if(_pump_status ==-1)
            pump_status="OFF";
        else
            pump_status="ON";
    }
    public void set_line_water(int _w_status){
        if(_w_status==0)
            line_water = "N-A";
        else
            line_water = "Available";
    }


}
