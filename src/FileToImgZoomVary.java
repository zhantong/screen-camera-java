import java.awt.*;
import java.util.BitSet;

/**
 * Created by zhantong on 16/4/24.
 */
public class FileToImgZoomVary extends FileToImg {
    public static void main(String[] args) {
        String inputFilePath = "/Users/zhantong/Desktop/test3.txt";
        String outputImageDirectory = "/Users/zhantong/Desktop/test6/";
        FileToImg f = new FileToImgZoomVary();
        f.toImg(inputFilePath, outputImageDirectory);
    }
    public FileToImgZoomVary(){
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
                if((x+y)%2==0){
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
        int leftVaryLeftOffset = frameWhiteBlock + frameBlackBlock;
        int rightVaryLeftOffset = leftVaryLeftOffset + frameVaryFirstBlock + frameVarySecondBlock + contentBlock;
        int varyTopOffset = frameWhiteBlock + frameBlackBlock;
        int varyBottomOffset = varyTopOffset + contentBlock;
        if (index % 2 == 0) {
            img.fillRect(leftVaryLeftOffset, varyTopOffset, frameVaryFirstBlock, contentBlock);
            img.fillRect(rightVaryLeftOffset, varyTopOffset, frameVaryFirstBlock, contentBlock);
        } else {
            img.fillRect(leftVaryLeftOffset + frameVaryFirstBlock, varyTopOffset, frameVarySecondBlock, contentBlock);
            img.fillRect(rightVaryLeftOffset + frameVaryFirstBlock, varyTopOffset, frameVarySecondBlock, contentBlock);
        }
        int contentTopOffset = frameWhiteBlock + frameBlackBlock;
        int contentBottomOffset = contentTopOffset + contentBlock;
        int x=leftVaryLeftOffset + frameVaryFirstBlock;
        final float width=0.6f;
        final float height=0.6f;
        if(index%2==0) {
            for (int y = contentTopOffset; y < contentBottomOffset; y++) {
                img.clearBackground(Color.BLACK,x,y,1,1);
                img.setDefaultColor(Color.WHITE);
                img.fillRect(x,y,0,0.2f,width,height);
            }
        }else{
            for (int y = contentTopOffset; y < contentBottomOffset; y++) {
                img.clearBackground(Color.BLACK,x,y,1,1);
                img.setDefaultColor(Color.WHITE);
                img.fillRect(x,y,0.4f,0.2f,width,height);
            }
        }
    }
}
