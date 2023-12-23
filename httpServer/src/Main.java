import RSON.RSON;

import java.io.*;
import java.net.ServerSocket;


public class Main {
    static RSON conf = new RSON("conf.rson");
    public static final int PORT = conf.getInt("port");
    public static final String WEBROOT = conf.getValue("webroot");


    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(PORT);
        while (true) {
            Worker worker = new Worker(conf, server.accept());
            worker.run();
            worker.close();
        }
    }
}