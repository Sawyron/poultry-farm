package com.poultryfarm.network;

public interface TCPConnectionListener {
    String USER_CONNECT = "USER_CON";
    String USER_DISCONNECT = "USER_D";
    String HASH_BIRDS = "H_BIRDS";
    String DISCONNECT = "END_SESSION";

    void onConnection(TCPConnection tcpConnection);
    void onReceiveString(TCPConnection tcpConnection, String value);
    void onDisconnect(TCPConnection tcpConnection);
    void onException(TCPConnection tcpConnection, Exception e);
}
