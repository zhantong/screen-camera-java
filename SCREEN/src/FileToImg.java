import net.fec.openrq.EncodingPacket;
import net.fec.openrq.OpenRQ;
import net.fec.openrq.encoder.DataEncoder;
import net.fec.openrq.encoder.SourceBlockEncoder;
import net.fec.openrq.parameters.FECParameters;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhantong on 15/11/11.
 */
public class FileToImg {
    int frameWhiteLength=8;
    int frameBlackLength=1;
    int frameVaryLength=1;
    int frameVaryTwoLength=1;
    int contentLength=80;
    int blockLength=6;
    int grayCodeLength=10;
    int ecNum=80;
    int ecLength=10;
    int fileByteNum;
    public static void main(String[] args){
        FileToImg f=new FileToImg();
        List<BitSet> s=f.readFile("/Users/zhantong/Desktop/test.txt");
        f.toImage(s,"/Users/zhantong/Desktop/test12/");
    }
    public List<BitSet> readFile(String filePath){
        List<byte[]> buffer=new LinkedList<>();
        List<BitSet> bitSets=new LinkedList<>();
        Path path= Paths.get(filePath);
        byte[] byteData=null;
        try {
            byteData = Files.readAllBytes(path);
            fileByteNum=byteData.length;
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("file byte number:"+fileByteNum);
        int realByteLength=contentLength*contentLength/8-ecNum*ecLength/8-8;
        //FECParameters parameters=FECParameters.newParameters(fileByteNum,realByteLength,fileByteNum/(realByteLength*10)+1);
        FECParameters parameters=FECParameters.newParameters(fileByteNum,realByteLength,1);
        System.out.println(parameters.toString());
        System.out.println("length:"+fileByteNum+"\tblock length:"+realByteLength+"\tblocks:"+parameters.numberOfSourceBlocks());
        assert byteData!=null;
        DataEncoder dataEncoder= OpenRQ.newEncoder(byteData,parameters);
        int count=0;
        for(SourceBlockEncoder sourceBlockEncoder:dataEncoder.sourceBlockIterable()){
            for(EncodingPacket encodingPacket:sourceBlockEncoder.sourcePacketsIterable()){
                byte[] encode=encodingPacket.asArray();
                buffer.add(encode);
                System.out.println("packet length:"+encode.length);
            }
            System.out.println(++count);
        }
        buffer.remove(buffer.size()-1);
        buffer.add(dataEncoder.sourceBlock(dataEncoder.numberOfSourceBlocks()-1).repairPacket(dataEncoder.sourceBlock(dataEncoder.numberOfSourceBlocks()-1).numberOfSourceSymbols()).asArray());
        int repairNum=buffer.size()/2;
        for(int i=1;i<=repairNum;i++){
            for(SourceBlockEncoder sourceBlockEncoder:dataEncoder.sourceBlockIterable()){
                byte[] encode=sourceBlockEncoder.repairPacket(sourceBlockEncoder.numberOfSourceSymbols()+i).asArray();
                buffer.add(encode);
                System.out.println("packet length:"+encode.length);
            }
        }

        LinkedList<int[]> list=new LinkedList<>();

        //ReedSolomonEncoder encoder=new ReedSolomonEncoder(GenericGF.DATA_MATRIX_FIELD_256);
        ReedSolomonEncoder encoder=new ReedSolomonEncoder(GenericGF.AZTEC_DATA_10);
        for(byte[] b:buffer){
            int[] ordered=new int[contentLength*contentLength/ecLength];
            for(int i=0;i<b.length*8;i++){
                if((b[i/8]&(1<<(i%8)))>0){
                    ordered[i/ecLength]|=1<<(i%ecLength);
                }
            }
            encoder.encode(ordered,ecNum);
            list.add(ordered);
            BitSet bitSet=new BitSet();
            for(int i=0;i<contentLength*contentLength;i++){
                if((ordered[i/ecLength]&(1<<(i%ecLength)))>0){
                    bitSet.set(i);
                }
            }
            bitSets.add(bitSet);
        }

        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream("test.txt"));
            outputStream.writeObject(list);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitSets;
    }
    public void toImage(List<BitSet> bitSets,String path){
        String imgType="png";
        int length=((frameWhiteLength+frameBlackLength+frameVaryLength+frameVaryTwoLength)*2+contentLength)*blockLength;
        int startOffset=(frameWhiteLength+frameBlackLength+frameVaryLength+frameVaryTwoLength)*blockLength;
        int stopOffset=startOffset+contentLength*blockLength;
        int i=0;
        for(BitSet bitSet:bitSets){
            i++;
            BufferedImage img = new BufferedImage(length, length, BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D g = img.createGraphics();
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, length, length);
            g.setColor(Color.BLACK);
            int index = 0;
            for (int y = startOffset; y < stopOffset; y += blockLength) {
                for (int x = startOffset; x < stopOffset; x += blockLength) {
                    if(!bitSet.get(index)){
                        g.fillRect(x, y, blockLength, blockLength);
                    }
                    index++;
                }
            }
            //System.out.println("index:"+index);
            if(i%2==0){
                g.fillRect((frameWhiteLength + frameBlackLength) * blockLength, (frameWhiteLength + frameBlackLength) * blockLength, blockLength, (contentLength+2*(frameVaryLength+frameVaryTwoLength))*blockLength);
                g.fillRect((frameWhiteLength+2*frameVaryLength+frameVaryTwoLength+contentLength)*blockLength,(frameWhiteLength + frameBlackLength) * blockLength,  blockLength, (contentLength+2*(frameVaryLength+frameVaryTwoLength))*blockLength);
                //g.fillRect((frameWhiteLength+frameBlackLength)*blockLength,(frameWhiteLength+frameBlackLength)*blockLength,(contentLength+frameVaryLength)*blockLength,blockLength);
            }else {
                g.fillRect((frameWhiteLength+frameBlackLength+2*frameVaryLength+frameVaryTwoLength+contentLength)*blockLength,(frameWhiteLength + frameBlackLength) * blockLength,  blockLength, (contentLength+2*(frameVaryLength+frameVaryTwoLength))*blockLength);
                g.fillRect((frameWhiteLength + frameBlackLength+frameVaryLength) * blockLength, (frameWhiteLength + frameBlackLength) * blockLength, blockLength, (contentLength+2*(frameVaryLength+frameVaryTwoLength))*blockLength);
                //g.fillRect((frameWhiteLength+frameBlackLength)*blockLength,stopOffset,(contentLength+frameVaryLength)*blockLength,blockLength);
            }
            //g.fillRect(stopOffset,(frameWhiteLength + frameBlackLength) * blockLength,  blockLength, contentLength*blockLength);
            addFrame(g);
            //addGrayCode(g,CRC8.toString(i)+imgAmountString);
            addGrayCode(g,CRC8.toString(fileByteNum));
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
        int stopOffset=startOffset+(contentLength+2*(frameVaryLength+frameVaryTwoLength))*blockLength;
        int vBlockLength=frameVaryLength*blockLength;
        for(int i=startOffset;i<stopOffset;i+=vBlockLength*2){
            //g.fillRect(i,startOffset,vBlockLength,vBlockLength);
            //g.fillRect(startOffset,i,vBlockLength,vBlockLength);
            //g.fillRect(stopOffset,i,vBlockLength,vBlockLength);
            g.fillRect(frameWhiteLength*blockLength,i,vBlockLength,vBlockLength);
            //g.fillRect(i,stopOffset,vBlockLength,vBlockLength);
        }
        startOffset=frameWhiteLength*blockLength;
        stopOffset=startOffset+(2*(frameBlackLength+frameVaryLength+frameVaryTwoLength)+contentLength)*blockLength;
        int bBlockLength=frameBlackLength*blockLength;
        //g.fillRect(startOffset,startOffset,bBlockLength,stopOffset-startOffset);
        //g.fillRect(startOffset,startOffset,stopOffset-startOffset,bBlockLength);
        g.fillRect(startOffset,stopOffset-bBlockLength,stopOffset-startOffset,bBlockLength);
        g.fillRect(stopOffset-bBlockLength,startOffset,bBlockLength,stopOffset-startOffset);
    }
    public void addGrayCode(Graphics2D g,String grayCode){
        //System.out.println(grayCode);
        //int startOffset=(frameWhiteLength+frameBlackLength)*blockLength;
        int startOffset=frameWhiteLength*blockLength;
        int stopOffset=startOffset+(contentLength+frameVaryLength+frameVaryTwoLength*2+frameBlackLength)*blockLength;
        int vBlockLength=frameVaryLength*blockLength;
        int i;
        for(i=0;i<grayCode.length();i++){
            if(grayCode.charAt(i)=='0'){
                //g.fillRect(startOffset+(frameVaryLength+1)*blockLength+i*vBlockLength,startOffset,vBlockLength,vBlockLength);
                g.fillRect(startOffset+i*vBlockLength,frameWhiteLength*blockLength,vBlockLength,vBlockLength);
            }
        }

        i=startOffset+i*vBlockLength;
        for(;i<=stopOffset;i+=vBlockLength){
            g.fillRect(i,frameWhiteLength*blockLength,vBlockLength,vBlockLength);
        }

    }
}
