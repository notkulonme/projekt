import Logger.Logger;
import RSON.RSON;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;

public class Worker {

    final RSON conf;
    Logger logger;
    Socket socket;
    final String CRLF = "\r\n";

    InputStream inputStream;
    InputStreamReader reader;
    OutputStream writer;


    public Worker(RSON conf, Socket socket) {
        this.socket = socket;
        this.conf = conf;
        logger = new Logger(this.conf.getValue("loggerpath"));
    }

    public void run() {
        Instant start = Instant.now();
        logger.log(socket.getInetAddress() + " connected");
        String request;

        try {
            inputStream = socket.getInputStream();
            writer = socket.getOutputStream();

            request = getRequest();
            requestParser parser = new requestParser(request);

            ResponseBuilder response = new ResponseBuilder();
            if (!parser.error) {
                logger.log(parser.requestType + " " + parser.requestedFile);
                ContentType ct = new ContentType();
                if (parser.requestType.equalsIgnoreCase("get")) {
                    File f = new File(Main.WEBROOT + parser.requestedFile);
                    if (f.exists() || parser.requestedFile.equals("/")) {
                        byte[] content = Files.readAllBytes(f.toPath());
                        if (ct.isText(parser.fileType)) {

                            response.addToHeader("HTTP/1.1 200 ok");
                            response.addToHeader("Content-Type: text/" + parser.fileType+"; charset=utf-8");
                            response.addToHeader("Content-Length: " + content.length);
                            response.addToHeader("Connection: close");
                            response.addToBody(f);


                        } else {

                            response.addToHeader("HTTP/1.1 200 ok");
                            response.addToHeader("Content-Type: image/" + parser.fileType);
                            response.addToHeader("Connection: close");
                            response.addToBody(f);

                        }

                    } else {
                        if ((parser.fileType.equals("null") || ct.isText(parser.fileType)) && conf.getBool("404")) {
                            f = new File(Main.WEBROOT + "/404.html");
                            byte[] content = Files.readAllBytes(f.toPath());
                            response.addToHeader("HTTP/1.1 404 Not Found");
                            response.addToHeader("Content-Type: text/html");
                            response.addToHeader("Content-Length: " + content.length);
                            response.addToHeader("Connection: close");
                            response.addToBody(f);
                        } else {
                            response.addToHeader("HTTP/1.1 404 Not Found");
                            response.addToHeader("Connection: close");
                        }

                    }
                    writer.write(response.getResponse());

                }
            } else {
                //in case of an error
                writer.write(("HTTP/1.1 " + parser.errorType + CRLF + "Connection: close" + CRLF + CRLF).getBytes());
            }
            writer.flush();
        } catch (IOException e) {
            //this probably need more error handling but i don't wanna do it :c
            logger.log("IOexception");
        }
        logger.log("Served in: "+Duration.between(start, Instant.now()).toMillis()+" milli second");
    }


    private String getRequest() throws IOException {
        StringBuilder request = new StringBuilder();
        reader = new InputStreamReader(inputStream);
        Instant start = Instant.now();
        while (!reader.ready()) {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            if (duration.toSeconds() >= 2)
                return "error 408 \r\n";
        }
        while (reader.ready()) {
            request.append((char) reader.read());
        }
        return request.toString();
    }

    public void close() throws IOException {
        if (inputStream != null && writer != null && reader != null) {
            reader.close();
            inputStream.close();
            writer.close();
        } else
            logger.log("The socket variables haven't been initialized.");

        logger.log(socket.getInetAddress() + " connection closed");
        socket.close();
    }

}