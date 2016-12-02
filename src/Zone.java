/**
 * Created by zhantong on 2016/11/17.
 */
public class Zone {
    public int widthInBlock;
    public int heightInBlock;
    public int baseOffsetInBlockX;
    public int baseOffsetInBlockY;
    private BitContent content;
    private Block block;
    public Zone(int widthInBlock, int heightInBlock,int baseOffsetInBlockX,int baseOffsetInBlockY,Block block){
        this(widthInBlock,heightInBlock,baseOffsetInBlockX,baseOffsetInBlockY,block,null);
    }
    public Zone(int widthInBlock, int heightInBlock,int baseOffsetInBlockX,int baseOffsetInBlockY,BitContent content){
        this(widthInBlock,heightInBlock,baseOffsetInBlockX,baseOffsetInBlockY,null,content);
    }
    public Zone(int widthInBlock, int heightInBlock,int baseOffsetInBlockX,int baseOffsetInBlockY){
        this(widthInBlock,heightInBlock,baseOffsetInBlockX,baseOffsetInBlockY,null,null);
    }
    public Zone(int widthInBlock, int heightInBlock,int baseOffsetInBlockX,int baseOffsetInBlockY,Block block,BitContent content){
        this.widthInBlock=widthInBlock;
        this.heightInBlock=heightInBlock;
        this.baseOffsetInBlockX=baseOffsetInBlockX;
        this.baseOffsetInBlockY=baseOffsetInBlockY;
        this.block=block;
        this.content=content;
    }
    public void addContent(BitContent content){
        this.content=content;
    }
    public void addBlock(Block block){
        this.block=block;
    }
    public BitContent getContent(){
        return content;
    }
    public void toImage(Image image, int blockLengthInPixel, int barcodeIndex){
        toImage(image,block,blockLengthInPixel,barcodeIndex);
    }
    public void toImage(Image image, Block block, int blockLengthInPixel, int barcodeIndex){
        int pos=0;
        int blockWidthInPixel=blockLengthInPixel;
        int blockHeightInPixel=blockLengthInPixel;
        int bitsPerUnit=block.getBitsPerUnit();
        for(int y=0;y<heightInBlock;y++){
            for(int x=0;x<widthInBlock;x++){
                int value=content.get(pos,bitsPerUnit);
                pos+=bitsPerUnit;
                block.draw(image,(baseOffsetInBlockX+x)* blockWidthInPixel,(baseOffsetInBlockY+y)* blockHeightInPixel, blockWidthInPixel, blockHeightInPixel,value,barcodeIndex,x,y);
            }
        }
    }
    public int startInBlockX(){
        return baseOffsetInBlockX;
    }
    public int startInBlockY(){
        return baseOffsetInBlockY;
    }
    public int endInBlockX(){
        return baseOffsetInBlockX+widthInBlock;
    }
    public int endInBlockY(){
        return baseOffsetInBlockY+heightInBlock;
    }
}
