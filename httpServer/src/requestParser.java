import Logger.Logger;

public class requestParser {
    String wholeRequest;
    String requestType;
    String requestedFile;


    public requestParser(String request) {
        try {
            this.wholeRequest = request;
            String[] data = request.split(" ");
            requestType = data[0];
            requestedFile = data[1];
        } catch (Exception e) {
            requestType = null;
            requestedFile = null;
            Logger logger = new Logger(Main.conf.getValue("loggerpath"));
            logger.log("Iternal server error");
        }
    }
}
