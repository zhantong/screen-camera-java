import java.io.IOException;
import java.util.Iterator;

/**
 * Created by zhantong on 2016/11/17.
 */
public class Barcode {
    Districts districts;
    BarcodeConfig config;
    int index;
    public static void main(String[] args){
        int[] onesArray=new int[]{255,255,255,255,255};
        int[] zerosArray=new int[]{0,0,0,0,0};
        int[] varysArray=new int[]{85,85,85,85,85};
        int[] dataArray=new int[]{1,2,3,4,5,6,7,8};
        BitContent ones=new BitContent(Utils.intArrayToBitSet(onesArray,8));
        BitContent zeros=new BitContent(Utils.intArrayToBitSet(zerosArray,8));
        BitContent varys=new BitContent(Utils.intArrayToBitSet(varysArray,8));
        BitContent data=new BitContent(Utils.intArrayToBitSet(dataArray,8));


        Barcode barcode =new Barcode(0,new BarcodeConfig());
        barcode.districts.get(Districts.MARGIN).get(District.LEFT).addContent(ones);
        barcode.districts.get(Districts.MARGIN).get(District.UP).addContent(ones);
        barcode.districts.get(Districts.MARGIN).get(District.RIGHT).addContent(ones);
        barcode.districts.get(Districts.MARGIN).get(District.DOWN).addContent(ones);

        barcode.districts.get(Districts.BORDER).get(District.LEFT).addContent(varys);
        barcode.districts.get(Districts.BORDER).get(District.UP).addContent(zeros);
        barcode.districts.get(Districts.BORDER).get(District.RIGHT).addContent(varys);
        barcode.districts.get(Districts.BORDER).get(District.DOWN).addContent(zeros);

        barcode.districts.get(Districts.MAIN).get(District.MAIN).addContent(data);

        Image image= barcode.toImage(0);
        try {
            image.save(0,"./");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Barcode(int index,BarcodeConfig config){
        this.config=config;
        this.index=index;
        districts=new Districts();
        int MARGIN=Districts.MARGIN;
        int BORDER=Districts.BORDER;
        int PADDING=Districts.PADDING;
        int META=Districts.META;
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
        districts.get(MARGIN).set(LEFT_UP,new Zone(config.marginLength.get(District.LEFT),
                config.marginLength.get(District.UP),
                0,
                0));
        districts.get(MARGIN).set(LEFT,new Zone(config.marginLength.get(District.LEFT),
                config.borderLength.get(District.UP)+config.paddingLength.get(District.UP)+config.metaLength.get(District.UP)+config.mainHeight+config.metaLength.get(District.DOWN)+config.paddingLength.get(District.DOWN)+config.borderLength.get(District.DOWN),
                districts.get(MARGIN).get(LEFT_UP).startInBlockX(),
                districts.get(MARGIN).get(LEFT_UP).endInBlockY()));
        districts.get(MARGIN).set(UP,new Zone(config.borderLength.get(District.LEFT)+config.paddingLength.get(District.LEFT)+config.metaLength.get(District.LEFT)+config.mainWidth+config.metaLength.get(District.RIGHT)+config.paddingLength.get(District.RIGHT)+config.borderLength.get(District.RIGHT),
                config.marginLength.get(District.UP),
                districts.get(MARGIN).get(LEFT_UP).endInBlockX(),
                districts.get(MARGIN).get(LEFT_UP).startInBlockY()));
        districts.get(MARGIN).set(LEFT_DOWN,new Zone(config.marginLength.get(District.LEFT),
                config.marginLength.get(District.DOWN),
                districts.get(MARGIN).get(LEFT).startInBlockX(),
                districts.get(MARGIN).get(LEFT).endInBlockY()));
        districts.get(MARGIN).set(DOWN,new Zone(districts.get(MARGIN).get(UP).widthInBlock,
                config.marginLength.get(District.DOWN),
                districts.get(MARGIN).get(LEFT_DOWN).endInBlockX(),
                districts.get(MARGIN).get(LEFT_DOWN).startInBlockY()));
        districts.get(MARGIN).set(RIGHT_UP,new Zone(config.marginLength.get(District.RIGHT),
                config.marginLength.get(District.UP),
                districts.get(MARGIN).get(UP).endInBlockX(),
                districts.get(MARGIN).get(UP).startInBlockY()));
        districts.get(MARGIN).set(RIGHT,new Zone(config.marginLength.get(District.RIGHT),
                districts.get(MARGIN).get(LEFT).heightInBlock,
                districts.get(MARGIN).get(RIGHT_UP).startInBlockX(),
                districts.get(MARGIN).get(RIGHT_UP).endInBlockY()));
        districts.get(MARGIN).set(RIGHT_DOWN,new Zone(config.marginLength.get(District.RIGHT),
                config.marginLength.get(District.DOWN),
                districts.get(MARGIN).get(RIGHT).startInBlockX(),
                districts.get(MARGIN).get(RIGHT).endInBlockY()));

        districts.get(BORDER).set(LEFT_UP,new Zone(config.borderLength.get(District.LEFT),
                config.borderLength.get(District.UP),
                districts.get(MARGIN).get(LEFT).endInBlockX(),
                districts.get(MARGIN).get(UP).endInBlockY()));
        districts.get(BORDER).set(UP,new Zone(config.paddingLength.get(District.LEFT)+config.metaLength.get(District.LEFT)+config.mainWidth+config.metaLength.get(District.RIGHT)+config.paddingLength.get(District.RIGHT),
                config.borderLength.get(District.UP),
                districts.get(BORDER).get(LEFT_UP).endInBlockX(),
                districts.get(BORDER).get(LEFT_UP).startInBlockY()));
        districts.get(BORDER).set(LEFT,new Zone(config.borderLength.get(District.LEFT),
                config.paddingLength.get(District.UP)+config.metaLength.get(District.UP)+config.mainHeight+config.metaLength.get(District.DOWN)+config.paddingLength.get(District.DOWN),
                districts.get(BORDER).get(LEFT_UP).startInBlockX(),
                districts.get(BORDER).get(LEFT_UP).endInBlockY()));
        districts.get(BORDER).set(LEFT_DOWN,new Zone(config.borderLength.get(District.LEFT),
                config.borderLength.get(District.DOWN),
                districts.get(BORDER).get(LEFT).startInBlockX(),
                districts.get(BORDER).get(LEFT).endInBlockY()));
        districts.get(BORDER).set(DOWN,new Zone(districts.get(BORDER).get(UP).widthInBlock,
                config.borderLength.get(District.DOWN),
                districts.get(BORDER).get(LEFT_DOWN).endInBlockX(),
                districts.get(BORDER).get(LEFT_DOWN).startInBlockY()));
        districts.get(BORDER).set(RIGHT_UP,new Zone(config.borderLength.get(District.RIGHT),
                config.borderLength.get(District.UP),
                districts.get(BORDER).get(UP).endInBlockX(),
                districts.get(BORDER).get(UP).startInBlockY()));
        districts.get(BORDER).set(RIGHT,new Zone(config.borderLength.get(District.RIGHT),
                districts.get(BORDER).get(LEFT).heightInBlock,
                districts.get(BORDER).get(RIGHT_UP).startInBlockX(),
                districts.get(BORDER).get(RIGHT_UP).endInBlockY()));
        districts.get(BORDER).set(RIGHT_DOWN,new Zone(config.borderLength.get(District.RIGHT),
                config.borderLength.get(District.DOWN),
                districts.get(BORDER).get(RIGHT).startInBlockX(),
                districts.get(BORDER).get(RIGHT).endInBlockY()));

        districts.get(PADDING).set(LEFT_UP,new Zone(config.paddingLength.get(District.LEFT),
                config.paddingLength.get(District.UP),
                districts.get(BORDER).get(LEFT).endInBlockX(),
                districts.get(BORDER).get(UP).endInBlockY()));
        districts.get(PADDING).set(UP,new Zone(config.metaLength.get(District.LEFT)+config.mainWidth+config.metaLength.get(District.RIGHT),
                config.paddingLength.get(District.UP),
                districts.get(PADDING).get(LEFT_UP).endInBlockX(),
                districts.get(PADDING).get(LEFT_UP).startInBlockY()));
        districts.get(PADDING).set(LEFT,new Zone(config.paddingLength.get(District.LEFT),
                config.metaLength.get(District.UP)+config.mainHeight+config.metaLength.get(District.DOWN),
                districts.get(PADDING).get(LEFT_UP).startInBlockX(),
                districts.get(PADDING).get(LEFT_UP).endInBlockY()));
        districts.get(PADDING).set(LEFT_DOWN,new Zone(config.paddingLength.get(District.LEFT),
                config.paddingLength.get(District.DOWN),
                districts.get(PADDING).get(LEFT).startInBlockX(),
                districts.get(PADDING).get(LEFT).endInBlockY()));
        districts.get(PADDING).set(DOWN,new Zone(districts.get(PADDING).get(UP).widthInBlock,
                config.paddingLength.get(District.DOWN),
                districts.get(PADDING).get(LEFT_DOWN).endInBlockX(),
                districts.get(PADDING).get(LEFT_DOWN).startInBlockY()));
        districts.get(PADDING).set(RIGHT_UP,new Zone(config.paddingLength.get(District.RIGHT),
                config.paddingLength.get(District.UP),
                districts.get(PADDING).get(UP).endInBlockX(),
                districts.get(PADDING).get(UP).startInBlockY()));
        districts.get(PADDING).set(RIGHT,new Zone(config.paddingLength.get(District.RIGHT),
                districts.get(PADDING).get(LEFT).heightInBlock,
                districts.get(PADDING).get(RIGHT_UP).startInBlockX(),
                districts.get(PADDING).get(RIGHT_UP).endInBlockY()));
        districts.get(PADDING).set(RIGHT_DOWN,new Zone(config.paddingLength.get(District.RIGHT),
                config.paddingLength.get(District.DOWN),
                districts.get(PADDING).get(RIGHT).startInBlockX(),
                districts.get(PADDING).get(RIGHT).endInBlockY()));

        districts.get(META).set(LEFT_UP,new Zone(config.metaLength.get(District.LEFT),
                config.metaLength.get(District.UP),
                districts.get(PADDING).get(LEFT).endInBlockX(),
                districts.get(PADDING).get(UP).endInBlockY()));
        districts.get(META).set(UP,new Zone(config.mainWidth,
                config.metaLength.get(District.UP),
                districts.get(META).get(LEFT_UP).endInBlockX(),
                districts.get(META).get(LEFT_UP).startInBlockY()));
        districts.get(META).set(LEFT,new Zone(config.metaLength.get(District.LEFT),
                config.mainHeight,
                districts.get(META).get(LEFT_UP).startInBlockX(),
                districts.get(META).get(LEFT_UP).endInBlockY()));
        districts.get(META).set(LEFT_DOWN,new Zone(config.metaLength.get(District.LEFT),
                config.metaLength.get(District.DOWN),
                districts.get(META).get(LEFT).startInBlockX(),
                districts.get(META).get(LEFT).endInBlockY()));
        districts.get(META).set(DOWN,new Zone(districts.get(PADDING).get(UP).widthInBlock,
                config.metaLength.get(District.DOWN),
                districts.get(META).get(LEFT_DOWN).endInBlockX(),
                districts.get(META).get(LEFT_DOWN).startInBlockY()));
        districts.get(META).set(RIGHT_UP,new Zone(config.metaLength.get(District.RIGHT),
                config.metaLength.get(District.UP),
                districts.get(META).get(UP).endInBlockX(),
                districts.get(META).get(UP).startInBlockY()));
        districts.get(META).set(RIGHT,new Zone(config.metaLength.get(District.RIGHT),
                districts.get(META).get(LEFT).heightInBlock,
                districts.get(META).get(RIGHT_UP).startInBlockX(),
                districts.get(META).get(RIGHT_UP).endInBlockY()));
        districts.get(META).set(RIGHT_DOWN,new Zone(config.metaLength.get(District.RIGHT),
                config.metaLength.get(District.DOWN),
                districts.get(META).get(RIGHT).startInBlockX(),
                districts.get(META).get(RIGHT).endInBlockY()));

        districts.get(MAIN_DISTRICT).set(MAIN_ZONE,new Zone(config.mainWidth,
                config.mainHeight,
                districts.get(META).get(LEFT).endInBlockX(),
                districts.get(META).get(UP).endInBlockY()));

        int[] parts=new int[]{District.LEFT,District.UP,District.RIGHT,District.DOWN,
                District.LEFT_UP,District.RIGHT_UP,District.RIGHT_DOWN,District.LEFT_DOWN};
        for(int part:parts){
            districts.get(Districts.MARGIN).get(part).addBlock(config.marginBlock.get(part));
            districts.get(Districts.BORDER).get(part).addBlock(config.borderBlock.get(part));
            districts.get(Districts.PADDING).get(part).addBlock(config.paddingBlock.get(part));
            districts.get(Districts.META).get(part).addBlock(config.metaBlock.get(part));

            districts.get(Districts.MARGIN).get(part).addContent(config.marginContent.get(part));
            districts.get(Districts.BORDER).get(part).addContent(config.borderContent.get(part));
            districts.get(Districts.PADDING).get(part).addContent(config.paddingContent.get(part));
            districts.get(Districts.META).get(part).addContent(config.metaContent.get(part));
        }
        districts.get(Districts.MAIN).get(District.MAIN).addBlock(config.mainBlock.get(District.MAIN));
    }
    public Image toImage(int imageType){
        int blockLengthInPixels=config.blockLengthInPixel;
        Image image;
        switch (imageType){
            case 0:
                image=new ImageRGB(districts.get(Districts.MARGIN).get(District.RIGHT).endInBlockX()*blockLengthInPixels,districts.get(Districts.MARGIN).get(District.DOWN).endInBlockY()*blockLengthInPixels);
                break;
            case 1:
                image=new ImageYUV(districts.get(Districts.MARGIN).get(District.RIGHT).endInBlockX()*blockLengthInPixels,districts.get(Districts.MARGIN).get(District.DOWN).endInBlockY()*blockLengthInPixels);
                break;
            default:
                throw new IllegalArgumentException();
        }

        Iterator<District> districtItr=districts.iterator();
        while(districtItr.hasNext()){
            District district=districtItr.next();
            Iterator<Zone> zoneItr=district.iterator();
            while(zoneItr.hasNext()){
                Zone zone=zoneItr.next();
                if(zone!=null&&zone.getContent()!=null){
                    zone.toImage(image,blockLengthInPixels,index);
                }
            }
        }
        return image;
    }
}
