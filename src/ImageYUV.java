import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by zhantong on 2016/12/2.
 */
public class ImageYUV implements Image{
    public static final int TYPE_YUV444=0;
    private byte[] YBuffer;
    private byte[] UBuffer;
    private byte[] VBuffer;
    private int width;
    private int height;
    private int colorType;
    public ImageYUV(int widthInPixel,int heightInPixel){
        this(widthInPixel,heightInPixel,TYPE_YUV444);
    }
    public ImageYUV(int widthInPixel,int heightInPixel,int colorType){
        this.width=widthInPixel;
        this.height=heightInPixel;
        this.colorType=colorType;
        int numPixels=widthInPixel*heightInPixel;
        YBuffer=new byte[numPixels];
        UBuffer=new byte[numPixels];
        VBuffer=new byte[numPixels];
    }
    public void fillRect(int x,int y, int width,int height,CustomColor color){
        byte Y=(byte)color.getY();
        byte U=(byte)color.getU();
        byte V=(byte)color.getV();
        for(int row=y;row<y+height;row++){
            int rowStartPos=row*width;
            for(int column=x;column<x+width;column++){
                int pos=rowStartPos+column;
                YBuffer[pos]=Y;
                UBuffer[pos]=U;
                VBuffer[pos]=V;
            }
        }
    }

    public void save(int index,String directoryPath) throws IOException {
        String colorName;
        switch (colorType){
            case TYPE_YUV444:
                colorName="YUV444P";
                break;
            default:
                throw new IllegalArgumentException();
        }
        String fileName=String.format("%s_%dx%d_%06d.yuv",colorName,width,height,index);
        String filePath=Utils.combinePaths(directoryPath,fileName);
        FileOutputStream fos=new FileOutputStream(filePath);
        fos.write(YBuffer);
        fos.write(UBuffer);
        fos.write(VBuffer);
        fos.close();
    }
}
