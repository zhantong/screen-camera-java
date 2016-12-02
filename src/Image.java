import java.io.IOException;

/**
 * Created by zhantong on 2016/12/2.
 */
public interface Image {
    void fillRect(int x,int y, int width,int height,CustomColor color);
    void save(int index,String directoryPath) throws IOException;
}
