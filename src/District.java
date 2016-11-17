import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhantong on 2016/11/17.
 */
public class District {
    Zone left;
    Zone up;
    Zone right;
    Zone down;
    List<Zone> zones;
    public District(){
        zones=new ArrayList<>();
        zones.add(left);
        zones.add(up);
        zones.add(right);
        zones.add(down);
    }
}
