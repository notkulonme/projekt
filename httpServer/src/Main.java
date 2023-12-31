import RSON.RSON;

import java.io.*;
import java.net.ServerSocket;


public class Main {
    static RSON conf = new RSON("conf.rson");
    public static int PORT = conf.getInt("port");
    public static String WEBROOT = conf.getValue("webroot");



    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(PORT);
        while (true) {
            if(reloadConf())
            {
                server.close();
                server = new ServerSocket(PORT);
            }
            Worker worker = new Worker(conf, server.accept());
            worker.run();
            worker.close();
        }
    }
    public static boolean reloadConf()
    {
        int port = PORT;
        conf = new RSON("conf.rson");
        PORT = conf.getInt("port");
        WEBROOT = conf.getValue("webroot");
        return port != PORT;
    }
}