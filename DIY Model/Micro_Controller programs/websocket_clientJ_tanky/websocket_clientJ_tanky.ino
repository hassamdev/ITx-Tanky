

#include <Arduino.h>

#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>

#include <WebSocketsClient.h>

#include <Hash.h>

ESP8266WiFiMulti WiFiMulti;
WebSocketsClient webSocket;

bool connx_status = false;

String incoming_str;

void webSocketEvent(WStype_t type, uint8_t * payload, size_t length) {
	switch(type) {
		case WStype_DISCONNECTED:
			connx_status = false;
      break;
		case WStype_CONNECTED: {
      connx_status = true;
      // {"Id" : 1,"Ind_Id" : 1,"New" : true,"W_dist" : 10}
      // String temp_string ="{\"Id\" :"+String(container_id)+",\"Ind_Id\" :"+tank_id+",\"New\" : true,\"W_dist\" :"+W_dist+"}" ;
			// webSocket.sendTXT(temp_string);
		}
			break;
		case WStype_TEXT:
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
	while(WiFiMulti.run() != WL_CONNECTED) {
		delay(100);
	}

	// server address, port and URL
	webSocket.begin("192.168.10.23", 8000, "tank-side");

	// event handler
	webSocket.onEvent(webSocketEvent);


	// try ever 5000 again if connection has failed
	webSocket.setReconnectInterval(5000);
  webSocket.enableHeartbeat(15000, 3000, 2);

  // to clear the buffer
  incoming_str = Serial.readString();

}

void loop() {
	webSocket.loop();
  if(Serial.available()>0){ 
    incoming_str = Serial.readString();
    if(connx_status){
      webSocket.sendTXT(incoming_str);
    }
    
  }
}
















// String update_string(String w_dist){
//   // Serial.write(w_dist.c_str());
//   String temp_string = "{\"Id\" :"+String(container_id)+",\"Ind_Id\" :"+tank_id+",\"Update\" : true,\"W_dist\" :"+w_dist+"}";
//   return temp_string;
// }

