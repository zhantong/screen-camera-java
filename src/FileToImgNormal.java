
/**
 * Created by zhantong on 16/4/23.
 */
public class FileToImgNormal extends FileToImg {
    public static void main(String[] args) {
        String inputFilePath = "/Users/zhantong/Desktop/testsanguo.txt";
        String outputImageDirectory = "/Users/zhantong/Desktop/test10/";
        FileToImg f = new FileToImgNormal();
        f.toImg(inputFilePath, outputImageDirectory);
    }
    public FileToImgNormal(){
        bitsPerBlock=1;
        contentBlock = 80;
        blockLength = 6;
        ecSymbol = 80;
    }
}
