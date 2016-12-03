import java.io.IOException;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhantong on 2016/12/2.
 */
public class ShiftCodeColor extends ShiftCode {
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.3);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        ShiftCodeColor shiftCodeColor=new ShiftCodeColor(new ShiftCodeColorConfig(),hints);
        shiftCodeColor.toImages("/Users/zhantong/Downloads/sample1.txt","/Users/zhantong/Desktop/ShiftCodeColor4");
    }
    public ShiftCodeColor(BarcodeConfig config, Map<EncodeHintType, ?> hints) {
        super(config, hints);
    }
    protected void bitSetListToImages(List<BitSet> dataList, String outputDirectoryPath, BarcodeConfig config){
        for(int i=0;i<dataList.size();i++){
            BitSet dataBitSet=dataList.get(i);
            BitContent dataContent=new BitContent(dataBitSet);
            reconfigure(config,i);
            Barcode barcode=new Barcode(i,config);
            barcode.districts.get(Districts.MAIN).get(District.MAIN).addContent(dataContent);
            Image image=barcode.toImage(1);
            try {
                image.save(i,outputDirectoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
