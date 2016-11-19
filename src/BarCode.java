import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by zhantong on 2016/11/17.
 */
public class BarCode {
    Districts districts;
    BarCodeConfig config;
    public static void main(String[] args){
        int[] onesArray=new int[]{255,255,255,255,255};
        int[] zerosArray=new int[]{0,0,0,0,0};
        int[] varysArray=new int[]{85,85,85,85,85};
        int[] dataArray=new int[]{1,2,3,4,5,6,7,8};
        BitContent ones=new BitContent(Utils.intArrayToBitSet(onesArray,8));
        BitContent zeros=new BitContent(Utils.intArrayToBitSet(zerosArray,8));
        BitContent varys=new BitContent(Utils.intArrayToBitSet(varysArray,8));
        BitContent data=new BitContent(Utils.intArrayToBitSet(dataArray,8));


        BarCode barCode=new BarCode(new BarCodeConfig());
        barCode.districts.get(Districts.MARGIN).get(District.LEFT).addContent(ones);
        barCode.districts.get(Districts.MARGIN).get(District.UP).addContent(ones);
        barCode.districts.get(Districts.MARGIN).get(District.RIGHT).addContent(ones);
        barCode.districts.get(Districts.MARGIN).get(District.DOWN).addContent(ones);

        barCode.districts.get(Districts.BORDER).get(District.LEFT).addContent(varys);
        barCode.districts.get(Districts.BORDER).get(District.UP).addContent(zeros);
        barCode.districts.get(Districts.BORDER).get(District.RIGHT).addContent(varys);
        barCode.districts.get(Districts.BORDER).get(District.DOWN).addContent(zeros);

        barCode.districts.get(Districts.MAIN).get(District.MAIN).addContent(data);

        Image image=barCode.toImage();
        try {
            image.save("png","test.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public BarCode(BarCodeConfig config){
        this.config=config;
        districts=new Districts();
        int MARGIN=Districts.MARGIN;
        int BORDER=Districts.BORDER;
        int PADDING=Districts.PADDING;
        int MAIN_DISTRICT=Districts.MAIN;

        int LEFT=District.LEFT;
        int UP=District.UP;
        int RIGHT=District.RIGHT;
        int DOWN=District.DOWN;
        int LEFT_UP=District.LEFT_UP;
        int RIGHT_UP=District.RIGHT_UP;
        int RIGHT_DOWN=District.RIGHT_DOWN;
        int LEFT_DOWN=District.LEFT_DOWN;
        int MAIN_ZONE=District.MAIN;
        districts.get(MARGIN).set(LEFT_UP,new Zone(config.marginLeftWidth,
                config.marginUpHeight,
                0,
                0));
        districts.get(MARGIN).set(LEFT,new Zone(config.marginLeftWidth,
                config.borderUpHeight+config.paddingUpHeight+config.mainHeight+config.paddingDownHeight+config.borderDownHeight,
                districts.get(MARGIN).get(LEFT_UP).startInBlockX(),
                districts.get(MARGIN).get(LEFT_UP).endInBlockY()));
        districts.get(MARGIN).set(UP,new Zone(config.borderLeftWidth+config.paddingLeftWidth+config.mainWidth+config.paddingRightWidth+config.borderRightWidth,
                config.marginUpHeight,
                districts.get(MARGIN).get(LEFT_UP).endInBlockX(),
                districts.get(MARGIN).get(LEFT_UP).startInBlockY()));
        districts.get(MARGIN).set(LEFT_DOWN,new Zone(config.marginLeftWidth,
                config.marginDownHeight,
                districts.get(MARGIN).get(LEFT).startInBlockX(),
                districts.get(MARGIN).get(LEFT).endInBlockY()));
        districts.get(MARGIN).set(DOWN,new Zone(districts.get(MARGIN).get(UP).widthInBlock,
                config.marginDownHeight,
                districts.get(MARGIN).get(LEFT_DOWN).endInBlockX(),
                districts.get(MARGIN).get(LEFT_DOWN).startInBlockY()));
        districts.get(MARGIN).set(RIGHT_UP,new Zone(config.marginRightWidth,
                config.marginUpHeight,
                districts.get(MARGIN).get(UP).endInBlockX(),
                districts.get(MARGIN).get(UP).startInBlockY()));
        districts.get(MARGIN).set(RIGHT,new Zone(config.marginRightWidth,
                districts.get(MARGIN).get(LEFT).heightInBlock,
                districts.get(MARGIN).get(RIGHT_UP).startInBlockX(),
                districts.get(MARGIN).get(RIGHT_UP).endInBlockY()));
        districts.get(MARGIN).set(RIGHT_DOWN,new Zone(config.marginRightWidth,
                config.marginDownHeight,
                districts.get(MARGIN).get(RIGHT).startInBlockX(),
                districts.get(MARGIN).get(RIGHT).endInBlockY()));

        districts.get(BORDER).set(LEFT_UP,new Zone(config.borderLeftWidth,
                config.borderUpHeight,
                districts.get(MARGIN).get(LEFT).endInBlockX(),
                districts.get(MARGIN).get(UP).endInBlockY()));
        districts.get(BORDER).set(UP,new Zone(config.paddingLeftWidth+config.mainWidth+config.paddingRightWidth,
                config.borderUpHeight,
                districts.get(BORDER).get(LEFT_UP).endInBlockX(),
                districts.get(BORDER).get(LEFT_UP).startInBlockY()));
        districts.get(BORDER).set(LEFT,new Zone(config.borderLeftWidth,
                config.paddingUpHeight+config.mainHeight+config.paddingDownHeight,
                districts.get(BORDER).get(LEFT_UP).startInBlockX(),
                districts.get(BORDER).get(LEFT_UP).endInBlockY()));
        districts.get(BORDER).set(LEFT_DOWN,new Zone(config.borderLeftWidth,
                config.borderDownHeight,
                districts.get(BORDER).get(LEFT).startInBlockX(),
                districts.get(BORDER).get(LEFT).endInBlockY()));
        districts.get(BORDER).set(DOWN,new Zone(districts.get(BORDER).get(UP).widthInBlock,
                config.borderDownHeight,
                districts.get(BORDER).get(LEFT_DOWN).endInBlockX(),
                districts.get(BORDER).get(LEFT_DOWN).startInBlockY()));
        districts.get(BORDER).set(RIGHT_UP,new Zone(config.borderRightWidth,
                config.borderUpHeight,
                districts.get(BORDER).get(UP).endInBlockX(),
                districts.get(BORDER).get(UP).startInBlockY()));
        districts.get(BORDER).set(RIGHT,new Zone(config.borderRightWidth,
                districts.get(BORDER).get(LEFT).heightInBlock,
                districts.get(BORDER).get(RIGHT_UP).startInBlockX(),
                districts.get(BORDER).get(RIGHT_UP).endInBlockY()));
        districts.get(BORDER).set(RIGHT_DOWN,new Zone(config.borderRightWidth,
                config.borderDownHeight,
                districts.get(BORDER).get(RIGHT).startInBlockX(),
                districts.get(BORDER).get(RIGHT).endInBlockY()));

        districts.get(PADDING).set(LEFT_UP,new Zone(config.paddingLeftWidth,
                config.paddingUpHeight,
                districts.get(BORDER).get(LEFT).endInBlockX(),
                districts.get(BORDER).get(UP).endInBlockY()));
        districts.get(PADDING).set(UP,new Zone(config.mainWidth,
                config.paddingUpHeight,
                districts.get(PADDING).get(LEFT_UP).endInBlockX(),
                districts.get(PADDING).get(LEFT_UP).startInBlockY()));
        districts.get(PADDING).set(LEFT,new Zone(config.paddingLeftWidth,
                config.mainHeight,
                districts.get(PADDING).get(LEFT_UP).startInBlockX(),
                districts.get(PADDING).get(LEFT_UP).endInBlockY()));
        districts.get(PADDING).set(LEFT_DOWN,new Zone(config.paddingLeftWidth,
                config.paddingDownHeight,
                districts.get(PADDING).get(LEFT).startInBlockX(),
                districts.get(PADDING).get(LEFT).endInBlockY()));
        districts.get(PADDING).set(DOWN,new Zone(districts.get(PADDING).get(UP).widthInBlock,
                config.paddingDownHeight,
                districts.get(PADDING).get(LEFT_DOWN).endInBlockX(),
                districts.get(PADDING).get(LEFT_DOWN).startInBlockY()));
        districts.get(PADDING).set(RIGHT_UP,new Zone(config.paddingRightWidth,
                config.paddingUpHeight,
                districts.get(PADDING).get(UP).endInBlockX(),
                districts.get(PADDING).get(UP).startInBlockY()));
        districts.get(PADDING).set(RIGHT,new Zone(config.paddingRightWidth,
                districts.get(PADDING).get(LEFT).heightInBlock,
                districts.get(PADDING).get(RIGHT_UP).startInBlockX(),
                districts.get(PADDING).get(RIGHT_UP).endInBlockY()));
        districts.get(PADDING).set(RIGHT_DOWN,new Zone(config.paddingRightWidth,
                config.paddingDownHeight,
                districts.get(PADDING).get(RIGHT).startInBlockX(),
                districts.get(PADDING).get(RIGHT).endInBlockY()));
        districts.get(MAIN_DISTRICT).set(MAIN_ZONE,new Zone(config.mainWidth,
                config.mainHeight,
                districts.get(PADDING).get(LEFT).endInBlockX(),
                districts.get(PADDING).get(UP).endInBlockY()));

        int[] parts=new int[]{District.LEFT,District.UP,District.RIGHT,District.DOWN,
                District.LEFT_UP,District.RIGHT_UP,District.RIGHT_DOWN,District.LEFT_DOWN};
        for(int part:parts){
            districts.get(Districts.MARGIN).get(part).addBlock(config.marginBlock.get(part));
            districts.get(Districts.BORDER).get(part).addBlock(config.borderBlock.get(part));
            districts.get(Districts.PADDING).get(part).addBlock(config.paddingBlock.get(part));

            districts.get(Districts.MARGIN).get(part).addContent(config.marginContent.get(part));
            districts.get(Districts.BORDER).get(part).addContent(config.borderContent.get(part));
            districts.get(Districts.PADDING).get(part).addContent(config.paddingContent.get(part));
        }
        districts.get(Districts.MAIN).get(District.MAIN).addBlock(config.mainBlock.get(District.MAIN));
    }
    public Image toImage(){
        int blockLengthInPixels=config.blockLengthInPixel;
        Image image=new Image(districts.get(Districts.MARGIN).get(District.RIGHT).endInBlockX()*blockLengthInPixels,districts.get(Districts.MARGIN).get(District.DOWN).endInBlockY()*blockLengthInPixels);

        int countDistrict=0;
        Iterator<District> districtItr=districts.iterator();
        while(districtItr.hasNext()){
            System.out.println("count district: "+countDistrict++);
            District district=districtItr.next();
            int countZone=0;
            Iterator<Zone> zoneItr=district.iterator();
            while(zoneItr.hasNext()){
                Zone zone=zoneItr.next();
                if(zone!=null&&zone.getContent()!=null){
                    zone.toImage(image,blockLengthInPixels);
                    System.out.println("count zone: "+countZone++);
                }
            }
        }
        return image;
    }
}
