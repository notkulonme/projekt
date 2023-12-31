import java.util.ArrayList;

public class ImageType {
    private ArrayList<String> list;
    public ImageType() {
        list = new ArrayList<>();
        list.add("jpg");
        list.add("jpeg");
        list.add("png");
        list.add("svg");
        list.add("webp");
        list.add("ico");
        list.add("jfif");
        list.add("pjpeg");
        list.add("pjp");
        list.add("avif");
        list.add("bmp");
        list.add("tiff");
        list.add("tif");
        list.add("giff");
    }
    public boolean isImage(String type){
        return list.contains(type);
    }
}
