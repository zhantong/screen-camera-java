import java.awt.*;

/**
 * Created by zhantong on 2016/11/17.
 */
public class BlackWhiteBlock implements Block{
    public void draw(Image image,int x,int y,int width,int height,int value){
        Color color=null;
        switch (value){
            case 0:
                color=Color.BLACK;
                break;
            case 1:
                color=Color.WHITE;
                break;
        }
        image.fillRect(x,y,width,height,color);
    }

    public int getBitsPerUnit() {
        return 1;
    }
}
