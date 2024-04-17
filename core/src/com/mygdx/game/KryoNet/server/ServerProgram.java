package com.mygdx.game.KryoNet.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.KryoNet.packages.PacketMessage;

import java.util.Date;

public class ServerProgram extends Listener {
    static Server server;
    static int udpPort = 27777, tcpPort = 27777;
    public static void main(String[] args) throws Exception {
        server = new Server();
        server.getKryo().register(Package.class);
        server.bind(tcpPort, udpPort);

        server.start();
        server.addListener(new ServerProgram());
    }

    public void connected(Connection c){
        PacketMessage packetMessage = new PacketMessage();
        packetMessage.message = "Hello friend! The time is: " +new Date().toString();
        c.sendTCP(packetMessage);
    }

    public void received(Connection connection, Object object) {
    }

    public void disconnected(Connection connection) {
    }
}