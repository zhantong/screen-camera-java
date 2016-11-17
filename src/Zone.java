import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by zhantong on 2016/11/17.
 */
public class Zone {
    private Block defaultBlock;
    private int blockWidthInPixel;
    private int blockHeightInPixel;
    private int widthInBlock;
    private int heightInBlock;
    private int baseOffsetInBlockX;
    private int baseOffsetInBlockY;
    public static void main(String[] args){
        Zone zone=new Zone(10,10,new BlackWhiteBlock(),4,4,1,1);
        Image image=new Image((zone.widthInBlock+2)*zone.blockWidthInPixel,(zone.heightInBlock+2)*zone.blockHeightInPixel, BufferedImage.TYPE_INT_RGB);
        int[] content={1,2,3};
        BitContent bitContent=new BitContent(Utils.intArrayToBitSet(content,8),1);
        zone.fillZone(image,bitContent);
        try {
            image.save("png","test.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Zone(int widthInBlock, int heightInBlock, Block defaultBlock,int blockWidthInPixel, int blockHeightInPixel,int baseOffsetInBlockX,int baseOffsetInBlockY){
        this.defaultBlock=defaultBlock;
        this.blockWidthInPixel = blockWidthInPixel;
        this.blockHeightInPixel = blockHeightInPixel;
        this.widthInBlock=widthInBlock;
        this.heightInBlock=heightInBlock;
        this.baseOffsetInBlockX=baseOffsetInBlockX;
        this.baseOffsetInBlockY=baseOffsetInBlockY;
    }
    public void fillZone(Image image, BitContent content){
        for(int y=0;y<heightInBlock;y++){
            for(int x=0;x<widthInBlock;x++){
                int value=content.get();
                defaultBlock.draw(image,(baseOffsetInBlockX+x)* blockWidthInPixel,(baseOffsetInBlockY+y)* blockHeightInPixel, blockWidthInPixel, blockHeightInPixel,value);
            }
        }
    }
}
