import java.util.EnumMap;
import java.util.Map;

/**
 * Created by zhantong on 2016/11/22.
 */
public class ShiftCodeML extends BlackWhiteCodeML {

    public static void main(String[] args){
        ShiftCodeML shiftCodeML=new ShiftCodeML(new ShiftCodeMLConfig());
        shiftCodeML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/ShiftCodeML");
    }
    public ShiftCodeML(BarcodeConfig config) {
        super(config);
    }
}
