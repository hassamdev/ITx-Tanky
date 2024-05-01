
#include <Arduino.h>

// #include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>

#include <WebSocketsClient.h>

#include <Hash.h>

ESP8266WiFiMulti WiFiMulti;
WebSocketsClient webSocket;

int container_id = 1;
int W_status = 0;
String incoming_str;

void webSocketEvent(WStype_t type, uint8_t * payload, size_t length) {
	switch(type) {
		case WStype_DISCONNECTED:
      Serial.write("(0,0)"); //to off the pump and valve
			break;
		case WStype_CONNECTED: {
      // {"Id" : 1,"Ind_Id" : 1,"New" : true,"W_dist" : 10}
      String temp_string ="{\"Id\" :"+String(container_id)+",\"New\" : true,\"W_Status\" :"+W_status+"}" ;
			webSocket.sendTXT(temp_string);
		}
			break;
		case WStype_TEXT:
      Serial.write(payload,length);
			break;
		case WStype_BIN:
			// send data to server
			// webSocket.sendBIN(payload, length);
			break;
        case WStype_PING:
            // pong will be send automatically
            break;
        case WStype_PONG:
            // answer to a ping we send
            break;
    }
}

void setup() {
	Serial.begin(115200);
	WiFiMulti.addAP("HASSAM-GHORI 9029", "987654321");
	//WiFi.disconnect();
	while(WiFiMulti.run() != WL_CONNECTED) {
		delay(100);
	}
	// server address, port and URL
	webSocket.begin("192.168.10.23", 8000, "pump-side");
	// event handler
	webSocket.onEvent(webSocketEvent);
	webSocket.setReconnectInterval(4000);
  webSocket.enableHeartbeat(15000, 3000, 2);
  // to clear the buffer
  incoming_str = Serial.readString();
}

void loop() {
	webSocket.loop();
  // webSocket.sendTXT("Connected");
  if(Serial.available()>0){ 
    incoming_str = Serial.readString();
    String temp_string = update_string(incoming_str);
    webSocket.sendTXT(temp_string);
  }
}

String update_string(String w_stat){
  String temp_string = "{\"Id\" :"+String(container_id)+",\"Update\" : true,\"W_Status\" :"+w_stat+"}";
  return temp_string;
}

