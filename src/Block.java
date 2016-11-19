/**
 * Created by zhantong on 2016/11/17.
 */
public interface Block {
    void draw(Image image,int x,int y,int width,int height,int value,int barcodeIndex,int column,int row);
    int getBitsPerUnit();
}
