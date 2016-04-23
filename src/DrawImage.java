import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhantong on 16/3/17.
 */
public class DrawImage {
    int blockLength;
    Graphics2D g;
    BufferedImage img;
    public DrawImage(int imgWidthBlock,int imgHeightBlock,int blockLength){
        this.blockLength=blockLength;
        int imgWidth=imgWidthBlock*blockLength;
        int imgHeight=imgHeightBlock*blockLength;
        img=new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_BYTE_BINARY);
        g=img.createGraphics();
        initGraphics(imgWidth,imgHeight);
    }
    private void initGraphics(int imgWidth,int imgHeight){
        g.setBackground(Color.WHITE);
        g.clearRect(0,0,imgWidth,imgHeight);
    }
    public void clearBackground(Color color,int x,int y,int width,int height){
        g.setBackground(color);
        g.clearRect(x*blockLength,y*blockLength,width*blockLength,height*blockLength);
    }

    public void fillWhiteRect(int x,int y,int width,int height){
        g.setColor(Color.WHITE);
        fillRect(x,y,width,height);
    }
    public void fillBlackRect(int x,int y,int width,int height){
        g.setColor(Color.BLACK);
        fillRect(x,y,width,height);
    }
    public void setDefaultColor(Color color){
        g.setColor(color);
    }
    public void fillRect(int x,int y,int width,int height){
        g.fillRect(x*blockLength,y*blockLength,width*blockLength,height*blockLength);
    }
    public void fillRect(int x,int y,float offsetX,float offsetY,float width,float height){
        g.fillRect(Math.round((x+offsetX)*blockLength),Math.round((y+offsetY)*blockLength),Math.round(width*blockLength),Math.round(height*blockLength));
    }
    public void save(String imgFormat,String filePath) throws IOException{
        g.dispose();
        img.flush();
        File file=new File(filePath);
        ImageIO.write(img,imgFormat,file);
    }
}
