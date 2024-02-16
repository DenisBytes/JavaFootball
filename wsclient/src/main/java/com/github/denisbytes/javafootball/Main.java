package com.github.denisbytes.javafootball;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class Main {
    private static final String WS_ENDPOINT = "ws://127.0.0.1:8080/ws";
    private static final int SEND_INTERVAL = 5000;
    private static final int TWO_MINUTES = 2 * 60 * 1000;

    private final Random random = new Random();
    private final String[] teams1 = {"Milan", "Inter"};
    private final String[] teams2 = {"Barcelona", "Real Madrid"};
    private final String[] comment1 = {"Team get s forward", "Team advances down the field"};
    private final String[] comment2 = {"It's chance to score...", "They are moving the ball nicely..."};
    private final String[] comment3 = {"GOAL", "MISSED"};
    private final String[] comment4 = {"Deflected and in!", "it flew in like a bullet!"};
    private final String[] comment5 = {"Should have scored from there!", "A golden opportunity squandered!"};

    @OnOpen
    public void onOpen(Session session) {
        long startTime = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            try {
                while (System.currentTimeMillis() - startTime < TWO_MINUTES) {
                    ArrayList<String> matchHighlight = new ArrayList<>();
                    matchHighlight.add(comment1[random.nextInt(2)]);
                    matchHighlight.add(comment2[random.nextInt(2)]);
                    matchHighlight.add(comment3[random.nextInt(2)]);
                    if (matchHighlight.get(2).equals("GOAL")) {
                        matchHighlight.add(comment4[random.nextInt(2)]);
                    } else {
                        matchHighlight.add(comment5[random.nextInt(2)]);
                    }

                    JsonObject jsonObject = Json.createObjectBuilder()
                            .add("team", teams1[random.nextInt(2)])
                            .add("comments", matchHighlight.toString())
                            .build();

                    session.getBasicRemote().sendText(jsonObject.toString());

                    Thread.sleep(SEND_INTERVAL);
                }
                while (System.currentTimeMillis() - startTime < TWO_MINUTES) {
                    ArrayList<String> matchHighlight = new ArrayList<>();
                    matchHighlight.add(comment1[random.nextInt(2)]);
                    matchHighlight.add(comment2[random.nextInt(2)]);
                    matchHighlight.add(comment3[random.nextInt(2)]);
                    if (matchHighlight.get(2).equals("GOAL")) {
                        matchHighlight.add(comment4[random.nextInt(2)]);
                    } else {
                        matchHighlight.add(comment5[random.nextInt(2)]);
                    }

                    JsonObject jsonObject = Json.createObjectBuilder()
                            .add("team", teams2[random.nextInt(2)])
                            .add("comments", matchHighlight.toString())
                            .build();

                    session.getBasicRemote().sendObject(jsonObject);

                    Thread.sleep(SEND_INTERVAL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received: " + message);
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    public static void main(String[] args) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            Session session = container.connectToServer(Main.class, URI.create(WS_ENDPOINT));
            CountDownLatch latch = new CountDownLatch(1);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
