import java.awt.*;
import java.util.Arrays;

/**
 * Created by zhantong on 2016/12/2.
 */
public class ColorShiftBlock implements Block {
    private int[] channels;
    public ColorShiftBlock(int[] channels){
        this.channels=channels;
    }
    public void draw(Image image, int x, int y, int width, int height, int value,int barcodeIndex,int column,int row) {
        int littleWidth=Math.round(0.6f*width);
        int littleHeight=Math.round(0.6f*height);
        int[] values;
        if(channels.length==2) {
            values = new int[]{value >> 2, value & 0x03};
        }else {
            values = new int[]{value >> 4, (value >> 2) & 0x03, value & 0x03};
        }
        image.fillRect(x, y, width, height,CustomColor.Y1U0V0,0);
        for(int channelIndex=0;channelIndex<channels.length;channelIndex++){
            float littleOffsetX = 0;
            float littleOffsetY = 0;
            switch (values[channelIndex]) {
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

            backgroundColor = CustomColor.Y1U0V0;
            foregroundColor = CustomColor.Y1U1V1;
            if (barcodeIndex % 2 != 0) {
                backgroundColor = CustomColor.Y1U1V1;
                foregroundColor = CustomColor.Y1U0V0;
            }
            image.fillRect(x, y, width, height, backgroundColor,channels[channelIndex]);
            image.fillRect(x + Math.round(littleOffsetX * width), y + Math.round(littleOffsetY * height), littleWidth, littleHeight, foregroundColor,channels[channelIndex]);
        }
    }
    public int getBitsPerUnit() {
        return channels.length*2;
    }
}
