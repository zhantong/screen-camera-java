import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fec.openrq.parameters.FECParameters;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * Created by zhantong on 2017/5/10.
 */
public class BlackWhiteCodeML {
    BarcodeConfig config;
    Map<EncodeHintType,?> hints;
    int inputFileSizeInByte=0;
    private int numRandomBarcode=100;
    boolean saveBitSetList=true;
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.1);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        BlackWhiteCodeML blackWhiteCodeML=new BlackWhiteCodeML(new BlackWhiteCodeMLConfig(),hints);
        blackWhiteCodeML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample3.txt","/Users/zhantong/Desktop/BlackWhiteCodeML_100x100_0.1");
    }
    public BlackWhiteCodeML(BarcodeConfig config,Map<EncodeHintType,?> hints){
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
        List<int[]> rS=reedSolomonEncode(raptorQ,rSEcSize,numRSEc);
        List<BitSet> rSBitSet=intArrayListToBitSetList(rS,rSEcSize);
        if(saveBitSetList){
            List<int[]> outputIntList=new ArrayList<>();
            for(BitSet bitSet:rSBitSet){
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
        List<BitSet> randomBitSetList=Utils.randomBitSetList(config.mainWidth*config.mainHeight*config.mainBlock.get(District.MAIN).getBitsPerUnit(),numRandomBarcode,0);
        bitSetList.addAll(0,randomBitSetList);
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
    protected BarcodeConfig configureTopBar(BarcodeConfig config,int data){
        BitSet topBarBitSet=Utils.intWithCRC8Checksum(data);
        BitContent topBarContent=new BitContent(topBarBitSet);
        config.borderContent.set(District.UP,topBarContent);
        return config;
    }
    protected BarcodeConfig reconfigure(BarcodeConfig config,int barcodeIndex){
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
            config.borderContent.set(District.LEFT, leftBarContentEven);

            config.paddingContent.set(District.LEFT, paddingBarWhiteBlackContent);
            config.paddingContent.set(District.RIGHT, paddingBarWhiteBlackContent);
        }else{
            BitContent leftBarContentOdd=new BitContent(BitContent.ALL_ZEROS);
            config.borderContent.set(District.LEFT, leftBarContentOdd);

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
