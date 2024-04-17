package com.mygdx.game.KryoNet.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.KryoNet.packages.PacketMessage;
import com.mygdx.game.MyScreen;

public class ClientProgram extends Listener {
    static Client client;
    static String ip = "localhost";
    MyScreen myScreen;
    static int tcpPort = 27777, udpPort = 27777;
    static boolean messageReceived = false;
    public ClientProgram(MyScreen myScreen) throws Exception{
        this.myScreen = myScreen;
        client = new Client();
        client.getKryo().register(Package.class);
        client.start();
        client.connect(5000, ip, tcpPort, udpPort);
        client.addListener(this);
    }


    public void received(Connection connection, Object object) {
        if (object instanceof PacketMessage){
            PacketMessage packetMessage = (PacketMessage) object;
            myScreen.charsecond.body.setTransform(packetMessage.x, packetMessage.y, 0);
            packetMessage.x = myScreen.charsecond.getX();
            packetMessage.y = myScreen.charsecond.getY();
            connection.sendTCP(packetMessage);
        }
    }
}