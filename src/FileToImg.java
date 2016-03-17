import net.fec.openrq.EncodingPacket;
import net.fec.openrq.OpenRQ;
import net.fec.openrq.encoder.DataEncoder;
import net.fec.openrq.encoder.SourceBlockEncoder;
import net.fec.openrq.parameters.FECParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    int frameWhiteBlock =8;
    int frameBlackBlock =1;
    int frameVaryFirstBlock =1;
    int frameVarySecondBlock =1;
    int contentBlock =80;
    int blockLength=6;
    int ecSymbol=80;
    int ecSymbolBitLength=10;
    int fileByteNum;
    public static void main(String[] args){
        String inputFilePath="/Users/zhantong/Desktop/test.txt";
        String outputImageDirectory="/Users/zhantong/Desktop/test5/";
        FileToImg f=new FileToImg();
        List<byte[]> byteBuffer=f.readFile(inputFilePath);
        List<BitSet> bitSets=f.RSEncode(byteBuffer);
        f.toImage(bitSets,outputImageDirectory);
    }
    public List<byte[]> readFile(String filePath){
        List<byte[]> buffer=new LinkedList<>();
        Path path= Paths.get(filePath);
        byte[] byteData=null;
        try {
            byteData = Files.readAllBytes(path);
            fileByteNum=byteData.length;
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(String.format("file is %d bytes",fileByteNum));
        int realByteLength= contentBlock * contentBlock /8- ecSymbol * ecSymbolBitLength /8-8;
        FECParameters parameters=FECParameters.newParameters(fileByteNum,realByteLength,1);
        assert byteData!=null;
        DataEncoder dataEncoder= OpenRQ.newEncoder(byteData,parameters);
        System.out.println(String.format("RaptorQ: total %d bytes; %d source blocks; %d bytes per frame",
                parameters.dataLength(),dataEncoder.numberOfSourceBlocks(),parameters.symbolSize()));
        for(SourceBlockEncoder sourceBlockEncoder:dataEncoder.sourceBlockIterable()){
            System.out.println(String.format("source block %d: contains %d source symbols",
                    sourceBlockEncoder.sourceBlockNumber(),sourceBlockEncoder.numberOfSourceSymbols()));
            for(EncodingPacket encodingPacket:sourceBlockEncoder.sourcePacketsIterable()){
                byte[] encode=encodingPacket.asArray();
                buffer.add(encode);
            }
        }
        buffer.remove(buffer.size()-1);
        SourceBlockEncoder lastSourceBlock=dataEncoder.sourceBlock(dataEncoder.numberOfSourceBlocks()-1);
        buffer.add(lastSourceBlock.repairPacket(lastSourceBlock.numberOfSourceSymbols()).asArray());
        int repairNum=buffer.size()/2;
        for(int i=1;i<=repairNum;i++){
            for(SourceBlockEncoder sourceBlockEncoder:dataEncoder.sourceBlockIterable()){
                byte[] encode=sourceBlockEncoder.repairPacket(sourceBlockEncoder.numberOfSourceSymbols()+i).asArray();
                buffer.add(encode);
            }
        }
        System.out.println(String.format("generated %d symbols (the last 1 source symbol is dropped)",buffer.size()));
        return buffer;
    }
    public List<BitSet> RSEncode(List<byte[]> byteBuffer){
        final boolean record=false;
        LinkedList<int[]> recordList;
        String recordFilePath="test.txt";
        if(record) {recordList = new LinkedList<>();}
        List<BitSet> bitSets=new LinkedList<>();
        ReedSolomonEncoder encoder=new ReedSolomonEncoder(GenericGF.AZTEC_DATA_10);
        for(byte[] b:byteBuffer){
            int[] ordered=new int[contentBlock * contentBlock / ecSymbolBitLength];
            for(int i=0;i<b.length*8;i++){
                if((b[i/8]&(1<<(i%8)))>0){
                    ordered[i/ ecSymbolBitLength]|=1<<(i% ecSymbolBitLength);
                }
            }
            encoder.encode(ordered, ecSymbol);
            if(record){recordList.add(ordered);}
            BitSet bitSet=new BitSet();
            for(int i = 0; i< contentBlock * contentBlock; i++){
                if((ordered[i/ ecSymbolBitLength]&(1<<(i% ecSymbolBitLength)))>0){
                    bitSet.set(i);
                }
            }
            bitSets.add(bitSet);
        }
        if(record) {
            try {
                saveToFile(recordList,recordFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitSets;
    }
    public void saveToFile(Object object,String filePath) throws IOException{
        ObjectOutputStream outputStream;
        outputStream = new ObjectOutputStream(new FileOutputStream(filePath));
        outputStream.writeObject(object);
    }
    public void toImage(List<BitSet> bitSets,String directory){
        String imgType="png";
        int imgWidth=(frameWhiteBlock + frameBlackBlock + frameVaryFirstBlock + frameVarySecondBlock)*2+ contentBlock;
        int imgHeight=(frameWhiteBlock + frameBlackBlock)*2+ contentBlock;
        checkDirectory(directory);
        int i=0;
        for(BitSet bitSet:bitSets){
            i++;
            DrawImage img=new DrawImage(imgWidth,imgHeight,blockLength);
            img.setDefaultColor("black");
            addContent(img,bitSet);
            addVary(img,i);
            addFrame(img);
            addHead(img,genHead(fileByteNum));
            String destPath=String.format("%s%06d.%s",directory,i,imgType);
            try {
                img.save(imgType,destPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("save images done");
    }
    public void checkDirectory(String directory){
        File folder=new File(directory);
        boolean b=false;
        if(!folder.exists()){
            b=folder.mkdirs();
        }
        if(b){
            System.out.println("Directory successfully created");
        }else{
            System.out.println("Directory already exists");
        }
    }
    public void addContent(DrawImage img,BitSet content){
        final int contentLeftOffset=frameWhiteBlock + frameBlackBlock + frameVaryFirstBlock + frameVarySecondBlock;
        final int contentTopOffset=frameWhiteBlock + frameBlackBlock;
        final int contentRightOffset=contentLeftOffset+ contentBlock;
        final int contentBottomOffset=contentTopOffset+ contentBlock;
        int index = 0;
        for (int y = contentTopOffset; y < contentBottomOffset; y++) {
            for (int x = contentLeftOffset; x < contentRightOffset; x++) {
                if(!content.get(index)){
                    img.fillRect(x,y,1,1);
                }
                index++;
            }
        }
    }
    public void addVary(DrawImage img,int index){
        final int leftVaryLeftOffset=frameWhiteBlock + frameBlackBlock;
        final int rightVaryLeftOffset=leftVaryLeftOffset+frameVaryFirstBlock + frameVarySecondBlock+contentBlock;
        final int varyTopOffset=frameWhiteBlock + frameBlackBlock;
        final int varyBottomOffset=varyTopOffset+contentBlock;
        if(index%2==0){
            img.fillRect(leftVaryLeftOffset,varyTopOffset,frameVaryFirstBlock,contentBlock);
            img.fillRect(rightVaryLeftOffset,varyTopOffset,frameVaryFirstBlock,contentBlock);
        }else {
            img.fillRect(leftVaryLeftOffset + frameVaryFirstBlock,varyTopOffset,frameVarySecondBlock,contentBlock);
            img.fillRect(rightVaryLeftOffset + frameVaryFirstBlock,varyTopOffset,frameVarySecondBlock,contentBlock);
        }
    }
    public void addFrame(DrawImage img){
        int frameLeftOffset= frameWhiteBlock;
        int frameTopOffset=frameLeftOffset;
        int frameRightOffset=frameLeftOffset+2*(frameBlackBlock + frameVaryFirstBlock + frameVarySecondBlock)+ contentBlock;
        int frameBottomOffset=frameTopOffset+2* frameBlackBlock + contentBlock;
        for(int i=frameTopOffset;i<frameBottomOffset;i+=2*frameBlackBlock){
            img.fillRect(frameLeftOffset,i,frameBlackBlock,frameBlackBlock);
        }
        img.fillRect(frameLeftOffset,frameBottomOffset-frameBlackBlock,frameRightOffset-frameLeftOffset,frameBlackBlock);
        img.fillRect(frameRightOffset-frameBlackBlock,frameTopOffset,frameBlackBlock,frameBottomOffset-frameTopOffset);
    }
    public void addHead(DrawImage img, String head) {
        int headTopOffset = frameWhiteBlock;
        int headLeftOffset = headTopOffset;
        int headRightOffset = headLeftOffset + 2 * (frameBlackBlock + frameVaryFirstBlock + frameVarySecondBlock) + contentBlock;
        int i;
        for (i = 0; i < head.length(); i++) {
            if (head.charAt(i) == '0') {
                img.fillRect(headLeftOffset + i * frameBlackBlock, headTopOffset, frameBlackBlock, frameBlackBlock);
            }
        }
        i=headLeftOffset + i * frameBlackBlock;
        img.fillRect(i,headTopOffset,headRightOffset-i,frameBlackBlock);
    }
    public String genHead(int x){
        String pad32=String.format("%032d",0);
        String Pad8=String.format("%08d",0);
        CRC8 crc=new CRC8();
        crc.update(x);
        String c=Integer.toBinaryString((int)crc.getValue());
        String s=Integer.toBinaryString(x);
        return pad32.substring(s.length())+s+Pad8.substring(c.length())+c;
    }
}
