import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Created by zhantong on 2017/5/10.
 */
public class BlackWhiteCodeML extends BlackWhiteCodeWithBar{
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.1);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        hints.put(EncodeHintType.NUMBER_OF_RANDOM_BARCODES,100);
        BlackWhiteCodeML blackWhiteCodeML=new BlackWhiteCodeML(new BlackWhiteCodeMLConfig(),hints);
        blackWhiteCodeML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/BlackWhiteCodeML");
    }
    public BlackWhiteCodeML(BarcodeConfig config, Map<EncodeHintType, ?> hints) {
        super(config, hints);
    }
    @Override
    protected void saveBitSetList(List<BitSet> bitSetList) {
        List<int[]> outputIntList=new ArrayList<>();
        for(BitSet bitSet:bitSetList){
            outputIntList.add(Utils.bitSetToIntArray(bitSet,config.mainWidth*config.mainHeight*config.mainBlock.get(District.MAIN).getBitsPerUnit(),config.mainBlock.get(District.MAIN).getBitsPerUnit()));
        }
        Gson gson=new Gson();
        JsonObject root=new JsonObject();
        root.add("values",gson.toJsonTree(outputIntList));
        try(Writer writer=new FileWriter("out.txt")) {
            gson.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected List<BitSet> intArrayListToBitSetList(List<int[]> dataList,int bitsPerInt){
        List<BitSet> bitSetList=super.intArrayListToBitSetList(dataList,bitsPerInt);
        int numRandomBarcode=100;
        if(hints!=null){
            if(hints.containsKey(EncodeHintType.NUMBER_OF_RANDOM_BARCODES)){
                numRandomBarcode=Integer.parseInt(hints.get(EncodeHintType.NUMBER_OF_RANDOM_BARCODES).toString());
            }
        }
        List<BitSet> randomBitSetList=Utils.randomBitSetList(config.mainWidth*config.mainHeight*config.mainBlock.get(District.MAIN).getBitsPerUnit(),numRandomBarcode,0);
        bitSetList.addAll(0,randomBitSetList);
        return bitSetList;
    }
    protected BarcodeConfig reconfigure(BarcodeConfig config,int barcodeIndex){
        super.reconfigure(config,barcodeIndex);
        int numRandomBarcode=100;
        if(hints!=null){
            if(hints.containsKey(EncodeHintType.NUMBER_OF_RANDOM_BARCODES)){
                numRandomBarcode=Integer.parseInt(hints.get(EncodeHintType.NUMBER_OF_RANDOM_BARCODES).toString());
            }
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
}
