package com.example.itx_soltanky;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class connect{
    static int app_connected=0;
    static OkHttpClient connx_client = new OkHttpClient();
    static Request request = new Request.Builder().url("ws://192.168.10.23:8000/app-side").build();
    static splashActivity splash;
    static WebSocket curr_websocket ;
    static WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
//            System.out.println("WebSocket connection opened");
            curr_websocket = webSocket;
            webSocket.send(connect_string());
            // You can send messages or perform other actions when the connection is opened
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Received message: " + text);
            // Handle the received message
            try {
                JSONObject json = new JSONObject(text);
                client.set_data_in_list(json);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if(app_connected==0){
                app_connected = 1;
                splash.main_activity();
            }else {
                client.cur_Activity.do_changes();
            }

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("Received bytes: " + bytes.hex());
            // Handle the received bytes
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
            if(app_connected==1){
                client.cur_Activity.error_activity(R.string.E1,R.string.cnx_fail);
            }else {
                splash.error_activity(R.string.E1,R.string.cnx_fail);
            }
            app_connected= 0;


        }
    };
    public static void send_string(String data){
        curr_websocket.send(data);
    }

    public static String connect_string(){
//        {"Id":1,"New":true}
        String connect_string = "{\""+constants.id_string+"\":"+client.container_id+",\""+constants.new_string+"\":"+"true}";
        return  connect_string;
    }

    // Create a WebSocket connection
    public static void connect(splashActivity context) {
        splash = context;
        WebSocket webSocket = connx_client.newWebSocket(request, webSocketListener);

    }
}


