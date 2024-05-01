
//  communication structure "(1,1)" => (pump_status,valve) => e.g (1,1)

// valve pin set up
int valve1= 9;
int valve2 = 10;
int valve3 = 13;
int valve4 = 12;
int pump_pin = 11 ;
int connx_led_pin = 5; 


String income_str ;

void setup() {
  Serial.begin(115200);
  // put your setup code here, to run once:
  pinMode(valve1,OUTPUT);
  pinMode(valve2,OUTPUT);
  pinMode(valve3,OUTPUT);
  pinMode(valve4,OUTPUT);
  pinMode(pump_pin, OUTPUT);// pump pin
  pinMode(connx_led_pin,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  // for(int i=1;i<5;i++){
  //   please_on_the_valve(i);
  //   please_operate_the_pump(1);
  //   delay(1000);
  //   please_operate_the_pump(0);
  //   please_off_the_valve();
  // }

  if(Serial.available()){
    income_str = Serial.readString();
    String status = extract_command(income_str,"(",",");
    if(status =="1"){
      // Serial.println(status);
      String valve = extract_command(income_str,", ",")");
      if(valve==" 1"){
        please_on_the_valve(1);
        please_operate_the_pump(1);
      }else if(valve ==" 2"){
        please_on_the_valve(2);
        please_operate_the_pump(1);
      }else if(valve ==" 3"){
        please_on_the_valve(3);
        please_operate_the_pump(1);
      }else if(valve ==" 4"){
        please_on_the_valve(4);
        please_operate_the_pump(1);
      }
    }else if(status =="0"){
      please_operate_the_pump(0);
      please_off_the_valve();
    }
  }
  delay(1000);
  
}

void please_on_the_valve(int valve_no){
  if(valve_no==1){
    digitalWrite(valve1, HIGH);
  }else if(valve_no==2){
    digitalWrite(valve2, HIGH);
  }else if(valve_no==3){
    digitalWrite(valve3, HIGH);
  }else if(valve_no==4){
    digitalWrite(valve4, HIGH);
  }

}

void please_operate_the_pump(int req){
  if(req==1){
    digitalWrite(pump_pin, HIGH);
  }else if(req==0){
    digitalWrite(pump_pin, LOW);
  }
}

void please_off_the_valve(){
    digitalWrite(valve1, LOW);
    digitalWrite(valve2, LOW);
    digitalWrite(valve3, LOW);
    digitalWrite(valve4, LOW);

}

String extract_command(String &incoming_str,String f_limiter,String s_limiter){
  int f_index = incoming_str.indexOf(f_limiter);
  int s_index = incoming_str.indexOf(s_limiter);
  String command_string;
  if(f_index != -1){
    command_string = incoming_str.substring(f_index +1,s_index);
    return command_string;
  }
  return command_string;
}
