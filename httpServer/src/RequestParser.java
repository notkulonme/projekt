public class RequestParser {
    String wholeRequest;
    String requestType; // type of the request for example: GET or POST
    String requestedFile; // the files name and type (index.html)
    String fileType; //just the type of the file without dot (html)
    Boolean error; // by default, it is false, true if there is an error
    String[] arr; //this shouldn't be here
    String errorType;

    public RequestParser(String request) {
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
                    Controller.logger.log("Request time out.");
                }
                case "closedSocket" -> {
                    requestType = null;
                    requestedFile = null;
                    error = true;
                    errorType = "Closed socket";
                    Controller.logger.log("the socket is closed");
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

            Controller.logger.log("Internal server error");
            errorType = "500 Internal Server Error";
        }
    }
}
