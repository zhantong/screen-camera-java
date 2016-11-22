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
    private void toImages(String inputFilePath,String outputDirectoryPath){
        int rSEcSize=12;
        float rSEcLevel=0.2f;
        int NUMBER_OF_SOURCE_BLOCKS=1;
        float raptorQRedundancy=0.5f;
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
        }
        int numRSEc=calcEcNum(config.mainWidth,config.mainHeight,config.mainBlock.get(District.MAIN).getBitsPerUnit(),rSEcSize,rSEcLevel);
        byte[] inputFileArray=new byte[1];
        try {
            inputFileArray=Utils.fileToByteArray(inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configureTopBar(config,inputFileArray.length);

        int numDataBytes = calcNumDataBytes(config.mainWidth,config.mainHeight,config.mainBlock.get(District.MAIN).getBitsPerUnit(),rSEcSize,numRSEc);
        FECParameters parameters = FECParameters.newParameters(inputFileArray.length, numDataBytes, NUMBER_OF_SOURCE_BLOCKS);
        List<byte[]> raptorQ=Utils.raptorQEncode(inputFileArray,parameters,raptorQRedundancy,true);
        List<int[]> rS=new LinkedList<>();
        for(byte[] data:raptorQ){
            int[] converted=Utils.byteArrayToIntArray(data,rSEcSize);
            int[] rSEncoded=Utils.rSEncode(converted,numRSEc,rSEcSize);
            rS.add(rSEncoded);
        }
        List<BitSet> rSBitSet=new LinkedList<>();

        for(int[] data:rS){
            BitSet dataBitSet=Utils.intArrayToBitSet(data,rSEcSize);
            rSBitSet.add(dataBitSet);

/*            byte[] converted=Utils.intArrayToByteArray(data,rSEcSize);
            BitSet dataBitSet=BitSet.valueOf(converted);
            rSBitSet.add(dataBitSet);*/

        }

        for(int i=0;i<rSBitSet.size();i++){
            BitSet dataBitSet=rSBitSet.get(i);
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
