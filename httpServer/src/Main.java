import Logger.Logger;
import RSON.RSON;

import java.io.*;
import java.net.ServerSocket;


public class Main {
    static String confPath;
    static RSON conf;
    public static int PORT;
    public static String WEBROOT;


    public static void main(String[] args) throws IOException{
        confPath = args[0];
        conf = new RSON(args[0]);
        PORT = conf.getInt("port");
        WEBROOT = conf.getValue("webroot");
        ServerSocket server = new ServerSocket(PORT);
        Logger logger = new Logger(conf.getValue("loggerpath"));
        logger.log("http server started");

        while (conf.getBool("run")) {
            if (reloadConf()) {
                server.close();
                server = new ServerSocket(PORT);
            }
            Worker worker = new Worker(conf, server.accept());
            worker.run();
            worker.close();
        }

        logger.log("http server stopped");
    }

    public static boolean reloadConf() {
        int port = PORT;
        conf = new RSON(confPath);
        PORT = conf.getInt("port");
        WEBROOT = conf.getValue("webroot");
        return port != PORT;
    }
}