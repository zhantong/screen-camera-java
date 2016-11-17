import java.util.BitSet;

/**
 * Created by zhantong on 2016/11/17.
 */
public class Utils {
    public static int bitsToInt(BitSet bitSet,int length,int offset){
        int value=0;
        for(int i=0;i<length;i++){
            value+=bitSet.get(offset+i)?(1<<i):0;
        }
        return value;
    }
    public static BitSet intArrayToBitSet(int data[],int bitsPerInt){
        int index=0;
        BitSet bitSet=new BitSet();
        for(int current:data){
            for(int i=0;i<bitsPerInt;i++){
                if((current&(1<<i))>0){
                    bitSet.set(index);
                }
                index++;
            }
        }
        return bitSet;
    }
}
