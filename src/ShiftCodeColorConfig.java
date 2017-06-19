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

        hints.put(ShiftCodeColor.KEY_SIZE_RS_ERROR_CORRECTION,12);
        hints.put(ShiftCodeColor.KEY_LEVEL_RS_ERROR_CORRECTION,0.1);
        hints.put(ShiftCodeColor.KEY_NUMBER_RAPTORQ_SOURCE_BLOCKS,1);
        hints.put(ShiftCodeColor.KEY_PERCENT_RAPTORQ_REDUNDANT,0.5);
        hints.put(ShiftCodeColor.KEY_IS_REPLACE_LAST_RAPTORQ_SOURCE_PACKET_AS_REPAIR,true);
    }
}
