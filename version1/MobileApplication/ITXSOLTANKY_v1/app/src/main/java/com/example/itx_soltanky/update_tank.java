package com.example.itx_soltanky;

public class update_tank {
    String name,height,valve;
    update_tank(){
        name = "default";
        height = "default";
        valve = "default";
    }

    public void set_attribute(String _name,int _height, String _valve){

        name = _name;
        height = mm_to_cm(_height)+" cm" ;
        valve  = _valve;

    }
    public String mm_to_cm(int len){
        return String.valueOf(len / 10);
    }
    public String getValve(){return valve;}
    public String getName(){return name;}
    public String getHeight(){return height;}
}
