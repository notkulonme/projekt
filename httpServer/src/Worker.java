import Logger.Logger;
import RSON.RSON;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Worker {

    final RSON conf;
    Logger logger;
    Socket socket;
    final String CRLF = "\n\r";
    String file;
    InputStream reader = null;
    OutputStream writer = null;

    public Worker( RSON conf) {
        this.conf = conf;
        logger = new Logger(this.conf.getValue("loggerpath"));
    }
    public void run() throws IOException {
        logger.log(socket.getInetAddress()+" connected");
        reader = socket.getInputStream();
        writer = socket.getOutputStream();

        String request = getRequest();
        String response = "";

        //System.out.println(request);
        requestParser parser = new requestParser(request);
        logger.log(parser.requestType+"  "+parser.requestedFile);


            if (parser.requestType.toLowerCase().equals("get")) {
                if (parser.requestedFile.equals("/")) {
                    file = toString(new File(Main.WEBROOT + "/index.html"));
                    response = "HTTP/1.1 202 ok" + CRLF +
                            "Content-Length: " + file.getBytes().length + CRLF +//header
                            CRLF +
                            file +
                            CRLF + CRLF;
                } else {
                    try {
                        file = toString(new File(Main.WEBROOT + parser.requestedFile));
                        response = "HTTP/1.1 200 ok" + CRLF +
                                "Content-Length: " + file.getBytes().length + CRLF +//header
                                CRLF +
                                file +
                                CRLF + CRLF;
                    } catch (FileNotFoundException e) {
                        file = toString(new File(Main.WEBROOT + "/404.html"));
                        response = "HTTP/1.1 404 Not Found" + CRLF +
                                "Content-Length: " + file.getBytes().length + CRLF +//header
                                CRLF +
                                file +
                                CRLF + CRLF;
                    }
                }
            }

        writer.write(response.getBytes());
        writer.flush();
    }


    private String getRequest() throws IOException {
        String request = "";

        while (true) {
            char bytei = (char)reader.read();
            request += (char) bytei;
            if (reader.available()==0)
                break;
        }
        return request;
    }




    private String toString(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String str = "";
        while(scanner.hasNextLine())
            str+= scanner.nextLine();
        return str+" ";
    }
    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }
    public void close() throws IOException {
        if(reader != null && writer != null)
        {
            reader.close();
            writer.close();
        }
        else
            logger.log("The socket variables haven't been initialized.");

        logger.log(socket.getInetAddress()+" connection closed");
        socket.close();
    }
}
