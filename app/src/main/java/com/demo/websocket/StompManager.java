package com.demo.websocket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;



import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ua.naiksoftware.stomp.LifecycleEvent;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompMessage;



public class StompManager {

    public static final String LOG_TAG = "StompManager";

    private volatile HandlerThread handlerThread;
    private ServiceHandler serviceHandler;
    private LifeCycleListener lifeCycleListener;

    private MyStompClient stompClient;
    public static final String SUPPORTED_VERSIONS = "1.1";




    public StompManager(String url, LifeCycleListener lifeCycleListener) {
        this.lifeCycleListener = lifeCycleListener;
        handlerThread = new HandlerThread("ConnectionService.HandlerThread");
        handlerThread.start();
        serviceHandler = new ServiceHandler(handlerThread.getLooper());

        stompClient = MyStomp.over(Stomp.ConnectionProvider.OKHTTP, url);

        stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(this::handleConnectionLifecycle);
    }

    public void connect() {
        Log.d(LOG_TAG, "Connecting...");
        serviceHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Connecting in other thread.");
//                List<StompHeader> headers = new ArrayList<>();
//                headers.add(new StompHeader(StompHeader.VERSION, SUPPORTED_VERSIONS));
//                stompClient.connect(headers);
                List<StompHeader> headers = new ArrayList<>();
                headers.add(new StompHeader("accept-version", SUPPORTED_VERSIONS));
                stompClient.connect(headers);
            }
        });
    }

    public void disconnect() {
        stompClient.disconnect();
    }

    public void send(String mapping, String message) {
        Log.d(LOG_TAG, "Sending message");
        stompClient.send(mapping, message).subscribe();/*subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(handler);*/

    }

    public void subscribeTopic(String topic, MyListener listener) {
        Log.d(LOG_TAG, "Subscribe Topic");
        StompHeader header = new StompHeader("deviceId", "nishith");
        List<StompHeader> list = new ArrayList<>();
        list.add(header);
        stompClient.topic(topic, list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StompMessage>() {
                    @Override
                    public void call(StompMessage stompMessage) {
                        Log.e("test1",stompMessage.getPayload());
                        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.PROTECTED).create();
                        StompMessagePayload payload=gson.fromJson(stompMessage.getPayload(),StompMessagePayload.class);
                        listener.onMessageReceived(payload);
                    }
                });
    }


    private void handleConnectionLifecycle(LifecycleEvent event) {
        switch (event.getType()) {
            case OPENED:
                Log.d(LOG_TAG, "################## ONLINE!");
                if (lifeCycleListener != null) {
                    lifeCycleListener.onOpen();
                }
                break;
            case ERROR:
                Log.d(LOG_TAG, "################## ERROR! Trying to connect again...");
                Log.i(LOG_TAG, event.getException().getMessage());
                Log.i(LOG_TAG, event.getException().getCause().getMessage());
                try {
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    Log.d(LOG_TAG, "Thread.sleep() exception.");
                }
                // connect();
                break;
            case CLOSED:
                Log.d(LOG_TAG, "################## OFFLINE!");
                break;
        }

    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    public interface MyListener{
        void onMessageReceived(StompMessagePayload message);
    }


}
