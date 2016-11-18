/**
 * Created by zhantong on 2016/11/18.
 */
public class BarCodeConfig {
    public int marginLeftWidth=1;
    public int marginUpHeight =1;
    public int marginRightWidth=1;
    public int marginDownHeight =1;

    public int borderLeftWidth=1;
    public int borderUpHeight =1;
    public int borderRightWidth=1;
    public int borderDownHeight =1;

    public int paddingLeftWidth=1;
    public int paddingUpHeight =1;
    public int paddingRightWidth=1;
    public int paddingDownHeight =1;

    public int mainWidth=8;
    public int mainHeight=8;

    public Block marginBlock=new BlackWhiteBlock();
    public Block borderBlock=new BlackWhiteBlock();
    public Block paddingBlock=new BlackWhiteBlock();
    public Block mainBlock=new BlackWhiteBlock();
}
