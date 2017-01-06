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

        CustomColor black=CustomColor.YmU0V0;
        CustomColor white=CustomColor.YmU1V1;
        marginBlock = new DistrictConfig<>(new BlackWhiteBlock(black,white));
        //borderBlock = new DistrictConfig<>(new BlackWhiteBlock());
        borderBlock=new DistrictConfig<>(
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white),
                new ColorBlock(1,new CustomColor[]{CustomColor.YmU0V0,CustomColor.YmU1V1}),
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white),
                new BlackWhiteBlock(black,white)
        );
        mainBlock = new DistrictConfig<>(new ColorShiftBlock(new int[]{1,2}));

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
    }
}
