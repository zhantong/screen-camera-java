/**
 * Created by zhantong on 2016/12/2.
 */
public class ColorBlock implements Block {
    private int bitsPerUnit;
    private CustomColor[] mColors;
    public ColorBlock(int bitsPerUnit,CustomColor[] colors){
        if(colors.length!=Math.pow(2,bitsPerUnit)){
            throw new IllegalArgumentException();
        }
        this.bitsPerUnit=bitsPerUnit;
        mColors=colors;
    }
    public void draw(Image image, int x, int y, int width, int height, int value, int barcodeIndex, int column, int row) {
        CustomColor color=mColors[value];
        image.fillRect(x,y,width,height,color);
    }

    public int getBitsPerUnit() {
        return bitsPerUnit;
    }
}
