import java.util.BitSet;

/**
 * Created by zhantong on 2016/11/17.
 */
public class BitContent {
    private BitSet content;
    private int posInUnit;
    private int bitsPerUnit;
    public BitContent(BitSet content,int bitsPerUnit){
        this.content=content;
        this.bitsPerUnit=bitsPerUnit;
    }
    public int get(){
        int value= Utils.bitsToInt(content,bitsPerUnit,(posInUnit)*bitsPerUnit);
        posInUnit+=1;
        return value;
    }
}
