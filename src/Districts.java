import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhantong on 2016/11/17.
 */
public class Districts {
    District margin;
    District border;
    District padding;
    Zone main;
    List<District> districts;
    public Districts(){
        districts=new ArrayList<>();
        margin=new District();
        districts.add(margin);
        border=new District();
        districts.add(border);
        padding=new District();
        districts.add(padding);
    }
}
