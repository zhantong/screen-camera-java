/**
 * Created by zhantong on 2017/3/20.
 */
public class ShiftCodeColorMLConfig extends BarcodeConfig {
    public ShiftCodeColorMLConfig(){
        marginLength = new DistrictConfig<>(4);
        borderLength = new DistrictConfig<>(1);
        paddingLength = new DistrictConfig<>(2,0,2,0);
        metaLength=new DistrictConfig<>(1,0,1,0);

        mainWidth = 60;
        mainHeight = 60;

        blockLengthInPixel = 20;

        marginBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        borderBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        metaBlock = new DistrictConfig<>(new ColorBlock(1,new CustomColor[]{CustomColor.Y1U0V0,CustomColor.Y1U1V1}));
        mainBlock = new DistrictConfig<>(new ColorShiftBlock(new int[]{1,2}));

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
        metaContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
    }
}
