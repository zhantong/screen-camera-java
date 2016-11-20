import java.io.*;
import java.util.Map;

/**
 * Created by zhantong on 16/3/17.
 */
public class ImagesToVideo {
    private int imageFrameRate=1;
    private int videoFrameRate=30;
    private int startNumber=0;
    private boolean forceOverWrite=false;
    private File inputDirectory;
    private File outputDirectory;
    private String videoFileName=null;
    private String imageFileNameReg=null;
    private String ffmpegPath =null;

    public static void main(String[] args){
        ImagesToVideo toVideo=new ImagesToVideo(new File("/Users/zhantong/Desktop/out"),"%06d.png");
        toVideo.setImageFrameRate(0);
        toVideo.setForceOverWrite(true);
        try {
            boolean returnValue=toVideo.run();
            if(returnValue){
                System.out.println("success");
            }else{
                System.out.println("failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public ImagesToVideo(File inputDirectory,String imageFileNameReg,int imageFrameRate){
        this(inputDirectory,imageFileNameReg);
        setImageFrameRate(imageFrameRate);
    }
    public ImagesToVideo(File inputDirectory,String imageFileNameReg){
        setInputDirectory(inputDirectory);
        setImageFileNameReg(imageFileNameReg);
    }
    public void setInputDirectory(File inputDirectory){
        this.inputDirectory=inputDirectory;
        this.outputDirectory=inputDirectory;
    }
    public void setOutputDirectory(File outputDirectory){
        this.outputDirectory=outputDirectory;
    }
    public void setImageFrameRate(int imageFrameRate){
        this.imageFrameRate=imageFrameRate;
    }
    public void setVideoFrameRate(int videoFrameRate){
        this.videoFrameRate=videoFrameRate;
    }
    public void setStartNumber(int startNumber){
        this.startNumber=startNumber;
    }
    public void setForceOverWrite(boolean forceOverWrite){
        this.forceOverWrite=forceOverWrite;
    }
    public void setVideoFileName(String videoFileName){
        this.videoFileName=videoFileName;
    }
    public void setFFmpegPath(String path){
        ffmpegPath =path;
    }
    public void setImageFileNameReg(String imageFileNameReg){
        this.imageFileNameReg=imageFileNameReg;
    }
    public boolean run() throws IOException,InterruptedException{
        if(inputDirectory==null||outputDirectory==null||!inputDirectory.exists()||!outputDirectory.exists()){
            throw new IllegalArgumentException("input/output directory not set or don't exists");
        }
        if(imageFileNameReg==null){
            throw new IllegalArgumentException("imageFileNameReg not set.");
        }
        if(videoFileName==null){
            videoFileName=String.format("out_framerate_%d.mp4",imageFrameRate);
        }
        File videoFile=new File(outputDirectory,videoFileName);
        if(videoFile.exists()&&!videoFile.isDirectory()){
            if(forceOverWrite){
                videoFile.delete();
            }else {
                throw new RuntimeException("video file already exists and force overwrite is false.");
            }
        }
        ProcessBuilder builder=new ProcessBuilder();
        if(ffmpegPath !=null){
            Map<String,String> env=builder.environment();
            env.put("ffmpegPath", ffmpegPath);
        }

        builder.directory(inputDirectory);
        builder.command("ffmpeg",
                "-framerate",Integer.toString(imageFrameRate),
                "-start_number",Integer.toString(startNumber),
                "-i",imageFileNameReg,
                "-c:v","libx264",
                "-r",Integer.toString(videoFrameRate),
                "-pix_fmt","yuv420p",
                videoFile.getAbsolutePath());
        builder.redirectErrorStream(true);
        Process p = builder.start();
        String output=Utils.inputStreamToString(p.getInputStream());
        int returnValue=p.waitFor();
        if(returnValue==0){
            return true;
        }
        System.out.println("ffmpeg ended with return value "+returnValue+" output:");
        System.out.println(output);
        return false;
    }
}
