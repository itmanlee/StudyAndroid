package com.wangkeke.studyandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new WakeThread("192.168.1.128","18:18:18:19:19:19").start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        start("192.168.1.128","18:18:18:19:19:19");
                    }
                }).start();
            }
        });
    }

    public void start(String ipStr,String macStr) {

        try {
            byte[] macBytes = getMacBytes(macStr);
            byte[] bytes = new byte[6 + 16 * macBytes.length];

            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }

            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            InetAddress address = InetAddress.getByName(ipStr);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, 22);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i<bytes.length;i++){
                sb.append(String.valueOf(Integer.valueOf(bytes[i])));
            }
            System.out.println("Wake-on-LAN packet sent:  "+sb);
        }
        catch (Exception e) {
            System.out.println("Failed to send Wake-on-LAN packet: + e");
            System.exit(1);
        }

    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}
