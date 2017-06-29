import com.google.gson.JsonObject;

import java.util.*;

/**
 * Created by zhantong on 2017/6/3.
 */
public class RDCodeML extends BlackWhiteCodeML{
    RDCodeMLConfig config;

    public static void main(String[] args){
        RDCodeML rDCodeML =new RDCodeML(new RDCodeMLConfig());
        rDCodeML.toImages("/Volumes/扩展存储/实验/原始文件/sample12.txt","/Volumes/扩展存储/实验/RDCodeML/180x108_0.1/1x");
        rDCodeML.saveJsonToFile("out.json");
    }
    public RDCodeML(RDCodeMLConfig config) {
        super(config);
        this.config=config;
    }
    private void buildCenterRegion(Region region,int currentWindow,int currentFrame,int numFileBytes){
        region.data[0]=currentWindow;
        region.data[1]=currentFrame;
        region.data[2]=numFileBytes>>24;
        region.data[3]=(numFileBytes>>16)&0xff;
        region.data[4]=(numFileBytes>>8)&0xff;
        region.data[5]=numFileBytes&0xff;
        Utils.rSEncode(region.data,12,8,true);
    }
    protected void toImages(String inputFilePath,String outputDirectoryPath){
        byte[] inputFileArray=getInputFileBytes(inputFilePath);

        JsonObject fileJsonRoot=new JsonObject();
        fileJsonRoot.addProperty("path",inputFilePath);
        fileJsonRoot.addProperty("sha1",FileVerification.bytesToSHA1(inputFileArray));
        jsonRoot.add("file",fileJsonRoot);

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
        int numRSBytes=4;
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

        List<int[]> rSEncoded=new ArrayList<>();

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
                    buildCenterRegion( window.frames[currentFrame].regions[currentRegion],currentWindow,currentFrame,inputFileArray.length);
                    currentRegion++;
                }else{
                    if(currentRegion==numRegions){
                        currentRegion=0;
                        currentFrame++;
                    }
                    if(currentFrame==indexLastFrame){
                        while(currentRegion<numRegions){
                            if(currentRegion==indexCenterBlock){
                                buildCenterRegion( window.frames[currentFrame].regions[currentRegion],currentWindow,currentFrame,inputFileArray.length);
                                currentRegion++;
                            }else{
                                Utils.rSEncode(window.frames[currentFrame].regions[currentRegion].data,numRSBytes,8,true);
                                currentRegion++;
                            }
                        }
                        System.out.println("window "+currentWindow+": ");
                        System.out.println(window);
                        for(Frame frame:window.frames){
                            int numBytesPerFrame=frame.regions.length*frame.regions[0].data.length;
                            int numBytesPerRegionLine=config.mainBlock.get(District.MAIN).getBitsPerUnit()*config.regionWidth/8;
                            int numBytesPerRegion=numBytesPerRegionLine*config.regionHeight;
                            if(numBytesPerFrame!=numBytesPerRegion*config.numRegionVertical*config.numRegionHorizon){
                                throw new IllegalArgumentException();
                            }
                            int[] frameData=new int[numBytesPerFrame];
                            int frameDataPos=0;
                            for(int indexRegionOffset=0;indexRegionOffset<numRegions;indexRegionOffset+=config.numRegionHorizon){
                                for(int pos=0;pos<numBytesPerRegion;pos+=numBytesPerRegionLine){
                                    for(int indexRegionInLine=0;indexRegionInLine<config.numRegionHorizon;indexRegionInLine++){
                                        int indexRegion=indexRegionOffset+indexRegionInLine;
                                        System.arraycopy(frame.regions[indexRegion].data,pos,frameData,frameDataPos,numBytesPerRegionLine);
                                        frameDataPos+=numBytesPerRegionLine;
                                    }
                                }
                            }
                            rSEncoded.add(frameData);
                            System.out.println(Arrays.toString(frameData));
                        }
                        currentFrame=0;
                        currentRegion=0;
                        window=new Window(8,numRegions,numRegionBytes);
                        currentWindow++;
                    }
                }
            }
        }


        configureTopBar(config,inputFileArray.length);
        inputFileSizeInByte=inputFileArray.length;

        List<BitSet> rSBitSet=intArrayListToBitSetList(rSEncoded,8);
        jsonRoot.add("barcodeValues",bitSetListToJson(rSBitSet));
        bitSetListToImages(rSBitSet,outputDirectoryPath,config);
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
