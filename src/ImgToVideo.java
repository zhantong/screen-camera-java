import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by zhantong on 16/3/17.
 */
public class ImgToVideo {
    public static void main(String[] args){
        runFFMpeg("/Users/zhantong/Desktop/test1",30,"/Users/zhantong/Downloads/SnowLeopard_Lion_Mountain_Lion_Mavericks_Yosemite_El-Captain_02.02.2016/ffmpeg");
    }
    public static void runFFMpeg(String imgDir,int framerate,String ffmpegDir){
        String command=String.format("ffmpeg -framerate %d -i %s -c:v libx264 -r %d -pix_fmt yuv420p out_framerate_%d.mp4",framerate,"%06d.png",framerate,framerate);
        String[] envp=new String[]{String.format("ffmpeg=%s",ffmpegDir)};
        File dir=new File(imgDir);
        runCommand(command,envp,dir);
    }
    public static void runCommand(String command,String[] envp,File dir){
        Process p=null;
        try {
            p=Runtime.getRuntime().exec(command,envp,dir);
        }catch (IOException e){
            e.printStackTrace();
        }
        InputStreamReader ir=new InputStreamReader(p.getInputStream());
        LineNumberReader input=new LineNumberReader(ir);
        String line;
        try {
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
