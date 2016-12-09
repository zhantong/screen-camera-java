/**
 * Created by zhantong on 2016/12/2.
 */
public class ShiftCodeColorConfig extends BarcodeConfig {
    public ShiftCodeColorConfig() {
        marginLength = new DistrictConfig<>(8);
        borderLength = new DistrictConfig<>(1);
        paddingLength = new DistrictConfig<>(0);

        mainWidth = 40;
        mainHeight = 40;

        blockLengthInPixel = 20;

        marginBlock = new DistrictConfig<>(new BlackWhiteBlock());
        //borderBlock = new DistrictConfig<>(new BlackWhiteBlock());
        borderBlock=new DistrictConfig<>(
                new BlackWhiteBlock(),
                new BlackWhiteBlock(),
                new ColorBlock(1),
                new BlackWhiteBlock(),
                new BlackWhiteBlock(),
                new BlackWhiteBlock(),
                new BlackWhiteBlock(),
                new BlackWhiteBlock()
        );
        mainBlock = new DistrictConfig<>(new ColorShiftBlock(2));

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
    }
}
