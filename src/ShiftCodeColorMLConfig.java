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

        hints.put(ShiftCodeColorML.KEY_SIZE_RS_ERROR_CORRECTION,12);
        hints.put(ShiftCodeColorML.KEY_LEVEL_RS_ERROR_CORRECTION,0.1);
        hints.put(ShiftCodeColorML.KEY_NUMBER_RAPTORQ_SOURCE_BLOCKS,1);
        hints.put(ShiftCodeColorML.KEY_PERCENT_RAPTORQ_REDUNDANT,0.5);
        hints.put(ShiftCodeColorML.KEY_IS_REPLACE_LAST_RAPTORQ_SOURCE_PACKET_AS_REPAIR,true);
        hints.put(ShiftCodeColorML.KEY_NUMBER_RANDOM_BARCODES,100);
    }
}
