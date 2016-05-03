
/**
 * Created by zhantong on 16/4/23.
 */
public class FileToImgNormal extends FileToImg {
    public static void main(String[] args) {
        String inputFilePath = "/Users/zhantong/Desktop/test.txt";
        String outputImageDirectory = "/Users/zhantong/Desktop/test2/";
        FileToImg f = new FileToImgNormal();
        f.toImg(inputFilePath, outputImageDirectory);
    }
    public FileToImgNormal(){
        bitsPerBlock=1;
        contentLength = 80;
        blockLength = 6;
        ecNum = 80;
    }
}
