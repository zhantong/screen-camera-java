import java.awt.*;
import java.util.BitSet;

/**
 * Created by zhantong on 16/4/23.
 */
public class FileToImgZoom extends FileToImg {
    public static void main(String[] args) {
        String inputFilePath = "/Users/zhantong/Desktop/test3.txt";
        String outputImageDirectory = "/Users/zhantong/Desktop/test5/";
        FileToImg f = new FileToImgZoom();
        f.toImg(inputFilePath, outputImageDirectory);
    }
    public FileToImgZoom(){
        bitsPerBlock=2;
        contentBlock = 40;
        blockLength = 20;
        ecSymbol = 40;
    }
    protected void addContent(DrawImage img, BitSet content) {
        int contentLeftOffset = frameWhiteBlock + frameBlackBlock + frameVaryFirstBlock + frameVarySecondBlock;
        int contentTopOffset = frameWhiteBlock + frameBlackBlock;
        int contentRightOffset = contentLeftOffset + contentBlock;
        int contentBottomOffset = contentTopOffset + contentBlock;
        img.clearBackground(Color.BLACK,contentLeftOffset,contentTopOffset,contentBlock,contentBlock);
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
                img.fillRect(x,y,offsetX,offsetY,width,height);
                index+=bitsPerBlock;
            }
        }
    }
}
