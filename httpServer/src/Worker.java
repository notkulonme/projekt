import Logger.Logger;
import RSON.RSON;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Worker {

    final RSON conf;
    Logger logger;
    Socket socket;
    final String CRLF = "\r\n";
    String file;
    InputStream inputStream;
    InputStreamReader reader;
    OutputStream writer;


    public Worker(RSON conf, Socket socket) {
        this.socket = socket;
        this.conf = conf;
        logger = new Logger(this.conf.getValue("loggerpath"));
    }

    public void run() {
        logger.log(socket.getInetAddress() + " connected");
        String request = "";

        try {
            inputStream = socket.getInputStream();
            writer = socket.getOutputStream();

            request = getRequest();
            String response = "";
            requestParser parser = new requestParser(request);

            logger.log(parser.requestType + " " + parser.requestedFile);
            if (parser.validRequest) {
                if (parser.requestType.toLowerCase().equals("get")) {
                    ImageType imageType = new ImageType();
                    if (!imageType.isImage(parser.fileType)) {

                        if (parser.requestedFile.equals("/")) {
                            file = toString(new File(Main.WEBROOT + "/index.html"));
                            response = "HTTP/1.1 202 ok" + CRLF +
                                    "Content-Length: " + file.getBytes().length + CRLF +
                                    "Connection: close" + CRLF +//header
                                    CRLF +
                                    file +
                                    CRLF + CRLF;
                        } else {
                            try {
                                file = toString(new File(Main.WEBROOT + parser.requestedFile));
                                response = "HTTP/1.1 200 ok" + CRLF +
                                        "Content-Length: " + file.getBytes().length + CRLF +
                                        "Connection: close" + CRLF +//header
                                        CRLF +
                                        file +
                                        CRLF + CRLF;
                            } catch (FileNotFoundException e) {
                                file = toString(new File(Main.WEBROOT + "/404.html"));
                                response = "HTTP/1.1 404 Not Found" + CRLF +
                                        "Content-Length: " + file.getBytes().length + CRLF +
                                        "Connection: close" + CRLF +//header
                                        CRLF +
                                        file +
                                        CRLF + CRLF;
                            }
                        }
                        writer.write(response.getBytes());
                    } else {
                        byte[] imageBytes = new byte[10];
                        try {
                            imageBytes = Files.readAllBytes(Path.of(Main.WEBROOT+"/"+parser.requestedFile));
                        } catch (IOException e) {
                            logger.log("The requested image was not found.");
                        }
                        String header = "HTTP/1.1 202 ok" + CRLF +
                                "Content-Type: image/" + parser.fileType + CRLF +
                                "Connection: close" +
                                CRLF + CRLF;
                        byte[] reseponse = addArrays(header.getBytes(), imageBytes);
                        reseponse = addArrays(reseponse, (CRLF + CRLF).getBytes());
                        writer.write(reseponse);
                    }

                }
            } else {
                //file = toString(new File(Main.WEBROOT + "/404.html"));
                response = "HTTP/1.1 500 Internal Server Error" + CRLF +
                        "Connection: close"//header
                        + CRLF + CRLF;
                writer.write(response.getBytes());
            }


            writer.flush();
        } catch (IOException e) {
            logger.log("IOexception");
        }

    }


    private String getRequest() throws IOException {
        String request = "";
        reader = new InputStreamReader(inputStream);
        Instant start = Instant.now();
        while (!reader.ready()) {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            if (duration.toSeconds() > 2.5)
                break;
        }
        while (reader.ready()) {
            request += (char) reader.read();
        }
        //logger.log("Full request: " + request);
        return request;
    }


    private String toString(File file) throws FileNotFoundException {

        Scanner scanner = null;
        scanner = new Scanner(file);
        String str = "";
        while (scanner.hasNextLine())
            str += scanner.nextLine();

        scanner.close();
        return str + " ";

    }

    public void setSocket(Socket socket) {
        this.socket = socket;
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
        logger.log("");
    }

    private static byte[] addArrays(byte[] arr1, byte[] arr2) {
        byte[] finalArr = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, finalArr, 0, arr1.length);
        System.arraycopy(arr2, 0, finalArr, arr1.length, arr2.length);
        //System.out.println(Arrays.toString(finalArr));
        return finalArr;
    }
}
