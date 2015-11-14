import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by zhantong on 15/11/11.
 */
public class ImgToFile extends FileToImg{
    public static void main(String[] args){
//        String inPath="/Users/zhantong/Desktop/t.png";
//        String outPath="/Users/zhantong/Desktop/t.txt";
//        File infile=new File(inPath);
//        ImgToFile imgToFile=new ImgToFile();
//        int[] binaryStream=imgToFile.imgToBinaryStream(infile);
//        File outFile=new File(outPath);
//        imgToFile.binaryStreamToFile(binaryStream,outFile);
        File outFile=new File("/Users/zhantong/Desktop/t.txt");
        ImgToFile imgToFile=new ImgToFile();
        imgToFile.imgsToFile("/Users/zhantong/Desktop/test1",outFile);
    }
    public void imgsToFile(String imgsPath,File file){
        File root=new File(imgsPath);
        File[] imgs=root.listFiles();
        int[] buffer=new int[imgs.length*2500];
        int[] last={};
        int count=0;
        for(File img:imgs){
            int[] t={};
            try {
                t = imgToBinaryStream(img);
            }catch (NotFoundException e){
                System.out.println("Code image not found!");
            }
            if(t==last){
                continue;
            }
            last=t;
            System.out.println(buffer.length+" "+t.length);
            System.arraycopy(t,0,buffer,count*2500,t.length);
            count++;
            System.out.println("DONE!");
        }
        binaryStreamToFile(buffer,file);
    }
    public int[] imgToBinaryStream(File file) throws NotFoundException{
        BufferedImage img=null;
        try {
            img = ImageIO.read(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        int[][] biMatrix=Binarizer.binarizer(img);
        int[] border=FindBoarder.findBoarder(biMatrix);
        int imgWidth=(frameBlackLength+frameVaryLength)*2+contentLength;
        GridSampler gs=new GridSampler();
        int[][] matrixStream=gs.sampleGrid(biMatrix,imgWidth,imgWidth,0,0,imgWidth,0,imgWidth,imgWidth,0,imgWidth,border[0],border[1],border[2],border[3],border[4],border[5],border[6],border[7]);
        return matrixToBinaryStream(matrixStream);
    }
    public int[] matrixToBinaryStream(int[][] biMatrix){
        int startOffset=frameBlackLength+frameVaryLength;
        int stopOffset=startOffset+contentLength;
        System.out.println(startOffset+" "+stopOffset);
        int[] result=new int[contentLength*contentLength];
        int index=0;
        for(int j=startOffset;j<stopOffset;j++){
            for(int i=startOffset;i<stopOffset;i++){
                result[index++]=biMatrix[i][j];
            }
        }
        return result;
    }
    public void binaryStreamToFile(int[] binaryStream,File file){
        int stopIndex=0;
        for(int i=binaryStream.length-1;i>0;i--){
            if(binaryStream[i]==0){
                stopIndex=i;
                break;
            }
        }
        byte[] target=new byte[stopIndex/8];
        for(int i=0;i<stopIndex;i++){
            if(binaryStream[i]==1) {
                switch (i%8){
                    case 0:
                        target[i/8]= (byte) ((int)target[i/8] | 0x80);
                        break;
                    case 1:
                        target[i/8]= (byte) ((int)target[i/8] | 0x40);
                        break;
                    case 2:
                        target[i/8]= (byte) ((int)target[i/8] | 0x20);
                        break;
                    case 3:
                        target[i/8]= (byte) ((int)target[i/8] | 0x10);
                        break;
                    case 4:
                        target[i/8]= (byte) ((int)target[i/8] | 0x8);
                        break;
                    case 5:
                        target[i/8]= (byte) ((int)target[i/8] | 0x4);
                        break;
                    case 6:
                        target[i/8]= (byte) ((int)target[i/8] | 0x2);
                        break;
                    case 7:
                        target[i/8]= (byte) ((int)target[i/8] | 0x1);
                }
            }
        }
        OutputStream os;
        try{
            os=new FileOutputStream(file);
            os.write(target);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
