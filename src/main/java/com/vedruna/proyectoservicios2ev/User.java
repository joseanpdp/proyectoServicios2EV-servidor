package com.vedruna.proyectoservicios2ev;

import java.io.*;
import java.net.*;
import java.util.*;

public class User {
    String name;
    InetAddress inetAddress;
    int port;

    public User(String name, InetAddress inetAddress, int port) {
        this.name = name;
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InetAddress getInetAddress() {
        return this.inetAddress;
    }

    public int getPort() {
        return this.port;
    }
}
