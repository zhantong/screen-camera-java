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
        shiftCodeColor.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample3.txt","/Users/zhantong/Desktop/ShiftCodeColor6");
    }
    public ShiftCodeColor(BarcodeConfig config, Map<EncodeHintType, ?> hints) {
        super(config, hints);
    }
}
