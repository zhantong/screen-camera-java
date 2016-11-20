/**
 * Created by zhantong on 2016/11/19.
 */
public class ShiftCodeConfig extends BarcodeConfig{
    public ShiftCodeConfig() {
        marginLength = new DistrictConfig<>(2);
        borderLength = new DistrictConfig<>(1);
        paddingLength = new DistrictConfig<>(0);

        mainWidth = 40;
        mainHeight = 40;

        blockLengthInPixel = 10;

        marginBlock = new DistrictConfig<>(new BlackWhiteBlock());
        borderBlock = new DistrictConfig<>(new BlackWhiteBlock());
        mainBlock = new DistrictConfig<>(new ShiftBlock());

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
    }
}
