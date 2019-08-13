package com.demo.websocket;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class MainActivity extends AppCompatActivity implements LifeCycleListener {

    private Button start;
    private TextView output;
    StompManager stompManager;


    private StompClient mStompClient;

    @Override
    public void onOpen() {
        Log.i("Kashish", "onOpen in callnback");
        String dHeader = "Kashish";
        stompManager.subscribeTopic("/topic/welcome-user" + dHeader, new StompManager.MyListener() {
            @Override
            public void onMessageReceived(StompMessagePayload message) {
                if (message != null) {
                    Log.i("Kashish ->Message", message.getBody());
                    output.setText(message.getBody());
                }
            }
        });

        JSONObject object = new JSONObject();
        try {
            object.put("query", "Kishor kumar");
            object.put("content_filter", "2");
            object.put("geoLocation", "IN");
            object.put("include", new JSONArray().put("allItems"));
            object.put("isRegSrch", 0);
            String s = object.toString();

            stompManager.send("/app/search.register", s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*private class LongOperation extends AsyncTask<String, Void, String> {

        private StompClient mStompClient;

        String TAG="LongOperation";

        @Override
        protected String doInBackground(String... params) {

            Map<String, String> map = new HashMap<>();
            map.put("deviceId", "Kashish");
            //mStompClient = Stomp.over(WebSocket.class, "ws://192.169.36.204:8080/gaanasearch-api/", map );

            StompManager stompManager = new StompManager("ws://192.169.36.204:8080/gaanasearch-api/searchws", MainActivity.this);
            stompManager.connect();
            String dHeader = "Kashish";
            stompManager.subscribeTopic("/topic/welcome-user" + dHeader, new Action1<StompMessage>() {
                @Override
                public void call(StompMessage stompMessage) {

                }
            });

            *//*mStompClient.topic("/topic/welcome-user"+ dHeader).subscribe(new Action1<StompMessage>() {
                @Override
                public void call(StompMessage topicMessage) {
                    Log.d(TAG, topicMessage.getPayload());
                }
            });

            JSONObject object = new JSONObject();
            try {
                object.put("query", "Kishor kumar");
                object.put("content_filter", "2");
                object.put("geoLocation", "IN");
                object.put("include", new JSONArray().put("allItems"));
                object.put("isRegSrch" , 0);
                String s = object.toString();

                mStompClient.send("/app/search.register", s).subscribe();
            } catch (JSONException e) {
                e.printStackTrace();
            }*//*



     *//*mStompClient.lifecycle().subscribe(new Action1<LifecycleEvent>() {
                @Override
                public void call(LifecycleEvent lifecycleEvent) {
                    switch (lifecycleEvent.getType()) {

                        case OPENED:
                            Log.d(TAG, "Stomp connection opened");
                            break;

                        case ERROR:
                            Log.e(TAG, "Error", lifecycleEvent.getException());
                            break;

                        case CLOSED:
                            Log.d(TAG, "Stomp connection closed");
                            break;
                    }
                }
            });
            mStompClient.connect();*//*
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

    }*/


    /*private final class MyWebSocketListener extends WebSocketListener {

        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
*//*
            "/app/search.register",
                    {deviceId:dHeader},
            JSON.stringify({query: searchQuery,
                    content_filter: "2",
                    geoLocation: "IN",
                    include: ["allItems"],
            isRegSrch: 0}
          *//*


            JSONObject object = new JSONObject();
            try {
                object.put("query", "Kishor kumar");
                object.put("content_filter", "2");
                object.put("geoLocation", "IN");
                object.put("include", new JSONArray().put("allItems"));
                object.put("isRegSrch" , 0);
                String s = object.toString();

                webSocket.send(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
           // webSocket.send("Hello, it's SSaurel !");
            // webSocket.send("What's up ?");
           // webSocket.send(ByteString.decodeHex("deadbeef"));
           // webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        // client = new OkHttpClient();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new LongOperation().execute("");
                //start();
                Map<String, String> map = new HashMap<>();
                map.put("deviceId", "Kashish");
                //mStompClient = Stomp.over(WebSocket.class, "ws://192.169.36.204:8080/gaanasearch-api/", map );
                if (stompManager == null)
                    stompManager = new StompManager(
                            "ws://192.169.36.204:8080/gaanasearch-api/searchws", MainActivity.this);
                /*StompManager stompManager = new StompManager("ws://echo.websocket.org");*/

                stompManager.connect();


            }
        });
    }

   /* private void start() {
        Request request = new Request.Builder()
                .url("ws://192.169.36.204:8080/gaanasearch-api/searchws/037/wspmkua5/websocket")
                .header("deviceId", "Kashish")
                .addHeader("destination", "/app/search.register")
                .build();

        MyWebSocketListener listener = new MyWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);


        //client.dispatcher().executorService().shutdown();
    }*/

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (stompManager != null)
            stompManager.disconnect();
        super.onDestroy();
    }
}
