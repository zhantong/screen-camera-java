/**
 * Created by zhantong on 2016/12/2.
 */
public class ColorBlock implements Block {
    private int bitsPerUnit;
    public ColorBlock(int bitsPerUnit){
        this.bitsPerUnit=bitsPerUnit;
    }
    public void draw(Image image, int x, int y, int width, int height, int value, int barcodeIndex, int column, int row) {
        CustomColor color=null;
        switch (value){
            case 0:
                color=CustomColor.Y0U0V0;
                break;
            case 1:
                color=CustomColor.Y1U1V1;
                break;
        }
        image.fillRect(x,y,width,height,color,0);
        image.fillRect(x,y,width,height,color,1);
        //image.fillRect(x,y,width,height,color,2);
    }

    public int getBitsPerUnit() {
        return bitsPerUnit;
    }
}
