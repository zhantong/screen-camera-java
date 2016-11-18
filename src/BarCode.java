import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by zhantong on 2016/11/17.
 */
public class BarCode {
    Districts districts;
    public static void main(String[] args){
        int blockLengthInPixels=4;
        int[] onesArray=new int[]{255,255,255,255,255};
        int[] zerosArray=new int[]{0,0,0,0,0};
        int[] varysArray=new int[]{85,85,85,85,85};
        int[] dataArray=new int[]{1,2,3,4,5,6,7,8};
        BitContent ones=new BitContent(Utils.intArrayToBitSet(onesArray,8));
        BitContent zeros=new BitContent(Utils.intArrayToBitSet(zerosArray,8));
        BitContent varys=new BitContent(Utils.intArrayToBitSet(varysArray,8));
        BitContent data=new BitContent(Utils.intArrayToBitSet(dataArray,8));


        BarCode barCode=new BarCode(new BarCodeConfig());
        Image image=new Image(barCode.districts.margin.right.endInBlockX()*blockLengthInPixels,barCode.districts.margin.down.endInBlockY()*blockLengthInPixels, BufferedImage.TYPE_INT_RGB);
        barCode.districts.margin.left.fillZone(image,ones);
        barCode.districts.margin.up.fillZone(image,ones);
        barCode.districts.margin.right.fillZone(image,ones);
        barCode.districts.margin.down.fillZone(image,ones);

        barCode.districts.border.left.fillZone(image,varys);
        barCode.districts.border.up.fillZone(image,zeros);
        barCode.districts.border.right.fillZone(image,varys);
        barCode.districts.border.down.fillZone(image,zeros);

        barCode.districts.main.fillZone(image,data);
        try {
            image.save("png","test.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public BarCode(BarCodeConfig config){
        int blockLengthInPixels=4;
        districts=new Districts();
        districts.margin.leftUp=new Zone(config.marginLeftWidth,
                config.marginUpHeight,
                config.marginBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                0,
                0);
        districts.margin.left=new Zone(config.marginLeftWidth,
                config.borderUpHeight+config.paddingUpHeight+config.mainHeight+config.paddingDownHeight+config.borderDownHeight,
                config.marginBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.margin.leftUp.startInBlockX(),
                districts.margin.leftUp.endInBlockY());
        districts.margin.up=new Zone(config.borderLeftWidth+config.paddingLeftWidth+config.mainWidth+config.paddingRightWidth+config.borderRightWidth,
                config.marginUpHeight,
                config.marginBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.margin.leftUp.endInBlockX(),
                districts.margin.leftUp.startInBlockY());
        districts.margin.leftDown=new Zone(config.marginLeftWidth,
                config.marginDownHeight,
                config.marginBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.margin.left.startInBlockX(),
                districts.margin.left.endInBlockY());
        districts.margin.down=new Zone(districts.margin.up.widthInBlock,
                config.marginDownHeight,
                config.marginBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.margin.leftDown.endInBlockX(),
                districts.margin.leftDown.startInBlockY());
        districts.margin.rightUp=new Zone(config.marginRightWidth,
                config.marginUpHeight,
                config.marginBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.margin.up.endInBlockX(),
                districts.margin.up.startInBlockY());
        districts.margin.right=new Zone(config.marginRightWidth,
                districts.margin.left.heightInBlock,
                config.marginBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.margin.rightUp.startInBlockX(),
                districts.margin.rightUp.endInBlockY());
        districts.margin.rightDown=new Zone(config.marginRightWidth,
                config.marginDownHeight,
                config.marginBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.margin.right.startInBlockX(),
                districts.margin.right.endInBlockY());

        districts.border.leftUp=new Zone(config.borderLeftWidth,
                config.borderUpHeight,
                config.borderBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.margin.left.endInBlockX(),
                districts.margin.up.endInBlockY());
        districts.border.up=new Zone(config.paddingLeftWidth+config.mainWidth+config.paddingRightWidth,
                config.borderUpHeight,
                config.borderBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.border.leftUp.endInBlockX(),
                districts.border.leftUp.startInBlockY());
        districts.border.left=new Zone(config.borderLeftWidth,
                config.paddingUpHeight+config.mainHeight+config.paddingDownHeight,
                config.borderBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.border.leftUp.startInBlockX(),
                districts.border.leftUp.endInBlockY());
        districts.border.leftDown=new Zone(config.borderLeftWidth,
                config.borderDownHeight,
                config.borderBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.border.left.startInBlockX(),
                districts.border.left.endInBlockY());
        districts.border.down=new Zone(districts.border.up.widthInBlock,
                config.borderDownHeight,
                config.borderBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.border.leftDown.endInBlockX(),
                districts.border.leftDown.startInBlockY());
        districts.border.rightUp=new Zone(config.borderRightWidth,
                config.borderUpHeight,
                config.borderBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.border.up.endInBlockX(),
                districts.border.up.startInBlockY());
        districts.border.right=new Zone(config.borderRightWidth,
                districts.border.left.heightInBlock,
                config.borderBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.border.rightUp.startInBlockX(),
                districts.border.rightUp.endInBlockY());
        districts.border.rightDown=new Zone(config.borderRightWidth,
                config.borderDownHeight,
                config.borderBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.border.right.startInBlockX(),
                districts.border.right.endInBlockY());

        districts.padding.leftUp=new Zone(config.paddingLeftWidth,
                config.paddingUpHeight,
                config.paddingBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.border.left.endInBlockX(),
                districts.border.up.endInBlockY());
        districts.padding.up=new Zone(config.mainWidth,
                config.paddingUpHeight,
                config.paddingBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.padding.leftUp.endInBlockX(),
                districts.padding.leftUp.startInBlockY());
        districts.padding.left=new Zone(config.paddingLeftWidth,
                config.mainHeight,
                config.paddingBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.padding.leftUp.startInBlockX(),
                districts.padding.leftUp.endInBlockY());
        districts.padding.leftDown=new Zone(config.paddingLeftWidth,
                config.paddingDownHeight,
                config.paddingBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.padding.left.startInBlockX(),
                districts.padding.left.endInBlockY());
        districts.padding.down=new Zone(districts.padding.up.widthInBlock,
                config.paddingDownHeight,
                config.paddingBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.padding.leftDown.endInBlockX(),
                districts.padding.leftDown.startInBlockY());
        districts.padding.rightUp=new Zone(config.paddingRightWidth,
                config.paddingUpHeight,
                config.paddingBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.padding.up.endInBlockX(),
                districts.padding.up.startInBlockY());
        districts.padding.right=new Zone(config.paddingRightWidth,
                districts.padding.left.heightInBlock,
                config.paddingBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.padding.rightUp.startInBlockX(),
                districts.padding.rightUp.endInBlockY());
        districts.padding.rightDown=new Zone(config.paddingRightWidth,
                config.paddingDownHeight,
                config.paddingBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.padding.right.startInBlockX(),
                districts.padding.right.endInBlockY());
        districts.main=new Zone(config.mainWidth,
                config.mainHeight,
                config.mainBlock,
                blockLengthInPixels,
                blockLengthInPixels,
                districts.padding.left.endInBlockX(),
                districts.padding.up.endInBlockY());
    }
}
