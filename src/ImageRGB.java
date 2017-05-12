import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhantong on 2016/11/17.
 */
public class ImageRGB implements Image{
    private BufferedImage image;
    private Graphics2D g;
    public ImageRGB(int widthInPixel, int heightInPixel){
        this(widthInPixel,heightInPixel,BufferedImage.TYPE_INT_RGB);
    }
    public ImageRGB(int widthInPixel, int heightInPixel, int colorType){
        image=new BufferedImage(widthInPixel,heightInPixel,colorType);
        g=image.createGraphics();
    }
    public void fillRect(int x,int y, int width,int height,CustomColor customColor){
        g.setColor(new Color(customColor.getRGB()));
        g.fillRect(x,y,width,height);
    }

    @Override
    public void fillRect(int x, int y, int width, int height, CustomColor color, int channel) {

    }

    @Override
    public void save(int index, String directoryPath) throws IOException{
        g.dispose();
        image.flush();
        String fileName=String.format("%06d.png",index);
        String filePath=Utils.combinePaths(directoryPath,fileName);
        ImageIO.write(image,"PNG",new File(filePath));
    }

    @Override
    public void save(int index, String directoryPath, int colorType) throws IOException {
        save(index,directoryPath);
    }
}
