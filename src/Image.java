import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhantong on 2016/11/17.
 */
public class Image {
    private BufferedImage image;
    private Graphics2D g;
    public Image(int widthInPixel,int heightInPixel){
        this(widthInPixel,heightInPixel,BufferedImage.TYPE_INT_RGB);
    }
    public Image(int widthInPixel,int heightInPixel,int colorType){
        image=new BufferedImage(widthInPixel,heightInPixel,colorType);
        g=image.createGraphics();
    }
    public void fillRect(int x,int y, int width,int height,Color color){
        g.setColor(color);
        g.fillRect(x,y,width,height);
    }
    public void save(String imgFormat,String filePath) throws IOException {
        g.dispose();
        image.flush();
        File file=new File(filePath);
        ImageIO.write(image,imgFormat,file);
    }
}
