/**
 * Created by zhantong on 2017/6/3.
 */
public class RDCodeMLConfig extends BarcodeConfig {
    int regionWidth;
    int regionHeight;
    int numRegionHorizon;
    int numRegionVertical;

    int numInterBlockEc=3;

    public RDCodeMLConfig(){
        marginLength = new DistrictConfig<>(8);
        borderLength = new DistrictConfig<>(1);
        paddingLength = new DistrictConfig<>(2,0,2,0);
        metaLength=new DistrictConfig<>(0);

        regionWidth=12;
        regionHeight=12;
        numRegionHorizon=3;
        numRegionVertical=3;

        mainWidth = numRegionHorizon*regionWidth;
        mainHeight = numRegionVertical*regionHeight;

        blockLengthInPixel = 20;

        marginBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        borderBlock = new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
        mainBlock = new DistrictConfig<>(new ColorBlock(2,new CustomColor[]{CustomColor.Y1U0V0,CustomColor.Y1U0V1,CustomColor.Y1U1V0,CustomColor.Y1U1V1}));

        marginContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
        borderContent = new DistrictConfig<>(new BitContent(BitContent.ALL_ZEROS));
    }
}
