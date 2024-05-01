

// #include <DistanceSensor.h>
#include <HC_SR04.h>

// Define pins
const int echoPin = 12;
const int trigPin = 13;
const int connx_pin = 5; // setup pending
const int tankid =2;
int curr_value ;
bool flag = true;
// Start the sensor
// DistanceSensor sensor(trigPin, echoPin);
HC_SR04<echoPin> sensor(trigPin); 

void setup () {
    // Start serial port
    Serial.begin(115200);
    sensor.begin();
}

void loop () {
    // Get distance in cm
    // int distance = sensor.getCM();
    sensor.startMeasure();
    int distance = sensor.getDist_mm();

    // Write values to serial port
    if(flag){
      curr_value = distance;
      Serial.readString();
      Serial.write(get_tuple(curr_value).c_str());
      // Serial.println(curr_value);
      flag = false;

    }else if(distance != curr_value)
    {
      // distance!=curr_value +1 && distance != curr_value-1 &&
      curr_value = distance;
      Serial.readString();
      // Serial.println(curr_value);
      Serial.write(get_tuple(curr_value).c_str());
    }
    // Wait a bit
    delay(800);
}

String get_tuple(int num){
  return String(tankid)+","+String(num);
}
