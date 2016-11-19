/**
 * Created by zhantong on 2016/11/18.
 */
public class BarCodeConfig {
    public int marginWidthAndHeight=1;
    public int marginLeftWidth=1;
    public int marginUpHeight =1;
    public int marginRightWidth=1;
    public int marginDownHeight =1;

    public int borderWidthAndHeight=1;
    public int borderLeftWidth=1;
    public int borderUpHeight =1;
    public int borderRightWidth=1;
    public int borderDownHeight =1;

    public int paddingWidthAndHeight=1;
    public int paddingLeftWidth=1;
    public int paddingUpHeight =1;
    public int paddingRightWidth=1;
    public int paddingDownHeight =1;

    public int mainWidth=8;
    public int mainHeight=8;

    public int blockLengthInPixel=4;

    public DistrictConfig<Block> marginBlock=new DistrictConfig<>(new BlackWhiteBlock());
    /*
    public DistrictConfig<Block> marginBlock=new DistrictConfig<>(new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock());
    */

    public DistrictConfig<Block> borderBlock=new DistrictConfig<>(new BlackWhiteBlock());
    /*
    public DistrictConfig<Block> borderBlock=new DistrictConfig<>(new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock());
    */

    public DistrictConfig<Block> paddingBlock=new DistrictConfig<>(new BlackWhiteBlock());
    /*
    public DistrictConfig<Block> paddingBlock=new DistrictConfig<>(new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock());
    */

    public DistrictConfig<Block> mainBlock=new DistrictConfig<>(new BlackWhiteBlock());

    public DistrictConfig<BitContent> marginContent=new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
    /*
    public DistrictConfig<BitContent> marginContent=new DistrictConfig<>(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    */

    public DistrictConfig<BitContent> borderContent=new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
    /*
    public DistrictConfig<BitContent> borderContent=new DistrictConfig<>(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    */

    public DistrictConfig<BitContent> paddingContent=new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
    /*
    public DistrictConfig<BitContent> paddingContent=new DistrictConfig<>(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    */
}
