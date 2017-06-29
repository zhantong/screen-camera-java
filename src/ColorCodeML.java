
/**
 * Created by zhantong on 2017/5/15.
 */
public class ColorCodeML extends BlackWhiteCodeML {
    public static void main(String[] args){
        ColorCodeML colorCodeML=new ColorCodeML(new ColorCodeMLConfig());
        colorCodeML.toImages("/Volumes/扩展存储/实验/原始文件/sample23.txt","/Volumes/扩展存储/实验/ColorCodeML/140x140_0.1/1x");
        colorCodeML.saveJsonToFile("out.json");
    }
    public ColorCodeML(BarcodeConfig config) {
        super(config);
    }
}
