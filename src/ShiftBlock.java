import java.awt.*;

/**
 * Created by zhantong on 2016/11/19.
 */
public class ShiftBlock implements Block {
    @Override
    public void draw(Image image, int x, int y, int width, int height, int value,int barcodeIndex,int column,int row) {
        int littleWidth=Math.round(0.6f*width);
        int littleHeight=Math.round(0.6f*height);
        float littleOffsetX=0;
        float littleOffsetY=0;
        switch (value){
            case 0:
                littleOffsetX=0.2f;
                littleOffsetY=0;
                break;
            case 1:
                littleOffsetX=0.2f;
                littleOffsetY=0.4f;
                break;
            case 2:
                littleOffsetX=0;
                littleOffsetY=0.2f;
                break;
            case 3:
                littleOffsetX=0.4f;
                littleOffsetY=0.2f;
                break;
        }
        CustomColor backgroundColor=CustomColor.Y0UmVm;
        CustomColor foregroundColor=CustomColor.Y1UmVm;
        if((column+row+barcodeIndex)%2!=0){
            backgroundColor=CustomColor.Y1UmVm;
            foregroundColor=CustomColor.Y0UmVm;
        }
        image.fillRect(x,y,width,height, backgroundColor);
        image.fillRect(x+Math.round(littleOffsetX*width),y+Math.round(littleOffsetY*height),littleWidth,littleHeight,foregroundColor);
    }

    @Override
    public int getBitsPerUnit() {
        return 2;
    }
}
