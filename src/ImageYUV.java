import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by zhantong on 2016/12/2.
 */
public class ImageYUV implements Image{
    public static final int TYPE_YUV444=0;
    public static final int TYPE_YUV420=1;
    private byte[] YBuffer;
    private byte[] UBuffer;
    private byte[] VBuffer;
    private int width;
    private int height;
    public ImageYUV(int widthInPixel,int heightInPixel){
        this.width=widthInPixel;
        this.height=heightInPixel;
        int numPixels=widthInPixel*heightInPixel;
        YBuffer=new byte[numPixels];
        UBuffer=new byte[numPixels];
        VBuffer=new byte[numPixels];

        //Arrays.fill(YBuffer,(byte)255);
        //Arrays.fill(UBuffer,(byte)255);
        //Arrays.fill(VBuffer,(byte)255);
    }
    public void fillRect(int rectX,int rectY, int rectWidth,int rectHeight,CustomColor color){
        byte Y=(byte)color.getY();
        byte U=(byte)color.getU();
        byte V=(byte)color.getV();
        for(int row=rectY;row<rectY+rectHeight;row++){
            int rowStartPos=row*width;
            for(int column=rectX;column<rectX+rectWidth;column++){
                int pos=rowStartPos+column;
                YBuffer[pos]=Y;
                UBuffer[pos]=U;
                VBuffer[pos]=V;
            }
        }
    }

    @Override
    public void fillRect(int rectX,int rectY, int rectWidth,int rectHeight,CustomColor color, int channel) {
        byte Y=(byte)color.getY();
        byte U=(byte)color.getU();
        byte V=(byte)color.getV();
        for(int row=rectY;row<rectY+rectHeight;row++){
            int rowStartPos=row*width;
            for(int column=rectX;column<rectX+rectWidth;column++){
                int pos=rowStartPos+column;
                switch (channel){
                    case 0:
                        YBuffer[pos]=Y;
                        break;
                    case 1:
                        UBuffer[pos]=U;
                        break;
                    case 2:
                        VBuffer[pos]=V;
                        break;
                }
            }
        }
    }

    public void save(int index,String directoryPath) throws IOException {
        save(index,directoryPath,TYPE_YUV444);
    }

    @Override
    public void save(int index, String directoryPath, int colorType) throws IOException {
        String colorName;
        switch (colorType){
            case TYPE_YUV444:
                colorName="YUV444P";
                break;
            case TYPE_YUV420:
                colorName="YUV420P";
                break;
            default:
                throw new IllegalArgumentException();
        }

        String fileName=String.format("%s_%dx%d_%06d.yuv",colorName,width,height,index);
        String filePath=Utils.combinePaths(directoryPath,fileName);
        FileOutputStream fos=new FileOutputStream(filePath);
        fos.write(YBuffer);
        if(colorType==TYPE_YUV444){
            fos.write(UBuffer);
            fos.write(VBuffer);
        }else if(colorType==TYPE_YUV420){
            byte[] newU=new byte[width*height/4];
            byte[] newV=new byte[width*height/4];
            for(int y=0;y<height/2;y++){
                int startYPos=y*width/2;
                for(int x=0;x<width/2;x++){
                    newU[startYPos+x]=(byte)((UBuffer[(y*2)*width+x*2]+UBuffer[(y*2)*width+x*2+1]+UBuffer[(y*2+1)*width+x*2]+UBuffer[(y*2+1)*width+x*2+1])/4);
                    newV[startYPos+x]=(byte)((VBuffer[(y*2)*width+x*2]+VBuffer[(y*2)*width+x*2+1]+VBuffer[(y*2+1)*width+x*2]+VBuffer[(y*2+1)*width+x*2+1])/4);
                }
            }
            fos.write(newU);
            fos.write(newV);
        }
        fos.close();
    }
}
