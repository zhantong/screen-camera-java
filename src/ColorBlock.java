/**
 * Created by zhantong on 2016/12/2.
 */
public class ColorBlock implements Block {
    private int bitsPerUnit;
    private CustomColor[] mColors;
    public ColorBlock(int bitsPerUnit,CustomColor[] colors){
        this.bitsPerUnit=bitsPerUnit;
        mColors=colors;
    }
    public void draw(Image image, int x, int y, int width, int height, int value, int barcodeIndex, int column, int row) {
        CustomColor color=null;
        switch (value){
            case 0:
                color=mColors[0];
                break;
            case 1:
                color=mColors[1];
                break;
        }
        image.fillRect(x,y,width,height,color);
    }

    public int getBitsPerUnit() {
        return bitsPerUnit;
    }
}
