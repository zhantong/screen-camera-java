import net.fec.openrq.parameters.FECParameters;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhantong on 2017/5/24.
 */
public class BlackWhiteCode {
    BarcodeConfig config;
    Map<EncodeHintType,?> hints;
    int inputFileSizeInByte=0;
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.1);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        BlackWhiteCode blackWhiteCode=new BlackWhiteCode(new BlackWhiteCodeConfig(),hints);
        blackWhiteCode.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/BlackWhiteCode");
    }
    public BlackWhiteCode(BarcodeConfig config,Map<EncodeHintType,?> hints){
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
        int numRS=calcNumRS(config.mainWidth,config.mainHeight,config.mainBlock.get(District.MAIN).getBitsPerUnit(),rSEcSize);
        int numRSEc= calcNumRSEc(numRS,rSEcLevel);
        int numRSData=calcNumRSData(numRS,numRSEc);
        byte[] inputFileArray=getInputFileBytes(inputFilePath);

        configureTopBar(config,inputFileArray.length);
        inputFileSizeInByte=inputFileArray.length;

        int numDataBytes = calcRaptorQSymbolSize(calcRaptorQPacketSize(numRSData,rSEcSize));
        FECParameters parameters = FECParameters.newParameters(inputFileArray.length, numDataBytes, NUMBER_OF_SOURCE_BLOCKS);
        System.out.println("FECParameters: "+parameters.toString());
        List<byte[]> raptorQ=raptorQEncode(inputFileArray,parameters,raptorQRedundancy,isReplaceLastSourcePacketAsRepair);
        List<int[]> rS=Utils.rSEncode(raptorQ,numRSEc,rSEcSize);
        List<BitSet> rSBitSet=intArrayListToBitSetList(rS,rSEcSize);
        saveBitSetList(rSBitSet);
        bitSetListToImages(rSBitSet,outputDirectoryPath,config);
    }
    protected void saveBitSetList(List<BitSet> bitSetList){
        Utils.writeObjectToFile(bitSetList,"out.txt");
    }
    protected void bitSetListToImages(List<BitSet> dataList,String outputDirectoryPath,BarcodeConfig config){
        for(int i=0;i<dataList.size();i++){
            BitSet dataBitSet=dataList.get(i);
            BitContent dataContent=new BitContent(dataBitSet);
            reconfigure(config,i);
            Barcode barcode=new Barcode(i,config);
            barcode.districts.get(Districts.MAIN).get(District.MAIN).addContent(dataContent);
            //Image image=barcode.toImage(0);
            Image image=barcode.toImage(1);
            try {
                image.save(i,outputDirectoryPath,ImageYUV.TYPE_YUV420);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    protected List<BitSet> intArrayListToBitSetList(List<int[]> dataList,int bitsPerInt){
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
    protected BarcodeConfig configureTopBar(BarcodeConfig config,int data){
        BitSet topBarBitSet=Utils.intWithCRC8Checksum(data);
        BitContent topBarContent=new BitContent(topBarBitSet);
        config.borderContent.set(District.UP,topBarContent);
        return config;
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
    protected static int calcNumRSEc(int numRS, float rSEcLevel){
        return (int)Math.floor(numRS*rSEcLevel);
    }
    protected static int calcNumRS(int width,int height,int bitsPerUnit,int rSEcSize){
        return bitsPerUnit*width*height/rSEcSize;
    }
    protected static int calcNumRSData(int numRS,int numRSEc){
        return numRS-numRSEc;
    }
    protected static int calcRaptorQPacketSize(int numRSData,int rSEcSize){
        return numRSData*rSEcSize/8;
    }
    protected static int calcRaptorQSymbolSize(int raptorQPacketSize){
        return raptorQPacketSize-8;
    }
}
