/**
 * Created by zhantong on 2017/5/15.
 */
public class ColorCodeMLConfig extends BarcodeConfig {
    public ColorCodeMLConfig(){
        marginLength = new DistrictConfig<>(4);
        borderLength = new DistrictConfig<>(1);
        paddingLength = new DistrictConfig<>(2,0,2,0);
        metaLength=new DistrictConfig<>(0);

        mainWidth = 40;
        mainHeight = 40;

        blockLengthInPixel = 20;

        marginBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        borderBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        //metaBlock = new DistrictConfig<>(new ColorBlock(1,new CustomColor[]{CustomColor.Y1U0V0,CustomColor.Y1U1V1}));
        mainBlock = new DistrictConfig<>(new ColorBlock(2,new CustomColor[]{CustomColor.Y1U0V0,CustomColor.Y1U0V1,CustomColor.Y1U1V0,CustomColor.Y1U1V1}));

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
        //metaContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));

        hints.put(ColorCodeML.KEY_SIZE_RS_ERROR_CORRECTION,12);
        hints.put(ColorCodeML.KEY_LEVEL_RS_ERROR_CORRECTION,0.1);
        hints.put(ColorCodeML.KEY_NUMBER_RAPTORQ_SOURCE_BLOCKS,1);
        hints.put(ColorCodeML.KEY_PERCENT_RAPTORQ_REDUNDANT,0.5);
        hints.put(ColorCodeML.KEY_IS_REPLACE_LAST_RAPTORQ_SOURCE_PACKET_AS_REPAIR,true);
        hints.put(ColorCodeML.KEY_NUMBER_RANDOM_BARCODES,100);
    }
}
