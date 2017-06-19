import java.io.IOException;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhantong on 2016/12/2.
 */
public class ShiftCodeColor extends ShiftCode {
    public static void main(String[] args){
        ShiftCodeColor shiftCodeColor=new ShiftCodeColor(new ShiftCodeColorConfig());
        shiftCodeColor.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/ShiftCodeColor");
    }
    public ShiftCodeColor(BarcodeConfig config) {
        super(config);
        BitSet rightBarBitSet=new BitSet();
        for(int i=0;i<config.mainHeight;i+=2){
            rightBarBitSet.set(i);
        }
        BitContent rightBarContent=new BitContent(rightBarBitSet);

        config.paddingContent.set(District.RIGHT,rightBarContent);
        config.borderContent.set(District.RIGHT,new BitContent(BitContent.ALL_ZEROS));
    }
    protected BarcodeConfig configureTopBar(BarcodeConfig config,int data){
        BitSet topBarBitSet=Utils.intWithCRC8Checksum(data);
        BitContent topBarContent=new BitContent(topBarBitSet);
        config.paddingContent.set(District.UP,topBarContent);
        return config;
    }
    protected BarcodeConfig reconfigure(BarcodeConfig config,int barcodeIndex){
        if(barcodeIndex%2==0) {
            BitSet leftBarBitSet=new BitSet();
            leftBarBitSet.set(0);
            leftBarBitSet.set(config.mainHeight-1);
            BitContent leftBarContentEven=new BitContent(leftBarBitSet);
            config.paddingContent.set(District.LEFT, leftBarContentEven);
        }else{
            BitContent leftBarContentOdd=new BitContent(BitContent.ALL_ZEROS);
            config.paddingContent.set(District.LEFT, leftBarContentOdd);
        }
        return config;
    }
}
