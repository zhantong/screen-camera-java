import net.fec.openrq.parameters.FECParameters;

import java.io.IOException;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhantong on 2016/11/19.
 */
public class ShiftCode {
    ShiftCodeConfig config;
    int rSEcSize=12;
    double rSEcLevel=0.2;
    int numRSEc;
    public static void main(String[] args){
        ShiftCode shiftCode=new ShiftCode();
        shiftCode.toImages("/Users/zhantong/Desktop/phpinfo.txt","/Users/zhantong/Desktop/out");
    }
    public ShiftCode(){
        config=new ShiftCodeConfig();
        numRSEc=calcEcNum(rSEcLevel);
        BitSet rightBarBitSet=new BitSet();
        for(int i=0;i<config.mainHeight;i+=2){
            rightBarBitSet.set(i);
        }
        BitContent rightBarContent=new BitContent(rightBarBitSet);

        config.borderContent.set(District.RIGHT,rightBarContent);
    }
    private void toImages(String inputFilePath,String outputDirectoryPath){
        int NUMBER_OF_SOURCE_BLOCKS=1;
        byte[] inputFileArray=new byte[1];
        try {
            inputFileArray=Utils.fileToByteArray(inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitSet topBarBitSet=Utils.intWithCRC8Checksum(inputFileArray.length);
        BitContent topBarContent=new BitContent(topBarBitSet);
        config.borderContent.set(District.UP,topBarContent);

        int numDataBytes = config.mainBlock.get(District.MAIN).getBitsPerUnit()*config.mainWidth*config.mainHeight / 8 - rSEcSize * numRSEc / 8 - 8;
        FECParameters parameters = FECParameters.newParameters(inputFileArray.length, numDataBytes, NUMBER_OF_SOURCE_BLOCKS);
        List<byte[]> raptorQ=Utils.raptorQEncode(inputFileArray,parameters,0.5f,true);
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

        BitSet leftBarBitSet=new BitSet();
        leftBarBitSet.set(0);
        leftBarBitSet.set(config.mainHeight-1);
        BitContent leftBarContentEven=new BitContent(leftBarBitSet);
        BitContent leftBarContentOdd=new BitContent(BitContent.ALL_ZEROS);
        for(int i=0;i<rSBitSet.size();i++){
            BitSet dataBitSet=rSBitSet.get(i);
            BitContent dataContent=new BitContent(dataBitSet);
            if(i%2==0) {
                config.borderContent.set(District.LEFT, leftBarContentEven);
            }else{
                config.borderContent.set(District.LEFT, leftBarContentOdd);
            }
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
    protected int calcEcNum(double ecLevel){
        return ((int)((config.mainBlock.get(District.MAIN).getBitsPerUnit()*config.mainWidth*config.mainHeight/rSEcSize)*ecLevel))/2*2;
    }
}
