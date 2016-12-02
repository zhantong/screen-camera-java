import java.util.BitSet;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhantong on 2016/11/22.
 */
public class ShiftCodeML extends ShiftCode {
    private int numRandomBarcode=40;
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.1);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        ShiftCodeML shiftCodeML=new ShiftCodeML(new ShiftCodeMLConfig(),hints);
        shiftCodeML.toImages("/Users/zhantong/Downloads/sample1.txt","/Users/zhantong/Desktop/ShiftCodeML_40_0.1");
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
        if(barcodeIndex<numRandomBarcode){
            BitSet bottomBarBitSet=new BitSet();
            bottomBarBitSet.set(0);
            BitContent bottomBarContent=new BitContent(bottomBarBitSet);
            config.borderContent.set(District.DOWN,bottomBarContent);
        }else {
            config.borderContent.set(District.DOWN,new BitContent(BitContent.ALL_ZEROS));
        }
        return config;
    }
    protected List<BitSet> intArrayListToBitSetList(List<int[]> dataList, int bitsPerInt){
        List<BitSet> bitSetList=super.intArrayListToBitSetList(dataList,bitsPerInt);
        List<BitSet> randomBitSetList=Utils.randomBitSetList(config.mainWidth*config.mainHeight*config.mainBlock.get(District.MAIN).getBitsPerUnit(),numRandomBarcode,0);
        bitSetList.addAll(0,randomBitSetList);
        return bitSetList;
    }
}
