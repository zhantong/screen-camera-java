import java.awt.*;

/**
 * Created by zhantong on 2016/11/17.
 */
public class BlackWhiteBlock implements Block{
    private CustomColor mBlack;
    private CustomColor mWhite;
    public BlackWhiteBlock(CustomColor black,CustomColor white){
        mBlack=black;
        mWhite=white;
    }
    public void draw(Image image,int x,int y,int width,int height,int value,int barcodeIndex,int column,int row){
        CustomColor color;
        switch (value){
            case 0:
                color=mBlack;
                break;
            case 1:
                color=mWhite;
                break;
            default:
                throw new IllegalArgumentException();
        }
        image.fillRect(x,y,width,height,color);
    }

    public int getBitsPerUnit() {
        return 1;
    }
}
