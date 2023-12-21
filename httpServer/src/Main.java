import RSON.RSON;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



public class Main {
    static RSON conf = new RSON("conf.rson");
    public static final int PORT = conf.getInt("port");
    public static final String WEBROOT = conf.getValue("webroot");



    public static void main(String[] args) throws IOException, InterruptedException {
        final String CRLF = "\n\r";
        ServerSocket server = new ServerSocket(PORT);
        Worker worker = new Worker(conf);
        while(true){
            Socket socket = server.accept();
            worker.setSocket(socket);
            worker.run();
            worker.close();
        }
    }
    public static String toString(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String str = "";
        while(scanner.hasNextLine())
            str+= scanner.nextLine();
        return str;
    }
}
