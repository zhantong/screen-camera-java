import java.util.BitSet;

/**
 * Created by zhantong on 2016/11/17.
 */
public class BitContent {
    private BitSet content;
    public BitContent(BitSet content){
        this.content=content;
    }
    public int get(int pos,int length){
        int value= Utils.bitsToInt(content,length,pos);
        return value;
    }
}
