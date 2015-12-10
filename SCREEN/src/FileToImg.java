import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by zhantong on 15/11/11.
 */
public class FileToImg {
    int frameWhiteLength=8;
    int frameBlackLength=1;
    int frameVaryLength=1;
    int contentLength=76;
    int blockLength=10;
    int grayCodeLength=10;
    int ecByteNum=38;
    public static void main(String[] args){
        FileToImg f=new FileToImg();
        f.toImage(f.readFile("/Users/zhantong/Desktop/test1.txt"),"/Users/zhantong/Desktop/test8/");
    }
    public String readFile(String path){
        File inFile=new File(path);
        FileInputStream fileInputStream=null;
        try{
            fileInputStream=new FileInputStream(inFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        StringBuffer stringBuffer=new StringBuffer();
        int total_length=0;
        int byte_length=0;
        try{
            int i;
            int length=contentLength*contentLength/8-ecByteNum;
            int ecNum=ecByteNum;
            byte[] b=new byte[length];
            int[] c=new int[length+ecNum];
            ReedSolomonEncoder encoder=new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256);
            Boolean flag=false;
            while(true){
                if((i=fileInputStream.read(b))!=length){
                    b[i++]=-128;
                    for(int index=i;index<length;index++){
                        b[index]=0;
                    }
                    flag=true;
                }
                byte_length+=i;
                total_length+=i*8;
                for(int j=0;j<length;j++){
                    c[j]=b[j]&0xff;
                }
                encoder.encode(c,ecNum);
                for(int k:c){
                    String s = Integer.toBinaryString(k);
                    int temp=Integer.parseInt(s);
                    stringBuffer.append(String.format("%1$08d",temp));
                }
                if(flag){
                    break;
                }
            }
            /*
            while((i=fileInputStream.read())!=-1) {
                String b = Integer.toBinaryString(i);
                int temp=Integer.parseInt(b);
                stringBuffer.append(String.format("%1$08d",temp));
                //System.out.println(i);
            }
            */
            System.out.println("total length:"+total_length);
            System.out.println("byte length:"+byte_length);
            return stringBuffer.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public void toImage(String biData,String path){
        String imgType="png";
        int length=((frameWhiteLength+frameBlackLength+frameVaryLength)*2+contentLength)*blockLength;
        int startOffset=(frameWhiteLength+frameBlackLength+frameVaryLength)*blockLength;
        int stopOffset=startOffset+contentLength*blockLength;
        int biDataLength=biData.length();
        int imgAmount=(int)Math.ceil((double)biDataLength/(contentLength*contentLength));
        int index = 0;
        //GrayCode grayCode=new GrayCode(grayCodeLength);
        String imgAmountString=CRC8.toString(imgAmount);
        for(int i=1;i<=imgAmount;i++) {
            BufferedImage img = new BufferedImage(length, length, BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D g = img.createGraphics();
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, length, length);
            g.setColor(Color.BLACK);

            boolean flag = true;
            for (int y = startOffset; y < stopOffset; y += blockLength) {
                for (int x = startOffset; x < stopOffset; x += blockLength) {
                    if (index < biDataLength) {
                        if (biData.charAt(index) == '0') {
                            g.fillRect(x, y, blockLength, blockLength);
                        }
                        index++;
                    } else if (flag) {
                        g.fillRect(x, y, blockLength, blockLength);
                        flag = false;
                    }
                }
            }
            addFrame(g);
            addGrayCode(g,CRC8.toString(i)+imgAmountString);
            g.dispose();
            img.flush();
            String destPath=String.format("%s%06d.%s",path,i,imgType);
            File destFile = new File(destPath);
            try {
                ImageIO.write(img, imgType, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addFrame(Graphics2D g){
        int startOffset=(frameWhiteLength+frameBlackLength)*blockLength;
        int stopOffset=startOffset+(contentLength+frameVaryLength)*blockLength;
        int vBlockLength=frameVaryLength*blockLength;
        for(int i=startOffset;i<stopOffset;i+=vBlockLength*2){
            //g.fillRect(i,startOffset,vBlockLength,vBlockLength);
            g.fillRect(startOffset,i,vBlockLength,vBlockLength);
            g.fillRect(stopOffset,i,vBlockLength,vBlockLength);
            g.fillRect(i,stopOffset,vBlockLength,vBlockLength);
        }
        startOffset=frameWhiteLength*blockLength;
        stopOffset=startOffset+(2*(frameBlackLength+frameVaryLength)+contentLength)*blockLength;
        int bBlockLength=frameBlackLength*blockLength;
        g.fillRect(startOffset,startOffset,bBlockLength,stopOffset-startOffset);
        g.fillRect(startOffset,startOffset,stopOffset-startOffset,bBlockLength);
        g.fillRect(startOffset,stopOffset-bBlockLength,stopOffset-startOffset,bBlockLength);
        g.fillRect(stopOffset-bBlockLength,startOffset,bBlockLength,stopOffset-startOffset);
    }
    public void addGrayCode(Graphics2D g,String grayCode){
        System.out.println(grayCode);
        int startOffset=(frameWhiteLength+frameBlackLength)*blockLength;
        int stopOffset=startOffset+(contentLength+frameVaryLength)*blockLength;
        int vBlockLength=frameVaryLength*blockLength;
        int i;
        for(i=0;i<grayCode.length();i++){
            if(grayCode.charAt(i)=='0'){
                g.fillRect(startOffset+i*vBlockLength,startOffset,vBlockLength,vBlockLength);
            }
        }
        i=startOffset+i*vBlockLength;
        for(;i<stopOffset;i+=vBlockLength){
            g.fillRect(i,startOffset,vBlockLength,vBlockLength);
        }
    }
}
