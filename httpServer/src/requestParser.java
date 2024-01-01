import Logger.Logger;

public class requestParser {
    String wholeRequest;
    String requestType;
    String requestedFile;
    String fileType;
    Boolean validRequest = true;
    String[] arr;


    public requestParser(String request) {
        try {
            this.wholeRequest = request;
            String[] requestSplited = request.split("\r\n");
            String[] data = requestSplited[0].split(" ");
            requestType = data[0];
            //System.out.println(data[0]);
            requestedFile = data[1];

            if (!requestedFile.equals("/")) {
                arr = requestedFile.split("\\.");
                fileType = arr[arr.length - 1];
                if (fileType.equals(requestedFile))
                    fileType = "null";
            } else {
                fileType = "html";
                requestedFile = "/index.html";
            }
        } catch (Exception e) {
            requestType = null;
            requestedFile = null;
            validRequest = false;
            Logger logger = new Logger(Main.conf.getValue("loggerpath"));
            logger.log("Iternal server error");
        }
    }
}
