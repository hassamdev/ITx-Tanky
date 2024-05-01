package com.example.itx_soltanky;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class connect{
    static int app_connected=0;
    static String pump_state = "pump";
    static String tank_state = "tank";
    static String no_state = null;
    static String state = no_state;
    static String tank_api = "ws://192.168.3.22:81";
    static String pump_api = "ws://192.168.4.22:81";

    static OkHttpClient.Builder connx_client_builder = new OkHttpClient.Builder();
    static splashActivity splash;
    static WebSocket curr_websocket ;
    static WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            app_connected = 1;
            curr_websocket = webSocket;
//            if(state==pump_state){
//
//            }


            Thread thread = new Thread(){
                public  void run(){
                    try{
                        if(state==pump_state) {
                            client.create_tank_objects();
                            splash.main_activity();
                            webSocket.send(connect_string());
                        } else if (state==tank_state) {
                            splash.tank_edit_activity();
                        }
                    }catch (Exception e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }finally {


                    }
                }
            };
            thread.start();





        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Received message: " + text);
        // from pump String temp = "tank1,45,1$tank2,55,2$tank3,53,3$tank4,53,4$"+String(line_water)+"#"+PumpStatus.OnValve;
            if(state==pump_state) {
                client.set_data_in_list(text);
                if(client.cur_Activity!=null)
                    client.cur_Activity.do_changes();

            }else if (state==tank_state) {
                try {
                    String[] temp_str = text.split(",");// ['name','height','valve']
                    client.edit_Activity.tank.set_attribute(temp_str[0],Integer.parseInt(temp_str[1]),temp_str[2]);
                    client.edit_Activity.set_attribute();
                }catch (Exception e){
                    System.out.println(e);
                }

            }

        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {

        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            System.out.println("WebSocket closing: " + code + ", " + reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            System.out.println("WebSocket closed: " + code + ", " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.err.println("WebSocket failure: " + t.getMessage());
            System.out.println("not connect with "+state);

            if(state==pump_state && app_connected ==0){// calling
                connect_with_tank(splash);
                state=tank_state;
            }else if(state==pump_state && app_connected ==1){
                client.cur_Activity.error_activity(R.string.E1,R.string.cnx_fail);
            }else if(state==tank_state && app_connected ==1){
                client.edit_Activity.error_activity(R.string.E1,R.string.cnx_fail);
            }else{
                splash.error_activity(R.string.E1,R.string.cnx_fail);
            }


        }
    };
    public static void send_string(String data){
        curr_websocket.send(data);
    }

    public static String connect_string(){
//        {"Id":1,"New":true}
        String connect_string = "6,app";
        return  connect_string;
    }

    // Create a WebSocket connection
    public static void connect(String api_str) {
        Request request = new Request.Builder().url(api_str).build();
        connx_client_builder.connectTimeout(5, TimeUnit.SECONDS); // Adjust the timeout value as per your requirement
        OkHttpClient client = connx_client_builder.build();
        WebSocket webSocket = client.newWebSocket(request, webSocketListener);
    }
    public static void connect_with_pump(splashActivity context){
        splash = context;
        state = pump_state;
        connect(pump_api);
    }

    public static void connect_with_tank(splashActivity context){
        splash = context;
        state = tank_state;
        connect(tank_api);
    }
}


