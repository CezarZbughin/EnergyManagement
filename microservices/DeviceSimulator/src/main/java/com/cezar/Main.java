package com.cezar;

import com.cezar.dto.ConsumptionEventDTO;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

public class Main {
    static String EXCHANGE_NAME = "device-exchange";
    static String ROUTING_KEY = "device.consumption";

    static String ADDRESS = "localhost";
    static long DEVICE_ID = 1;
    static String FILE_PATH = "sensor.csv";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        String sensorData = Files.readString(Paths.get(FILE_PATH), StandardCharsets.UTF_8);
        String[] lines = sensorData.split("\r\n");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(ADDRESS);
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        for(String line : lines){
            float measure = Float.parseFloat(line);
            String message = makeMessage(DEVICE_ID, measure);
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent : '" + message + "'");
            Thread.sleep(5000);
        }
    }

    public static String makeMessage(long deviceId, float measure){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
            sb.append("\"timestamp\":");
                sb.append(Instant.now().toEpochMilli());
                sb.append(",");
            sb.append("\"deviceId\":");
                sb.append(deviceId);
                sb.append(",");
            sb.append("\"measurementValue\":");
                sb.append(measure);
                sb.append("");
        sb.append("}");
        return sb.toString();
    }

}