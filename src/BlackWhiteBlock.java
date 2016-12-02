import java.awt.*;

/**
 * Created by zhantong on 2016/11/17.
 */
public class BlackWhiteBlock implements Block{
    public void draw(Image image,int x,int y,int width,int height,int value,int barcodeIndex,int column,int row){
        CustomColor color;
        switch (value){
            case 0:
                color=CustomColor.BLACK;
                break;
            case 1:
                color=CustomColor.WHITE;
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
