import java.util.EnumMap;
import java.util.Map;

/**
 * Created by zhantong on 2017/5/15.
 */
public class ColorCodeML extends BlackWhiteCodeML {
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.1);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        hints.put(EncodeHintType.NUMBER_OF_RANDOM_BARCODES,100);
        ColorCodeML colorCodeML=new ColorCodeML(new ColorCodeMLConfig(),hints);
        colorCodeML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/ColorCodeML");
    }
    public ColorCodeML(BarcodeConfig config, Map<EncodeHintType, ?> hints) {
        super(config, hints);
    }
}
