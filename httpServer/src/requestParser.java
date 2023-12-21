public class requestParser {
    String wholeRequest;
    String requestType;
    String requestedFile;


    public requestParser(String request) {
        this.wholeRequest = request;
        String[] data = request.split(" ");
        requestType = data[0];
        requestedFile = data[1];
    }
}
