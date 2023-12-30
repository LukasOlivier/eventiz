package com.example.eventiz.network;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.eventiz.ui.screens.EventizViewModel;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.PusherEvent;

import java.util.logging.Logger;

public class Websockets {

    public Websockets(EventizViewModel viewModel) {
        PusherOptions options = new PusherOptions()
                .setCluster("mt1")
                .setHost("141.134.173.203")
                .setWsPort(6001)
                .setWssPort(6001)
                .setUseTLS(false);

        Pusher pusher = new Pusher("websocket", options);

        pusher.connect();

        pusher.subscribe("sensors", new ChannelEventListener() {
            @Override
            public void onError(String message, Exception e) {
                Logger.getLogger("Websockets").warning(message);
                ChannelEventListener.super.onError(message, e);
            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {
                Logger.getLogger("Websockets").info("Subscribed!");
                System.out.println("Subscribed!");
            }

            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onEvent(PusherEvent event) {
                Gson gson = new Gson();
                WebsocketSensorEvent exampleEvent = gson.fromJson(event.getData(), WebsocketSensorEvent.class);
                viewModel.setSensorValue(exampleEvent.type, exampleEvent.value);
            }
        }, "App\\Events\\SensorUpdate");

    }

    public static class WebsocketSensorEvent {
        public String type;
        public Float value;
    }
}