import java.util.*;

/**
 * Created by zhantong on 2017/5/10.
 */
public class BlackWhiteCodeML extends BlackWhiteCodeWithBar{
    public static final String KEY_NUMBER_RANDOM_BARCODES="NUMBER_RANDOM_BARCODES";
    public static void main(String[] args){
        BlackWhiteCodeML blackWhiteCodeML=new BlackWhiteCodeML(new BlackWhiteCodeMLConfig());
        blackWhiteCodeML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/BlackWhiteCodeML");
        blackWhiteCodeML.saveJsonToFile("out.json");
    }
    public BlackWhiteCodeML(BarcodeConfig config) {
        super(config);
    }
    protected List<BitSet> intArrayListToBitSetList(List<int[]> dataList,int bitsPerInt){
        List<BitSet> bitSetList=super.intArrayListToBitSetList(dataList,bitsPerInt);
        int numRandomBarcode=Integer.parseInt(config.hints.get(KEY_NUMBER_RANDOM_BARCODES).toString());
        List<BitSet> randomBitSetList=Utils.randomBitSetList(config.mainWidth*config.mainHeight*config.mainBlock.get(District.MAIN).getBitsPerUnit(),numRandomBarcode,0);
        bitSetList.addAll(0,randomBitSetList);
        return bitSetList;
    }
    protected BarcodeConfig reconfigure(BarcodeConfig config,int barcodeIndex){
        super.reconfigure(config,barcodeIndex);
        int numRandomBarcode=Integer.parseInt(config.hints.get(KEY_NUMBER_RANDOM_BARCODES).toString());
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
}
