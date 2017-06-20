import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhantong on 2016/11/18.
 */
public class BarcodeConfig {
    public DistrictConfig<Integer> marginLength=new DistrictConfig<>(1);
    //public DistrictConfig<Integer> marginLength=new DistrictConfig<>(1,1,1,1);

    public DistrictConfig<Integer> borderLength=new DistrictConfig<>(1);
    //public DistrictConfig<Integer> borderLength=new DistrictConfig<>(1,1,1,1);

    public DistrictConfig<Integer> paddingLength=new DistrictConfig<>(1);
    //public DistrictConfig<Integer> paddingLength=new DistrictConfig<>(1,1,1,1);

    public DistrictConfig<Integer> metaLength=new DistrictConfig<>(1);
    //public DistrictConfig<Integer> metaLength=new DistrictConfig<>(1,1,1,1);

    public int mainWidth=8;
    public int mainHeight=8;

    public int blockLengthInPixel=4;

    public DistrictConfig<Block> marginBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
    /*
    public DistrictConfig<Block> marginBlock=new DistrictConfig<>(new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock());
    */

    public DistrictConfig<Block> borderBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
    /*
    public DistrictConfig<Block> borderBlock=new DistrictConfig<>(new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock());
    */

    public DistrictConfig<Block> paddingBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
    /*
    public DistrictConfig<Block> paddingBlock=new DistrictConfig<>(new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock());
    */

    public DistrictConfig<Block> metaBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));
    /*
    public DistrictConfig<Block> metaBlock=new DistrictConfig<>(new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock(),
            new BlackWhiteBlock());
    */

    public DistrictConfig<Block> mainBlock=new DistrictConfig<>(new BlackWhiteBlock(CustomColor.Y0UmVm,CustomColor.Y1UmVm));

    public DistrictConfig<BitContent> marginContent=new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
    /*
    public DistrictConfig<BitContent> marginContent=new DistrictConfig<>(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    */

    public DistrictConfig<BitContent> borderContent=new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
    /*
    public DistrictConfig<BitContent> borderContent=new DistrictConfig<>(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    */

    public DistrictConfig<BitContent> paddingContent=new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
    /*
    public DistrictConfig<BitContent> paddingContent=new DistrictConfig<>(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    */

    public DistrictConfig<BitContent> metaContent=new DistrictConfig<>(new BitContent(BitContent.ALL_ONES));
    /*
    public DistrictConfig<BitContent> metaContent=new DistrictConfig<>(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    */

    public Map<String,Object> hints=new HashMap<>();

    JsonElement toJson(){
        Gson gson=new Gson();
        JsonObject root=new JsonObject();
        root.add("borderLength",borderLength.toJson());
        root.add("paddingLength",paddingLength.toJson());
        root.add("metaLength",metaLength.toJson());
        root.addProperty("mainWidth",mainWidth);
        root.addProperty("mainHeight",mainHeight);

        Block block=mainBlock.get(District.MAIN);
        root.addProperty("mainBlockName",block.getClass().getName());
        root.addProperty("mainBlockBitsPerUnit",block.getBitsPerUnit());

        root.add("hints",gson.toJsonTree(hints));
        return root;
    }
}
