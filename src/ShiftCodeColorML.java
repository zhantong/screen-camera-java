import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by zhantong on 2017/3/20.
 */
public class ShiftCodeColorML extends BlackWhiteCodeML {
    public static void main(String[] args){
        ShiftCodeColorML shiftCodeColorML=new ShiftCodeColorML(new ShiftCodeColorMLConfig());
        shiftCodeColorML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/ShiftCodeColorML");
    }
    public ShiftCodeColorML(BarcodeConfig config) {
        super(config);

        BitSet rightBarBitSet=new BitSet();
        for(int i=0;i<config.mainHeight;i+=2){
            rightBarBitSet.set(i);
        }
        BitContent rightBarContent=new BitContent(rightBarBitSet);

        config.metaContent.set(District.RIGHT,rightBarContent);
    }
    protected BarcodeConfig reconfigure(BarcodeConfig config,int barcodeIndex){
        config=super.reconfigure(config,barcodeIndex);
              if(barcodeIndex%2==0) {
            BitSet leftBarBitSet=new BitSet();
            leftBarBitSet.set(0);
            leftBarBitSet.set(config.mainHeight-1);
            BitContent leftBarContentEven=new BitContent(leftBarBitSet);
            config.metaContent.set(District.LEFT, leftBarContentEven);
        }else{
            BitContent leftBarContentOdd=new BitContent(BitContent.ALL_ZEROS);
            config.metaContent.set(District.LEFT, leftBarContentOdd);
        }
        return config;
    }
}
