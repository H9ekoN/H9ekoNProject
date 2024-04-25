package com.mygdx.game.KryoNet.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.KryoNet.packages.PacketMessage;
import com.mygdx.game.MyScreen;

import java.util.Date;

public class ServerProgram extends Listener {
    public Server server;
    MyScreen myScreen;
    static int udpPort = 27960, tcpPort = 27960;
    Connection character;
    public ServerProgram(MyScreen myScreen) throws Exception{
        this.myScreen = myScreen;
        server = new Server();
        server.getKryo().register(PacketMessage.class);
        server.bind(tcpPort, udpPort);
        server.start();
        server.addListener(this);
    }
@Override
    public void connected(Connection c){
        character = c;
        PacketMessage packetMessage = new PacketMessage();
        packetMessage.x = myScreen.charfirst.getX();
        packetMessage.y = myScreen.charfirst.getY();
        c.sendTCP(packetMessage);
        myScreen.Mod = 2;
    }
@Override
    public void received(Connection connection, Object object) {
        if (object instanceof PacketMessage){
            PacketMessage packetMessage = (PacketMessage) object;
            myScreen.charsecond.body.setTransform(packetMessage.x, packetMessage.y, 0);
            packetMessage.x = myScreen.charfirst.getX();
            packetMessage.y = myScreen.charfirst.getY();
            connection.sendTCP(packetMessage);
        }
    }

    public void disconnected(Connection connection) {
    }
}