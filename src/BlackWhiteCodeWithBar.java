import java.util.BitSet;

/**
 * Created by zhantong on 2017/6/15.
 */
public class BlackWhiteCodeWithBar extends BlackWhiteCode {
    public static void main(String[] args){
        BlackWhiteCodeWithBar blackWhiteCodeWithBar=new BlackWhiteCodeWithBar(new BlackWhiteCodeWithBarConfig());
        blackWhiteCodeWithBar.toImages("/Volumes/扩展存储/实验/原始文件/sample5.txt","/Volumes/扩展存储/实验/BlackWhiteCodeWithBar/140x140_0.1/1x");
        blackWhiteCodeWithBar.saveJsonToFile("out.json");
    }
    public BlackWhiteCodeWithBar(BarcodeConfig config) {
        super(config);
    }
    protected BarcodeConfig reconfigure(BarcodeConfig config,int barcodeIndex){
        super.reconfigure(config,barcodeIndex);
        BitSet paddingBarWhiteBlackBitSet=new BitSet();
        BitSet paddingBarBlackWhiteBitSet=new BitSet();
        for(int i=0;i<config.mainHeight*2;i+=2){
            paddingBarWhiteBlackBitSet.set(i);
            paddingBarBlackWhiteBitSet.set(i+1);
        }
        BitContent paddingBarWhiteBlackContent=new BitContent(paddingBarWhiteBlackBitSet);
        BitContent paddingBarBlackWhiteContent=new BitContent(paddingBarBlackWhiteBitSet);
        if(barcodeIndex%2==0) {
            config.paddingContent.set(District.LEFT, paddingBarWhiteBlackContent);
            config.paddingContent.set(District.RIGHT, paddingBarWhiteBlackContent);
        }else{
            config.paddingContent.set(District.LEFT, paddingBarBlackWhiteContent);
            config.paddingContent.set(District.RIGHT, paddingBarBlackWhiteContent);
        }
        return config;
    }
}
