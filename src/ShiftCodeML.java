import java.util.EnumMap;
import java.util.Map;

/**
 * Created by zhantong on 2016/11/22.
 */
public class ShiftCodeML extends BlackWhiteCodeML {

    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.1);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        ShiftCodeML shiftCodeML=new ShiftCodeML(new ShiftCodeMLConfig(),hints);
        shiftCodeML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample2.txt","/Users/zhantong/Desktop/ShiftCodeML2");
    }
    public ShiftCodeML(BarcodeConfig config, Map<EncodeHintType, ?> hints) {
        super(config, hints);
    }
}
