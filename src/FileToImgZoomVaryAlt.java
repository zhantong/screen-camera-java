import java.awt.*;
import java.util.BitSet;

/**
 * Created by zhantong on 16/5/6.
 */
public class FileToImgZoomVaryAlt extends FileToImg {
    public static void main(String[] args) {
        String inputFilePath = "/Users/zhantong/Desktop/test3.txt";
        String outputImageDirectory = "/Users/zhantong/Desktop/test11/";
        FileToImg f = new FileToImgZoomVaryAlt();
        f.toImg(inputFilePath, outputImageDirectory);
    }
    public FileToImgZoomVaryAlt(){
        bitsPerBlock=2;
        contentLength = 40;
        blockLength = 20;
        ecNum = 40;
    }
    protected void addContent(DrawImage img, BitSet content,int barcodeIndex) {
        int contentLeftOffset = frameWhiteBlock + frameBlackLength + frameVaryLength + frameVaryTwoLength;
        int contentTopOffset = frameWhiteBlock + frameBlackLength;
        int contentRightOffset = contentLeftOffset + contentLength;
        int contentBottomOffset = contentTopOffset + contentLength;
        img.clearBackground(Color.BLACK,contentLeftOffset,contentTopOffset, contentLength, contentLength);
        img.setDefaultColor(Color.WHITE);
        int index = 0;
        float offsetX=0;
        float offsetY=0;
        final float width=0.6f;
        final float height=0.6f;
        for (int y = contentTopOffset; y < contentBottomOffset; y++) {
            for (int x = contentLeftOffset; x < contentRightOffset; x++) {
                int con=0;
                for(int i=0;i<bitsPerBlock;i++){
                    con=(con<<1)+(content.get(index+i)?1:0);
                }
                switch (con){
                    case 0:
                        offsetX=0.2f;
                        offsetY=0;
                        break;
                    case 1:
                        offsetX=0.2f;
                        offsetY=0.4f;
                        break;
                    case 2:
                        offsetX=0;
                        offsetY=0.2f;
                        break;
                    case 3:
                        offsetX=0.4f;
                        offsetY=0.2f;
                        break;
                }
                if((x+y+barcodeIndex)%2==0){
                    img.clearBackground(Color.BLACK,x,y,1,1);
                    img.setDefaultColor(Color.WHITE);
                    img.fillRect(x,y,offsetX,offsetY,width,height);
                }
                else{
                    img.clearBackground(Color.WHITE,x,y,1,1);
                    img.setDefaultColor(Color.BLACK);
                    img.fillRect(x,y,offsetX,offsetY,width,height);
                }
                index+=bitsPerBlock;
            }
        }
    }
    protected void addVary(DrawImage img, int index) {
        img.setDefaultColor(Color.BLACK);
        int leftVaryLeftOffset = frameWhiteBlock + frameBlackLength;
        int rightVaryLeftOffset = leftVaryLeftOffset + frameVaryLength + frameVaryTwoLength + contentLength;
        int varyTopOffset = frameWhiteBlock + frameBlackLength;
        int varyBottomOffset = varyTopOffset + contentLength;

        if (index % 2 == 0) {
            img.fillRect(leftVaryLeftOffset, varyTopOffset, frameVaryLength, contentLength);
            img.fillRect(rightVaryLeftOffset, varyTopOffset, frameVaryLength, contentLength);
        } else {
            img.fillRect(leftVaryLeftOffset + frameVaryLength, varyTopOffset, frameVaryTwoLength, contentLength);
            img.fillRect(rightVaryLeftOffset + frameVaryLength, varyTopOffset, frameVaryTwoLength, contentLength);
        }

        if(index%2==0){
            img.setDefaultColor(Color.BLACK);
            img.fillRect(leftVaryLeftOffset+frameVaryLength,varyTopOffset,1,1);
            img.fillRect(leftVaryLeftOffset+frameVaryLength,varyBottomOffset-frameVaryLength,1,1);
        }else{
            img.setDefaultColor(Color.WHITE);
            img.fillRect(leftVaryLeftOffset+frameVaryLength,varyTopOffset,1,1);
            img.fillRect(leftVaryLeftOffset+frameVaryLength,varyBottomOffset-frameVaryLength,1,1);
        }
        if(index/2%2==0){
            img.setDefaultColor(Color.BLACK);
            img.fillRect(leftVaryLeftOffset+frameVaryLength,varyTopOffset+frameVaryLength,1,1);
        }else{
            img.setDefaultColor(Color.WHITE);
            img.fillRect(leftVaryLeftOffset+frameVaryLength,varyTopOffset+frameVaryLength,1,1);
        }
    }
}
