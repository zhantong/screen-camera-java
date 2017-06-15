import java.util.*;

/**
 * Created by zhantong on 2016/11/19.
 */
public class ShiftCode extends BlackWhiteCode{
    public static void main(String[] args){
        Map<EncodeHintType,Object> hints=new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_SIZE,12);
        hints.put(EncodeHintType.RS_ERROR_CORRECTION_LEVEL,0.1);
        hints.put(EncodeHintType.RAPTORQ_NUMBER_OF_SOURCE_BLOCKS,1);
        ShiftCode shiftCode=new ShiftCode(new ShiftCodeConfig(),hints);
        shiftCode.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/ShiftCode");
    }
    public ShiftCode(BarcodeConfig config, Map<EncodeHintType, ?> hints) {
        super(config, hints);
    }
}
