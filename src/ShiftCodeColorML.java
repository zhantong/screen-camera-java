import java.util.BitSet;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhantong on 2017/3/20.
 */
public class ShiftCodeColorML extends ShiftCode {
    private int numRandomBarcode=100;
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.2);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        ShiftCodeColorML shiftCodeColorML=new ShiftCodeColorML(new ShiftCodeColorMLConfig(),hints);
        shiftCodeColorML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample4.txt","/Users/zhantong/Desktop/ShiftCodeColorML3");
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
        BitSet paddingBarWhiteBlackBitSet=new BitSet();
        BitSet paddingBarBlackWhiteBitSet=new BitSet();
        for(int i=0;i<config.mainHeight*2;i+=2){
            paddingBarWhiteBlackBitSet.set(i);
            paddingBarBlackWhiteBitSet.set(i+1);
        }
        BitContent paddingBarWhiteBlackContent=new BitContent(paddingBarWhiteBlackBitSet);
        BitContent paddingBarBlackWhiteContent=new BitContent(paddingBarBlackWhiteBitSet);
        if(barcodeIndex%2==0) {
            BitSet leftBarBitSet=new BitSet();
            leftBarBitSet.set(0);
            leftBarBitSet.set(config.mainHeight-1);
            BitContent leftBarContentEven=new BitContent(leftBarBitSet);
            config.metaContent.set(District.LEFT, leftBarContentEven);

            config.paddingContent.set(District.LEFT, paddingBarWhiteBlackContent);
            config.paddingContent.set(District.RIGHT, paddingBarWhiteBlackContent);
        }else{
            BitContent leftBarContentOdd=new BitContent(BitContent.ALL_ZEROS);
            config.metaContent.set(District.LEFT, leftBarContentOdd);

            config.paddingContent.set(District.LEFT, paddingBarBlackWhiteContent);
            config.paddingContent.set(District.RIGHT, paddingBarBlackWhiteContent);
        }
        if(barcodeIndex<numRandomBarcode){
            BitSet bottomBarBitSet=new BitSet();
            bottomBarBitSet.set(2);
            BitContent bottomBarContent=new BitContent(bottomBarBitSet);
            config.borderContent.set(District.DOWN,bottomBarContent);

            BitSet topBarBitSet=Utils.reverse(Utils.intToBitSet(Utils.intToGrayCode(barcodeIndex),32),32);
            BitContent topBarContent=new BitContent(topBarBitSet);
            config.borderContent.set(District.UP,topBarContent);
        }else {
            config.borderContent.set(District.DOWN,new BitContent(BitContent.ALL_ZEROS));

            BitSet topBarBitSet=Utils.intWithCRC8Checksum(inputFileSizeInByte);
            BitContent topBarContent=new BitContent(topBarBitSet);
            config.borderContent.set(District.UP,topBarContent);
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
