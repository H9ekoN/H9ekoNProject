package com.mygdx.game.KryoNet.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.KryoNet.packages.PacketMessage;

public class ClientProgram extends Listener {
    static Client client;
    static String ip = "localhost";
    static int tcpPort = 27777, udpPort = 27777;
    static boolean messageReceived = false;
    public static void main(String[] args) throws Exception{
        client = new Client();
        client.getKryo().register(Package.class);
        client.start();
        client.connect(5000, ip, tcpPort, udpPort);
        client.addListener(new ClientProgram());
        while (!messageReceived){
            Thread.sleep(1000);
        }
    }


    public void received(Connection connection, Object object) {
        if (object instanceof PacketMessage){
            PacketMessage packetMessage = (PacketMessage) object;
            messageReceived = true;
        }
    }
}