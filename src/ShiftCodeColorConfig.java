/**
 * Created by zhantong on 2016/12/2.
 */
public class ShiftCodeColorConfig extends BarcodeConfig {
    public ShiftCodeColorConfig() {
        marginLength = new DistrictConfig<>(4);
        borderLength = new DistrictConfig<>(1);
        paddingLength = new DistrictConfig<>(1);
        metaLength = new DistrictConfig<>(0);

        mainWidth = 20;
        mainHeight = 20;

        blockLengthInPixel = 20;

        CustomColor black=CustomColor.Y1U0V0;
        CustomColor white=CustomColor.Y1U1V1;
        marginBlock = new DistrictConfig<>(new BlackWhiteBlock(black,CustomColor.Y1UmVm));
        //borderBlock = new DistrictConfig<>(new BlackWhiteBlock());
        borderBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        paddingBlock=new DistrictConfig<>(
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white),
                new ColorBlock(1,new CustomColor[]{CustomColor.Y1U0V0,CustomColor.Y1U1V1}),
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white)
        );
        metaBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));

        mainBlock = new DistrictConfig<>(new ColorShiftBlock(new int[]{1,2}));

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
        paddingContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
        metaContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
    }
}
