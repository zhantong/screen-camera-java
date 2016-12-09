import java.awt.*;
import java.util.Arrays;

/**
 * Created by zhantong on 2016/12/2.
 */
public class ColorShiftBlock implements Block {
    private int numChannel;
    public ColorShiftBlock(int numChannel){
        this.numChannel=numChannel;
    }
    public void draw(Image image, int x, int y, int width, int height, int value,int barcodeIndex,int column,int row) {
        int littleWidth=Math.round(0.6f*width);
        int littleHeight=Math.round(0.6f*height);
        int[] values;
        if(numChannel==2) {
            values = new int[]{value >> 2, value & 0x03};
        }else {
            values = new int[]{value >> 4, (value >> 2) & 0x03, value & 0x03};
        }
        for(int i=0;i<numChannel;i++) {
            float littleOffsetX = 0;
            float littleOffsetY = 0;
            switch (values[i]) {
                case 0:
                    littleOffsetX = 0.2f;
                    littleOffsetY = 0;
                    break;
                case 1:
                    littleOffsetX = 0.2f;
                    littleOffsetY = 0.4f;
                    break;
                case 2:
                    littleOffsetX = 0;
                    littleOffsetY = 0.2f;
                    break;
                case 3:
                    littleOffsetX = 0.4f;
                    littleOffsetY = 0.2f;
                    break;
            }
            CustomColor backgroundColor;
            CustomColor foregroundColor;

            backgroundColor = CustomColor.Y0U0V0;
            foregroundColor = CustomColor.Y1U1V1;
            if ((column + row + barcodeIndex) % 2 != 0) {
                backgroundColor = CustomColor.Y1U1V1;
                foregroundColor = CustomColor.Y0U0V0;
            }
            image.fillRect(x, y, width, height, backgroundColor,i);
            image.fillRect(x + Math.round(littleOffsetX * width), y + Math.round(littleOffsetY * height), littleWidth, littleHeight, foregroundColor,i);
        }
    }
    public int getBitsPerUnit() {
        return numChannel*2;
    }
}
