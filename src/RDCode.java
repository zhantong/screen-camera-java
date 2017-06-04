import net.fec.openrq.parameters.FECParameters;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhantong on 2017/6/3.
 */
public class RDCode {
    RDCodeConfig config;
    Map<EncodeHintType,?> hints;
    int inputFileSizeInByte=0;
    boolean saveBitSetList=true;
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,8);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.1);
        RDCode rDCode=new RDCode(new RDCodeConfig(),hints);
        rDCode.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample1.txt","/Users/zhantong/Desktop/RDCode1");
    }
    public RDCode(RDCodeConfig config,Map<EncodeHintType,?> hints){
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

        int numRegions=config.numRegionHorizon*config.numRegionVertical;
        int numParityRegions=3;
        int numDataRegions=numRegions-numParityRegions;
        int indexCenterBlock=numRegions/2;
        List<Integer>[] interRegionParity=new List[3];
        interRegionParity[0]=new ArrayList<>();
        for(int i=0;i<numRegions-2;i++){
            if(i!=indexCenterBlock&&((numRegions-i-3)%4<2)){
                interRegionParity[0].add(i);
            }
        }
        interRegionParity[1]=new ArrayList<>();
        for(int i=0;i<numRegions;i++){
            if(i!=indexCenterBlock&&(i%2==numRegions%2)){
                interRegionParity[1].add(i);
            }
        }
        interRegionParity[2]=new ArrayList<>();
        for(int i=0;i<numRegions;i++){
            if(i!=indexCenterBlock){
                interRegionParity[2].add(i);
            }
        }
        List<Integer>[] parityMap=new List[numRegions];
        for(int i=0;i<numRegions;i++){
            parityMap[i]=new ArrayList<>();
            for(int j=0;j<interRegionParity.length;j++){
                for(int k:interRegionParity[j]){
                    if(k==i){
                        parityMap[i].add(j);
                    }
                }
            }
        }
        int numRSBytes=6;
        int numColors=4;
        int numRegionBytes=(config.regionWidth*config.regionHeight)/(8/(int)Math.sqrt(numColors));
        int numRegionDataBytes=numRegionBytes-numRSBytes;
        Window window=new Window(8,numRegions,numRegionBytes);
        int numInterFrameParity=1;
        int indexLastFrame=window.frames.length-numInterFrameParity;
        int currentFrame=0;
        int currentRegion=0;
        int currentPosition=0;
        int currentWindow=0;

        Random random=new Random();
        int fileIndex=0;
        while(true){
            int data;
            if(fileIndex==inputFileArray.length){
                if(currentFrame==0&&currentRegion==0&&currentPosition==0){
                    break;
                }else{
                    data=random.nextInt(256);
                }
            }else{
                data=inputFileArray[fileIndex]&0xff;
                fileIndex++;
            }
            window.frames[currentFrame].regions[currentRegion].data[currentPosition]=data;
            window.frames[indexLastFrame].regions[currentRegion].data[currentPosition]^=data;
            for(int i:parityMap[currentRegion]){
                window.frames[currentFrame].regions[numDataRegions+i].data[currentPosition]^=data;
            }
            currentPosition++;
            if(currentPosition==numRegionDataBytes){
                Utils.rSEncode(window.frames[currentFrame].regions[currentRegion].data,numRSBytes,8,true);
                currentPosition=0;
                currentRegion++;
                for(int i=0;i<interRegionParity.length;i++){
                    if(interRegionParity[i].get(interRegionParity[i].size()-1)==currentRegion){
                        while(currentPosition<numRegionDataBytes){
                            int currentData=window.frames[currentFrame].regions[currentRegion].data[currentPosition];
                            window.frames[indexLastFrame].regions[currentRegion].data[currentPosition]^=currentData;
                            for(int j:parityMap[currentRegion]){
                                if(j!=i) {
                                    window.frames[currentFrame].regions[numDataRegions + j].data[currentPosition] ^= data;
                                }
                            }
                            currentPosition++;
                        }
                        Utils.rSEncode(window.frames[currentFrame].regions[currentRegion].data,numRSBytes,8,true);
                        currentPosition=0;
                        currentRegion++;
                    }
                }
                if(currentRegion==indexCenterBlock){
                    window.frames[currentFrame].regions[currentRegion].data[0]=currentWindow;
                    window.frames[currentFrame].regions[currentRegion].data[1]=currentFrame;
                    Utils.rSEncode(window.frames[currentFrame].regions[currentRegion].data,numRSBytes,8,true);
                    currentRegion++;
                }else{
                    if(currentRegion==numRegions){
                        currentRegion=0;
                        currentFrame++;
                    }
                    if(currentFrame==indexLastFrame){
                        while(currentRegion<numRegions){
                            if(currentRegion==indexCenterBlock){
                                window.frames[currentFrame].regions[currentRegion].data[0]=currentWindow;
                                window.frames[currentFrame].regions[currentRegion].data[1]=currentFrame;
                                Utils.rSEncode(window.frames[currentFrame].regions[currentRegion].data,numRSBytes,8,true);
                                currentRegion++;
                            }else{
                                Utils.rSEncode(window.frames[currentFrame].regions[currentRegion].data,numRSBytes,8,true);
                                currentRegion++;
                            }
                        }
                        System.out.println("window "+currentWindow+": ");
                        System.out.println(window);
                        currentFrame=0;
                        currentRegion=0;
                        window=new Window(8,numRegions,numRegionBytes);
                        currentWindow++;
                    }
                }
            }
        }


/*        configureTopBar(config,inputFileArray.length);
        inputFileSizeInByte=inputFileArray.length;

        int numDataBytes = calcRaptorQSymbolSize(calcRaptorQPacketSize(numRSData,rSEcSize));
        FECParameters parameters = FECParameters.newParameters(inputFileArray.length, numDataBytes, NUMBER_OF_SOURCE_BLOCKS);
        System.out.println("FECParameters: "+parameters.toString());
        List<byte[]> raptorQ=raptorQEncode(inputFileArray,parameters,raptorQRedundancy,isReplaceLastSourcePacketAsRepair);
        List<int[]> rS=reedSolomonEncode(raptorQ,rSEcSize,numRSEc);
        List<BitSet> rSBitSet=intArrayListToBitSetList(rS,rSEcSize);
        if(saveBitSetList){
            Utils.writeObjectToFile(rSBitSet,"out.txt");
        }
        bitSetListToImages(rSBitSet,outputDirectoryPath,config);*/
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
        config.borderContent.set(District.DOWN,new BitContent(BitContent.ALL_ZEROS));

        BitSet topBarBitSet=Utils.intWithCRC8Checksum(inputFileSizeInByte);
        BitContent topBarContent=new BitContent(topBarBitSet);
        config.borderContent.set(District.UP,topBarContent);
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
    class Window{
        Frame[] frames;
        public Window(int numFrames,int numRegionsPerFrame,int numRegionBytes){
            frames=new Frame[numFrames];
            for(int i=0;i<frames.length;i++){
                frames[i]=new Frame(numRegionsPerFrame,numRegionBytes);
            }
        }
        @Override
        public String toString() {
            StringBuilder builder=new StringBuilder();
            builder.append('{');
            builder.append('\n');
            for(int i=0;i<frames.length;i++){
                builder.append("frame ");
                builder.append(i);
                builder.append(": ");
                builder.append(frames[i].toString());
                builder.append('\n');
            }
            builder.append('}');
            return builder.toString();
        }
    }
    class Frame{
        Region[] regions;
        public Frame(int numRegions,int numRegionBytes){
            regions=new Region[numRegions];
            for(int i=0;i<regions.length;i++){
                regions[i]=new Region(numRegionBytes);
            }
        }

        @Override
        public String toString() {
            StringBuilder builder=new StringBuilder();
            builder.append('{');
            builder.append('\n');
            for(int i=0;i<regions.length;i++){
                builder.append("region ");
                builder.append(i);
                builder.append(": ");
                builder.append(regions[i].toString());
                builder.append('\n');
            }
            builder.append('}');
            return builder.toString();
        }
    }
    class Region{
        int[] data;
        public Region(int numBytes){
            data=new int[numBytes];
        }

        @Override
        public String toString() {
            return Arrays.toString(data);
        }
    }
}
