/**
 * Created by zhantong on 2016/11/22.
 */
public class ShiftCodeMLConfig extends BarcodeConfig {
    public ShiftCodeMLConfig(){
        marginLength = new DistrictConfig<>(4);
        borderLength = new DistrictConfig<>(1);
        paddingLength = new DistrictConfig<>(2,0,2,0);
        metaLength=new DistrictConfig<>(0);

        mainWidth = 40;
        mainHeight = 40;

        blockLengthInPixel = 20;

        marginBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        borderBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        mainBlock = new DistrictConfig<>(new ShiftBlock());

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));

        hints.put(ShiftCodeML.KEY_SIZE_RS_ERROR_CORRECTION,12);
        hints.put(ShiftCodeML.KEY_LEVEL_RS_ERROR_CORRECTION,0.1);
        hints.put(ShiftCodeML.KEY_NUMBER_RAPTORQ_SOURCE_BLOCKS,1);
        hints.put(ShiftCodeML.KEY_PERCENT_RAPTORQ_REDUNDANT,0.5);
        hints.put(ShiftCodeML.KEY_IS_REPLACE_LAST_RAPTORQ_SOURCE_PACKET_AS_REPAIR,true);
        hints.put(ShiftCodeML.KEY_NUMBER_RANDOM_BARCODES,100);
    }
}
