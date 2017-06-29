import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fec.openrq.parameters.FECParameters;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhantong on 2017/5/24.
 */
public class BlackWhiteCode {
    public static final String KEY_SIZE_RS_ERROR_CORRECTION="SIZE_RS_ERROR_CORRECTION";
    public static final String KEY_LEVEL_RS_ERROR_CORRECTION="LEVEL_RS_ERROR_CORRECTION";
    public static final String KEY_NUMBER_RAPTORQ_SOURCE_BLOCKS="NUMBER_RAPTORQ_SOURCE_BLOCKS";
    public static final String KEY_PERCENT_RAPTORQ_REDUNDANT="PERCENT_RAPTORQ_REDUNDANT";
    public static final String KEY_IS_REPLACE_LAST_RAPTORQ_SOURCE_PACKET_AS_REPAIR="IS_REPLACE_LAST_RAPTORQ_SOURCE_PACKET_AS_REPAIR";
    BarcodeConfig config;
    int inputFileSizeInByte=0;
    JsonObject jsonRoot=new JsonObject();
    public static void main(String[] args){
        BlackWhiteCode blackWhiteCode=new BlackWhiteCode(new BlackWhiteCodeConfig());
        blackWhiteCode.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/BlackWhiteCode");
        blackWhiteCode.saveJsonToFile("out.json");
    }
    public BlackWhiteCode(BarcodeConfig config){
        this.config=config;
        jsonRoot.add("barcodeConfig",config.toJson());
        BitSet rightBarBitSet=new BitSet();
        for(int i=0;i<config.mainHeight;i+=2){
            rightBarBitSet.set(i);
        }
        BitContent rightBarContent=new BitContent(rightBarBitSet);

        config.borderContent.set(District.RIGHT,rightBarContent);
    }
    protected void toImages(String inputFilePath,String outputDirectoryPath){
        int rSEcSize=Integer.parseInt(config.hints.get(KEY_SIZE_RS_ERROR_CORRECTION).toString());
        float rSEcLevel=Float.parseFloat(config.hints.get(KEY_LEVEL_RS_ERROR_CORRECTION).toString());
        int NUMBER_OF_SOURCE_BLOCKS=Integer.parseInt(config.hints.get(KEY_NUMBER_RAPTORQ_SOURCE_BLOCKS).toString());
        float raptorQRedundancy=Float.parseFloat(config.hints.get(KEY_PERCENT_RAPTORQ_REDUNDANT).toString());
        boolean isReplaceLastSourcePacketAsRepair=Boolean.parseBoolean(config.hints.get(KEY_IS_REPLACE_LAST_RAPTORQ_SOURCE_PACKET_AS_REPAIR).toString());

        int numRS=calcNumRS(config.mainWidth,config.mainHeight,config.mainBlock.get(District.MAIN).getBitsPerUnit(),rSEcSize);
        int numRSEc= calcNumRSEc(numRS,rSEcLevel);
        int numRSData=calcNumRSData(numRS,numRSEc);
        byte[] inputFileArray=getInputFileBytes(inputFilePath);

        JsonObject fileJsonRoot=new JsonObject();
        fileJsonRoot.addProperty("path",inputFilePath);
        fileJsonRoot.addProperty("sha1",FileVerification.bytesToSHA1(inputFileArray));
        jsonRoot.add("file",fileJsonRoot);

        configureTopBar(config,inputFileArray.length);
        inputFileSizeInByte=inputFileArray.length;

        int numDataBytes = calcRaptorQSymbolSize(calcRaptorQPacketSize(numRSData,rSEcSize));
        FECParameters parameters = FECParameters.newParameters(inputFileArray.length, numDataBytes, NUMBER_OF_SOURCE_BLOCKS);
        jsonRoot.add("fecParameters",Utils.fecParametersToJson(parameters));
        System.out.println("fecParameters: "+parameters.toString());
        List<byte[]> raptorQ=raptorQEncode(inputFileArray,parameters,raptorQRedundancy,isReplaceLastSourcePacketAsRepair);
        List<int[]> rS=Utils.rSEncode(raptorQ,numRSEc,rSEcSize);
        List<BitSet> rSBitSet=intArrayListToBitSetList(rS,rSEcSize);
        jsonRoot.add("barcodeValues",bitSetListToJson(rSBitSet));
        bitSetListToImages(rSBitSet,outputDirectoryPath,config);
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
    protected byte[] getInputFileBytes(String inputFilePath){
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
    void saveJsonToFile(String filePath){
        Utils.saveJsonToFile(jsonRoot,filePath);
    }
    JsonElement bitSetListToJson(List<BitSet> bitSetList){
        List<int[]> outputIntList=new ArrayList<>();
        for(BitSet bitSet:bitSetList){
            outputIntList.add(Utils.bitSetToIntArray(bitSet,config.mainWidth*config.mainHeight*config.mainBlock.get(District.MAIN).getBitsPerUnit(),config.mainBlock.get(District.MAIN).getBitsPerUnit()));
        }
        return new Gson().toJsonTree(outputIntList);
    }
}
