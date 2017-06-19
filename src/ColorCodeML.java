
/**
 * Created by zhantong on 2017/5/15.
 */
public class ColorCodeML extends BlackWhiteCodeML {
    public static void main(String[] args){
        ColorCodeML colorCodeML=new ColorCodeML(new ColorCodeMLConfig());
        colorCodeML.toImages("/Volumes/扩展存储/ShiftCode实验/发送方/sample0.txt","/Users/zhantong/Desktop/ColorCodeML");
    }
    public ColorCodeML(BarcodeConfig config) {
        super(config);
    }
}
