import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;

public class Worker extends Thread {

    private boolean timedOut = false;
    Socket socket;
    final String CRLF = "\r\n";

    InputStream inputStream;
    InputStreamReader reader;
    OutputStream writer;


    public Worker(Socket socket) {
        this.socket = socket;
    }

    public void start() {
        Controller.threadCount++;
        Instant start = Instant.now();
        Controller.logger.log(socket.getInetAddress() + " connected");
        String request;

        try {
            inputStream = socket.getInputStream();
            writer = socket.getOutputStream();
                request = getRequest();
                RequestParser parser = new RequestParser(request);

                ResponseBuilder response = new ResponseBuilder();
                if (!parser.error) {
                    Controller.logger.log(parser.requestType + " " + parser.requestedFile);
                    ContentType ct = new ContentType();
                    if (parser.requestType.equalsIgnoreCase("get")) {
                        File f = new File(Controller.WEBROOT + parser.requestedFile);
                        if (f.exists() || parser.requestedFile.equals("/")) {
                            byte[] content = Files.readAllBytes(f.toPath());
                            if (ct.isText(parser.fileType)) {

                                response.addToHeader("HTTP/1.1 200 ok");
                                response.addToHeader("Content-Type: text/" + parser.fileType + "; charset=utf-8");
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
                            if ((parser.fileType.equals("null") || ct.isText(parser.fileType)) && Controller.conf.getBool("404")) {
                                f = new File(Controller.WEBROOT + "/404.html");
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
            Controller.logger.log("IOexception");
        }
        Controller.logger.log("Served in: " + Duration.between(start, Instant.now()).toMillis() + " milli second");
        try {
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String getRequest() throws IOException {
        StringBuilder request = new StringBuilder();
        reader = new InputStreamReader(inputStream);
        Instant start = Instant.now();
        while (!reader.ready()) {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            if (duration.toSeconds() >= Controller.conf.getInt("timeOut")){
                timedOut = true;
                return "error 408 \r\n";
            }
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
            Controller.logger.log("The socket variables haven't been initialized.");

        Controller.logger.log(socket.getInetAddress() + " connection closed");
        socket.close();
        Controller.threadCount--;
    }

}