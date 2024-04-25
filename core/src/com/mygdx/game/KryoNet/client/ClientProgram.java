package com.mygdx.game.KryoNet.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.KryoNet.packages.PacketMessage;
import com.mygdx.game.MyScreen;

public class ClientProgram extends Listener {
    Client client;
    static String ip = "127.0.0.1";
    MyScreen myScreen;
    static int tcpPort = 27960, udpPort = 27960;
    static boolean messageReceived = false;
    public ClientProgram(MyScreen myScreen) throws Exception{
        this.myScreen = myScreen;
        client = new Client();
        client.getKryo().register(PacketMessage.class);
        client.start();
        client.connect(5000, client.discoverHost(tcpPort, 3000), tcpPort, udpPort);
        client.addListener(this);
    }

@Override
    public void received(Connection connection, Object object) {
        if (object instanceof PacketMessage){
            PacketMessage packetMessage = (PacketMessage) object;
            myScreen.charfirst.body.setTransform(packetMessage.x, packetMessage.y, 0);
            packetMessage.x = myScreen.charsecond.getX();
            packetMessage.y = myScreen.charsecond.getY();
            connection.sendTCP(packetMessage);
            myScreen.Mod = 2;
        }
    }
}