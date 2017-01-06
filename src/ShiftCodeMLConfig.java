/**
 * Created by zhantong on 2016/11/22.
 */
public class ShiftCodeMLConfig extends BarcodeConfig {
    public ShiftCodeMLConfig(){
        marginLength = new DistrictConfig<>(2);
        borderLength = new DistrictConfig<>(1);
        paddingLength = new DistrictConfig<>(2,0,2,0);

        mainWidth = 40;
        mainHeight = 40;

        blockLengthInPixel = 10;

        marginBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        borderBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        mainBlock = new DistrictConfig<>(new ShiftBlock());

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
    }
}
