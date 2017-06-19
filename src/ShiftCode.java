
/**
 * Created by zhantong on 2016/11/19.
 */
public class ShiftCode extends BlackWhiteCode{
    public static void main(String[] args){
        ShiftCode shiftCode=new ShiftCode(new ShiftCodeConfig());
        shiftCode.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/ShiftCode");
    }
    public ShiftCode(BarcodeConfig config) {
        super(config);
    }
}
