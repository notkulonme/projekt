import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

//you really just can't remove data from the header or from the body sorry :c
// i don't know what is happening here maybe i was drunk, but it works
public class ResponseBuilder {

    private byte[] byteHeader;
    private byte[] byteBody;
    final String CRLF = "\r\n";



    public ResponseBuilder() {

        this.byteHeader = new byte[0];
        this.byteBody = new byte[0];

    }


    public void addToHeader(String content) {
        content = content + CRLF;
        this.byteHeader = addArrays(this.byteHeader, content.getBytes());
    }

    public void addToBody(String content) {
        content = content + CRLF;
        this.byteBody = addArrays(this.byteBody, content.getBytes());
    }

    public void addToBody(File file) throws IOException {
        byte[] fileB = Files.readAllBytes(file.toPath());
        this.byteBody = addArrays(this.byteBody, fileB);
        this.byteBody = addArrays(this.byteBody, CRLF.getBytes());
    }
    public void addToBody(byte[] arr)
    {
        this.byteBody = addArrays(this.byteBody,arr);
        this.byteBody = addArrays(this.byteBody, CRLF.getBytes());
    }

    public byte[] getResponse() {
        byte[] header = addArrays(this.byteHeader, CRLF.getBytes());
        byte[] body = addArrays(this.byteBody, CRLF.getBytes());
        return addArrays(header, body);
    }

    public byte[] getByteHeader() {
        return byteHeader;
    }

    public static byte[] addArrays(byte[] arr1, byte[] arr2) {
        byte[] finalArr = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, finalArr, 0, arr1.length);
        System.arraycopy(arr2, 0, finalArr, arr1.length, arr2.length);
        //System.out.println(Arrays.toString(finalArr));
        return finalArr;
    }
}
