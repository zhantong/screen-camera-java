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


        BarCode barCode=new BarCode();
        Image image=new Image(20*blockLengthInPixels,20*blockLengthInPixels, BufferedImage.TYPE_INT_RGB);
        barCode.districts.margin.left=new Zone(1,20,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,0,0);
        barCode.districts.margin.left.fillZone(image,ones);
        barCode.districts.margin.up=new Zone(20,1,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,0,0);
        barCode.districts.margin.up.fillZone(image,ones);
        barCode.districts.margin.right=new Zone(1,20,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,19,0);
        barCode.districts.margin.right.fillZone(image,ones);
        barCode.districts.margin.down=new Zone(20,1,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,0,19);
        barCode.districts.margin.down.fillZone(image,ones);

        barCode.districts.border.left=new Zone(1,18,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,1,1);
        barCode.districts.border.left.fillZone(image,varys);
        barCode.districts.border.up=new Zone(18,1,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,1,1);
        barCode.districts.border.up.fillZone(image,zeros);
        barCode.districts.border.right=new Zone(1,18,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,18,1);
        barCode.districts.border.right.fillZone(image,varys);
        barCode.districts.border.down=new Zone(18,1,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,1,18);
        barCode.districts.border.down.fillZone(image,zeros);

        barCode.districts.main=new Zone(16,16,new BlackWhiteBlock(),blockLengthInPixels,blockLengthInPixels,2,2);
        barCode.districts.main.fillZone(image,data);
        try {
            image.save("png","test.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public BarCode(){
        districts=new Districts();
    }
}
