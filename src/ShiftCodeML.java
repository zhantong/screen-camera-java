import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by zhantong on 2016/11/22.
 */
public class ShiftCodeML extends ShiftCode {
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.2);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        ShiftCodeML shiftCodeML=new ShiftCodeML(new ShiftCodeMLConfig(),hints);
        shiftCodeML.toImages("/Users/zhantong/Desktop/phpinfo.txt","/Users/zhantong/Desktop/out2");
    }
    public ShiftCodeML(BarcodeConfig config, Map<EncodeHintType, ?> hints) {
        super(config, hints);
    }
    protected BarcodeConfig reconfigure(BarcodeConfig config,int barcodeIndex){
        config=super.reconfigure(config,barcodeIndex);
        BitSet paddingBarWhiteBlackBitSet=new BitSet();
        BitSet paddingBarBlackWhiteBitSet=new BitSet();
        for(int i=0;i<config.mainHeight*2;i+=2){
            paddingBarWhiteBlackBitSet.set(i);
            paddingBarBlackWhiteBitSet.set(i+1);
        }
        BitContent paddingBarWhiteBlackContent=new BitContent(paddingBarWhiteBlackBitSet);
        BitContent paddingBarBlackWhiteContent=new BitContent(paddingBarBlackWhiteBitSet);
        if(barcodeIndex%2==0) {
            config.paddingContent.set(District.LEFT, paddingBarWhiteBlackContent);
            config.paddingContent.set(District.RIGHT, paddingBarWhiteBlackContent);
        }else{
            config.paddingContent.set(District.LEFT, paddingBarBlackWhiteContent);
            config.paddingContent.set(District.RIGHT, paddingBarBlackWhiteContent);
        }
        return config;
    }
}
