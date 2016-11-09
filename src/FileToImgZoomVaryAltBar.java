/**
 * Created by zhantong on 2016/11/5.
 */
public class FileToImgZoomVaryAltBar extends FileToImgZoomVaryAlt {
    public static void main(String[] args) {
        String inputFilePath = "/Users/zhantong/Desktop/test.txt";
        String outputImageDirectory = "/Users/zhantong/Desktop/test1/";
        FileToImg f = new FileToImgZoomVaryAltBar();
        f.toImg(inputFilePath, outputImageDirectory);
    }
    public FileToImgZoomVaryAltBar(){
        super();
        frameVaryLength=1;
        frameVaryTwoLength=1;
    }
}
