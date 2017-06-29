import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by zhantong on 2017/6/3.
 */
public class RDCodeMLConfig extends BarcodeConfig {
    int regionWidth;
    int regionHeight;
    int numRegionHorizon;
    int numRegionVertical;

    public RDCodeMLConfig(){
        marginLength = new DistrictConfig<>(4);
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

        hints.put(RDCodeML.KEY_SIZE_RS_ERROR_CORRECTION,8);
        hints.put(RDCodeML.KEY_LEVEL_RS_ERROR_CORRECTION,0.1);
        hints.put(RDCodeML.KEY_NUMBER_RANDOM_BARCODES,100);
    }
    @Override
    JsonElement toJson() {
        JsonObject root= (JsonObject) super.toJson();
        root.addProperty("regionWidth",regionWidth);
        root.addProperty("regionHeight",regionHeight);
        root.addProperty("numRegionHorizon",numRegionHorizon);
        root.addProperty("numRegionVertical",numRegionVertical);
        return root;
    }
}
