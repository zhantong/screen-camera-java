/**
 * Created by zhantong on 2016/11/18.
 */
public class BarcodeConfig {
    public DistrictConfig<Integer> marginLength=new DistrictConfig<>(1);
    //public DistrictConfig<Integer> marginLength=new DistrictConfig<>(1,1,1,1);

    public DistrictConfig<Integer> borderLength=new DistrictConfig<>(1);
    //public DistrictConfig<Integer> borderLength=new DistrictConfig<>(1,1,1,1);

    public DistrictConfig<Integer> paddingLength=new DistrictConfig<>(1);
    //public DistrictConfig<Integer> paddingLength=new DistrictConfig<>(1,1,1,1);

    public DistrictConfig<Integer> metaLength=new DistrictConfig<>(1);
    //public DistrictConfig<Integer> metaLength=new DistrictConfig<>(1,1,1,1);

    public int mainWidth=8;
    public int mainHeight=8;

    public int blockLengthInPixel=4;

    public DistrictConfig<Block> marginBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
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

    public DistrictConfig<Block> borderBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
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

    public DistrictConfig<Block> paddingBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
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

    public DistrictConfig<Block> metaBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
    /*
    public DistrictConfig<Block> metaBlock=new DistrictConfig<>(new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock());
    */

    public DistrictConfig<Block> mainBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));

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

    public DistrictConfig<BitContent> metaContent=new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
    /*
    public DistrictConfig<BitContent> metaContent=new DistrictConfig<>(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    */
}
