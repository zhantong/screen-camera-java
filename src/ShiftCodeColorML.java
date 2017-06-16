import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by zhantong on 2017/3/20.
 */
public class ShiftCodeColorML extends BlackWhiteCodeML {
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.2);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        hints.put(EncodeHintType.NUMBER_OF_RANDOM_BARCODES,100);
        ShiftCodeColorML shiftCodeColorML=new ShiftCodeColorML(new ShiftCodeColorMLConfig(),hints);
        shiftCodeColorML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/ShiftCodeColorML");
    }
    public ShiftCodeColorML(BarcodeConfig config, Map<EncodeHintType, ?> hints) {
        super(config, hints);

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
