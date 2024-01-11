import Logger.Logger;

public class requestParser {
    String wholeRequest;
    String requestType;
    String requestedFile;
    String fileType;
    Boolean error;
    String[] arr;
    String errorType;


    public requestParser(String request) {
        Logger logger = new Logger(Main.conf.getValue("loggerpath"));
        try {
            error = false;
            this.wholeRequest = request;
            String[] requestSplited = request.split("\r\n");
            String[] data = requestSplited[0].split(" ");
            requestType = data[0];
            requestedFile = data[1];
            requestedFile = requestedFile.replaceAll("%20"," ");
            switch (requestedFile) {
                case "/" -> {
                    fileType = "html";
                    requestedFile = "/index.html";
                }
                case "408" -> {
                    requestType = null;
                    requestedFile = null;
                    error = true;
                    errorType = "408 Request Timeout";
                    logger.log("Request time out.");
                }
                default -> {
                    arr = requestedFile.split("\\.");
                    fileType = arr[arr.length - 1];
                    if (fileType.equals(requestedFile))
                        fileType = "null";
                }
            }

        } catch (Exception e) {
            requestType = null;
            requestedFile = null;
            error = true;

            logger.log("Iternal server error");
            errorType = "500 Internal Server Error";
        }
    }
}
