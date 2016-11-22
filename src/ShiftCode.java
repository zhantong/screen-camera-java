import net.fec.openrq.parameters.FECParameters;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhantong on 2016/11/19.
 */
public class ShiftCode {
    BarcodeConfig config;
    Map<EncodeHintType,?> hints;
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.2);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        ShiftCode shiftCode=new ShiftCode(new ShiftCodeConfig(),hints);
        shiftCode.toImages("/Users/zhantong/Desktop/phpinfo.txt","/Users/zhantong/Desktop/out");
    }
    public ShiftCode(BarcodeConfig config,Map<EncodeHintType,?> hints){
        this.config=config;
        this.hints=hints;

        BitSet rightBarBitSet=new BitSet();
        for(int i=0;i<config.mainHeight;i+=2){
            rightBarBitSet.set(i);
        }
        BitContent rightBarContent=new BitContent(rightBarBitSet);

        config.borderContent.set(District.RIGHT,rightBarContent);
    }
    protected void toImages(String inputFilePath,String outputDirectoryPath){
        int rSEcSize=12;
        float rSEcLevel=0.2f;
        int NUMBER_OF_SOURCE_BLOCKS=1;
        float raptorQRedundancy=0.5f;
        boolean isReplaceLastSourcePacketAsRepair=true;
        if(hints!=null){
            if(hints.containsKey(EncodeHintType.RS_ERROR_CORRECTION_SIZE)){
                rSEcSize=Integer.parseInt(hints.get(EncodeHintType.RS_ERROR_CORRECTION_SIZE).toString());
            }
            if(hints.containsKey(EncodeHintType.RS_ERROR_CORRECTION_LEVEL)){
                rSEcLevel=Float.parseFloat(hints.get(EncodeHintType.RS_ERROR_CORRECTION_LEVEL).toString());
            }
            if(hints.containsKey(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS)){
                NUMBER_OF_SOURCE_BLOCKS=Integer.parseInt(hints.get(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS).toString());
            }
            if(hints.containsKey(EncodeHintType.RAPTORQ_REDUNDANT_PERCENT)){
                raptorQRedundancy=Float.parseFloat(hints.get(EncodeHintType.RAPTORQ_REDUNDANT_PERCENT).toString());
            }
            if(hints.containsKey(EncodeHintType.RAPTORQ_REPLACE_LAST_SOURCE_PACKET_AS_REPAIR)){
                isReplaceLastSourcePacketAsRepair=Boolean.parseBoolean(hints.get(EncodeHintType.RAPTORQ_REPLACE_LAST_SOURCE_PACKET_AS_REPAIR).toString());
            }
        }
        int numRSEc=calcEcNum(config.mainWidth,config.mainHeight,config.mainBlock.get(District.MAIN).getBitsPerUnit(),rSEcSize,rSEcLevel);
        byte[] inputFileArray=getInputFileBytes(inputFilePath);

        configureTopBar(config,inputFileArray.length);

        int numDataBytes = calcNumDataBytes(config.mainWidth,config.mainHeight,config.mainBlock.get(District.MAIN).getBitsPerUnit(),rSEcSize,numRSEc);
        FECParameters parameters = FECParameters.newParameters(inputFileArray.length, numDataBytes, NUMBER_OF_SOURCE_BLOCKS);
        List<byte[]> raptorQ=raptorQEncode(inputFileArray,parameters,raptorQRedundancy,isReplaceLastSourcePacketAsRepair);
        List<int[]> rS=reedSolomonEncode(raptorQ,rSEcSize,numRSEc);
        List<BitSet> rSBitSet=intArrayListToBitSetList(rS,rSEcSize);
        bitSetListToImages(rSBitSet,outputDirectoryPath,config);
    }
    private void bitSetListToImages(List<BitSet> dataList,String outputDirectoryPath,BarcodeConfig config){
        for(int i=0;i<dataList.size();i++){
            BitSet dataBitSet=dataList.get(i);
            BitContent dataContent=new BitContent(dataBitSet);
            reconfigure(config,i);
            Barcode barcode=new Barcode(i,config);
            barcode.districts.get(Districts.MAIN).get(District.MAIN).addContent(dataContent);
            Image image=barcode.toImage();
            String path=Utils.combinePaths(outputDirectoryPath,String.format("%06d.png", i));
            try {
                image.save("png",path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private List<BitSet> intArrayListToBitSetList(List<int[]> dataList,int bitsPerInt){
        List<BitSet> bitSetList=new LinkedList<>();

        for(int[] data:dataList){
            BitSet dataBitSet=Utils.intArrayToBitSet(data,bitsPerInt);
            bitSetList.add(dataBitSet);

/*            byte[] converted=Utils.intArrayToByteArray(data,rSEcSize);
            BitSet dataBitSet=BitSet.valueOf(converted);
            rSBitSet.add(dataBitSet);*/
        }
        return bitSetList;
    }
    private List<int[]> reedSolomonEncode(List<byte[]> dataList,int ecSize,int numEc){
        List<int[]> encodedList=new LinkedList<>();
        for(byte[] data:dataList){
            int[] converted=Utils.byteArrayToIntArray(data,ecSize);
            int[] encoded=Utils.rSEncode(converted,numEc,ecSize);
            encodedList.add(encoded);
        }
        return encodedList;
    }
    private List<byte[]> raptorQEncode(byte[] array,FECParameters parameters,float redundancy,boolean isReplaceLastSourcePacketAsRepair){
        return Utils.raptorQEncode(array,parameters,redundancy,isReplaceLastSourcePacketAsRepair);
    }
    private byte[] getInputFileBytes(String inputFilePath){
        byte[] inputFileArray;
        try {
            inputFileArray=Utils.fileToByteArray(inputFilePath);
        } catch (IOException e) {
            throw new RuntimeException("input file "+inputFilePath+" not found");
        }
        return inputFileArray;
    }
    private BarcodeConfig configureTopBar(BarcodeConfig config,int data){
        BitSet topBarBitSet=Utils.intWithCRC8Checksum(data);
        BitContent topBarContent=new BitContent(topBarBitSet);
        config.borderContent.set(District.UP,topBarContent);
        return config;
    }
    private int calcNumDataBytes(int width,int height,int bitsPerUnit,int rSEcSize,int numRSEc){
        return bitsPerUnit*width*height / 8 - rSEcSize * numRSEc / 8 - 8;
    }
    protected BarcodeConfig reconfigure(BarcodeConfig config,int barcodeIndex){
        if(barcodeIndex%2==0) {
            BitSet leftBarBitSet=new BitSet();
            leftBarBitSet.set(0);
            leftBarBitSet.set(config.mainHeight-1);
            BitContent leftBarContentEven=new BitContent(leftBarBitSet);
            config.borderContent.set(District.LEFT, leftBarContentEven);
        }else{
            BitContent leftBarContentOdd=new BitContent(BitContent.ALL_ZEROS);
            config.borderContent.set(District.LEFT, leftBarContentOdd);
        }
        return config;
    }
    protected static int calcEcNum(int width,int height,int bitsPerUnit,int rSEcSize,float rSEcLevel){
        return ((int)((bitsPerUnit*width*height/rSEcSize)*rSEcLevel))/2*2;
    }
}
