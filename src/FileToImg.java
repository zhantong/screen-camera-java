import ReedSolomon.GenericGF;
import ReedSolomon.ReedSolomonEncoder;
import net.fec.openrq.EncodingPacket;
import net.fec.openrq.OpenRQ;
import net.fec.openrq.encoder.DataEncoder;
import net.fec.openrq.encoder.SourceBlockEncoder;
import net.fec.openrq.parameters.FECParameters;

import java.awt.*;
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
 * 由文件生成多个二维码
 * 文件->RaptorQ编码->多个RaptorQ包->RS编码->多个RS编码包->扩展为BitSet->多个二维码
 * RaptorQ包由source symbol和repair symbol组成
 * 二维码除BitSet提供的内容外,还有另外的辅助信息
 */
public class FileToImg {
    protected int frameWhiteBlock = 8;//边界留白
    protected int frameBlackLength = 1;//黑色边界,最外围
    protected int frameVaryLength = 1;//左边第一个和右边第一个黑/白条
    protected int frameVaryTwoLength = 1;//左边第二个和右边第二个黑/白条
    protected int contentLength = 80;//内容
    protected int blockLength = 6;//小方格边长对应像素点
    protected int ecNum = 80;//RS纠错中用于纠错的symbol个数
    protected int ecLength = 10;//一个symbol对应bit数目,应与RS的decoder参数保持一致
    protected int bitsPerBlock=1;
    protected int fileByteNum;

    private static final int SAVE_AS_PNG=1;
    private static final int SAVE_AS_EPS=2;
    private int saveImageType=SAVE_AS_PNG;

    /**
     * 由输入文件生成多个二维码到指定文件夹
     *
     * @param inputFilePath        输入文件路径
     * @param outputImageDirectory 输出图片文件夹路径
     */
    public void toImg(String inputFilePath, String outputImageDirectory) {
        List<byte[]> byteBuffer = readFile(inputFilePath);
        List<BitSet> bitSets = RSEncode(byteBuffer);
        toImage(bitSets, outputImageDirectory);
    }

    /**
     * 读取输入文件,生成RaptorQ编码后的byte[]组成的list
     * byte[]的长度为二维码容量减去RS纠错部分
     *
     * @param filePath 输入文件路径
     * @return raptorQ编码后的byte[]组成的list
     */
    private List<byte[]> readFile(String filePath) {
        //一个二维码实际存储的文件信息,最后的8byte为RaptorQ头部
        final int realByteLength = bitsPerBlock* contentLength * contentLength / 8 - ecNum * ecLength / 8 - 8;
        final int NUMBER_OF_SOURCE_BLOCKS=1;
        final double REPAIR_PERCENT=0.5;
        List<byte[]> buffer = new LinkedList<>();
        Path path = Paths.get(filePath);
        byte[] byteData = null;
        try {
            byteData = Files.readAllBytes(path);
            fileByteNum = byteData.length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("file is %d bytes", fileByteNum));
        FECParameters parameters = FECParameters.newParameters(fileByteNum, realByteLength, NUMBER_OF_SOURCE_BLOCKS);
        assert byteData != null;
        DataEncoder dataEncoder = OpenRQ.newEncoder(byteData, parameters);
        System.out.println("RaptorQ parameters: "+parameters.toString());
        for (SourceBlockEncoder sourceBlockEncoder : dataEncoder.sourceBlockIterable()) {
            System.out.println(String.format("source block %d: contains %d source symbols",
                    sourceBlockEncoder.sourceBlockNumber(), sourceBlockEncoder.numberOfSourceSymbols()));
            for (EncodingPacket encodingPacket : sourceBlockEncoder.sourcePacketsIterable()) {
                byte[] encode = encodingPacket.asArray();
                buffer.add(encode);
            }
        }
        //因RaptorQ不保证最后一个source symbol的大小为指定大小,而二维码需要指定大小的内容,所以把最后一个source symbol用repair symbol替代
        buffer.remove(buffer.size() - 1);
        SourceBlockEncoder lastSourceBlock = dataEncoder.sourceBlock(dataEncoder.numberOfSourceBlocks() - 1);
        buffer.add(lastSourceBlock.repairPacket(lastSourceBlock.numberOfSourceSymbols()).asArray());
        int repairNum = (int)(buffer.size()*REPAIR_PERCENT);
        for (int i = 1; i <= repairNum; i++) {
            for (SourceBlockEncoder sourceBlockEncoder : dataEncoder.sourceBlockIterable()) {
                byte[] encode = sourceBlockEncoder.repairPacket(sourceBlockEncoder.numberOfSourceSymbols() + i).asArray();
                buffer.add(encode);
            }
        }
        System.out.println(String.format("generated %d symbols (the last 1 source symbol is dropped)", buffer.size()));
        return buffer;
    }

    /**
     * 对RaptorQ编码后内容进行RS编码,并将编码后的内容转换为BitSet
     *
     * @param byteBuffer raptorQ编码后的byte[]组成的list
     * @return RS编码后转换为BitSet组成的list
     */
    private List<BitSet> RSEncode(List<byte[]> byteBuffer) {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder(GenericGF.AZTEC_DATA_10);
        List<BitSet> bitSets = new LinkedList<>();
        for (byte[] b : byteBuffer) {
            int[] ordered = new int[bitsPerBlock* contentLength * contentLength / ecLength];
            for (int i = 0; i < b.length * 8; i++) {
                if ((b[i / 8] & (1 << (i % 8))) > 0) {
                    ordered[i / ecLength] |= 1 << (i % ecLength);
                }
            }
            encoder.encode(ordered, ecNum);
            bitSets.add(toBitSet(ordered, ecLength));
        }
        return bitSets;
    }

    /**
     * 将int[]转换为BitSet,只用到每个int的低bitNum位
     *
     * @param data   int[]
     * @param bitNum 用到的int的低bitNum位
     * @return 转换得到的BitSet
     */
    private static BitSet toBitSet(int data[],int bitNum){
        int index=0;
        BitSet bitSet=new BitSet();
        for(int current:data){
            for(int i=0;i<bitNum;i++){
                if((current&(1<<i))>0){
                    bitSet.set(index);
                }
                index++;
            }
        }
        return bitSet;
    }

    /**
     * 由BitSet生成二维码图片,BitSet只提供内容部分
     *
     * @param bitSets   内容
     * @param directory 生成二维码文件夹路径
     */
    private void toImage(List<BitSet> bitSets, String directory) {
        String imgType;
        switch (saveImageType){
            case SAVE_AS_PNG:
                imgType="png";
                break;
            case SAVE_AS_EPS:
                imgType="eps";
                break;
            default:
                throw new RuntimeException();
        }
        int imgWidth = (frameWhiteBlock + frameBlackLength + frameVaryLength + frameVaryTwoLength) * 2 + contentLength;
        int imgHeight = (frameWhiteBlock + frameBlackLength) * 2 + contentLength;
        String head = genHead(fileByteNum);
        checkDirectory(directory);
        int i = 0;
        for (BitSet bitSet : bitSets) {
            i++;
            //String destPath = String.format("%s%06d.%s", directory, extractEncodingSymbolID(getFecPayloadID(bitSet)), imgType);
            String destPath = String.format("%s%06d.%s", directory, i, imgType);
            DrawImage img;
            switch (saveImageType){
                case SAVE_AS_PNG:
                    img = new DrawImage(imgWidth, imgHeight, blockLength);
                    break;
                case SAVE_AS_EPS:
                    img = new DrawImage(imgWidth, imgHeight, blockLength,destPath);
                    break;
                default:
                    throw new RuntimeException();
            }
            img.setDefaultColor(Color.BLACK);
            addContent(img, bitSet,i);
            if(frameVaryLength!=0&&frameVaryTwoLength!=0) {
                addVary(img, i);
            }
            addFrame(img,i);
            addHead(img, head);

            try {
                switch (saveImageType){
                    case SAVE_AS_PNG:
                        img.save(imgType, destPath);
                        break;
                    case SAVE_AS_EPS:
                        img.save();
                        break;
                    default:
                        throw new RuntimeException();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("save images done");
    }

    /**
     * 检查文件夹路径是否存在,不存在则创建
     *
     * @param directory 文件夹路径
     */
    private void checkDirectory(String directory) {
        File folder = new File(directory);
        boolean b = false;
        if (!folder.exists()) {
            b = folder.mkdirs();
        }
        if (b) {
            System.out.println("Directory successfully created");
        } else {
            System.out.println("Directory already exists");
        }
    }

    /**
     * 将内容加入到二维码中
     *
     * @param img     二维码
     * @param content 内容
     */
    protected void addContent(DrawImage img, BitSet content,int barcodeIndex) {
        int contentLeftOffset = frameWhiteBlock + frameBlackLength + frameVaryLength + frameVaryTwoLength;
        int contentTopOffset = frameWhiteBlock + frameBlackLength;
        int contentRightOffset = contentLeftOffset + contentLength;
        int contentBottomOffset = contentTopOffset + contentLength;
        int index = 0;
        for (int y = contentTopOffset; y < contentBottomOffset; y++) {
            for (int x = contentLeftOffset; x < contentRightOffset; x++) {
                if (!content.get(index)) {
                    img.fillRect(x, y, 1, 1);
                }
                index++;
            }
        }
    }

    /**
     * 将黑/白条加入到二维码,根据二维码的编号确定黑/白条颜色
     *
     * @param img   二维码
     * @param index 二维码编号
     */
    protected void addVary(DrawImage img, int index) {
        img.setDefaultColor(Color.BLACK);
        int leftVaryLeftOffset = frameWhiteBlock + frameBlackLength;
        int rightVaryLeftOffset = leftVaryLeftOffset + frameVaryLength + frameVaryTwoLength + contentLength;
        int varyTopOffset = frameWhiteBlock + frameBlackLength;
        int varyBottomOffset = varyTopOffset + contentLength;
        if (index % 2 == 0) {
            img.fillRect(leftVaryLeftOffset, varyTopOffset, frameVaryLength, contentLength);
            img.fillRect(rightVaryLeftOffset, varyTopOffset, frameVaryLength, contentLength);
        } else {
            img.fillRect(leftVaryLeftOffset + frameVaryLength, varyTopOffset, frameVaryTwoLength, contentLength);
            img.fillRect(rightVaryLeftOffset + frameVaryLength, varyTopOffset, frameVaryTwoLength, contentLength);
        }
    }

    /**
     * 将边框加入到二维码
     *
     * @param img 二维码
     */
    protected void addFrame(DrawImage img,int index) {
        img.setDefaultColor(Color.BLACK);
        int frameLeftOffset = frameWhiteBlock;
        int frameTopOffset = frameLeftOffset;
        int frameRightOffset = frameLeftOffset + 2 * (frameBlackLength + frameVaryLength + frameVaryTwoLength) + contentLength;
        int frameBottomOffset = frameTopOffset + 2 * frameBlackLength + contentLength;
        for (int i = frameTopOffset; i < frameBottomOffset; i += 2 * frameBlackLength) {
            img.fillRect(frameLeftOffset, i, frameBlackLength, frameBlackLength);
        }
        img.fillRect(frameLeftOffset, frameBottomOffset - frameBlackLength, frameRightOffset - frameLeftOffset, frameBlackLength);
        img.fillRect(frameRightOffset - frameBlackLength, frameTopOffset, frameBlackLength, frameBottomOffset - frameTopOffset);
    }

    /**
     * 将头信息加入到二维码
     *
     * @param img  二维码
     * @param head 头信息
     */
    private void addHead(DrawImage img, String head) {
        img.setDefaultColor(Color.BLACK);
        int headTopOffset = frameWhiteBlock;
        int headLeftOffset = headTopOffset;
        int headRightOffset = headLeftOffset + 2 * (frameBlackLength + frameVaryLength + frameVaryTwoLength) + contentLength;
        int i;
        for (i = 0; i < head.length(); i++) {
            if (head.charAt(i) == '0') {
                img.fillRect(headLeftOffset + i * frameBlackLength, headTopOffset, frameBlackLength, frameBlackLength);
            }
        }
        i = headLeftOffset + i * frameBlackLength;
        img.fillRect(i, headTopOffset, headRightOffset - i, frameBlackLength);
    }

    /**
     * 生成头信息,即32位的int内容+8位CRC8校验码
     *
     * @param x 内容
     * @return 32位的int内容+8位CRC8校验码
     */
    private String genHead(int x) {
        String pad32 = String.format("%032d", 0);
        String Pad8 = String.format("%08d", 0);
        CRC8 crc = new CRC8();
        crc.update(x);
        String c = Integer.toBinaryString((int) crc.getValue());
        String s = Integer.toBinaryString(x);
        return pad32.substring(s.length()) + s + Pad8.substring(c.length()) + c;
    }
    public int getFecPayloadID(BitSet bitSet){
        int value=0;
        for (int i = bitSet.nextSetBit(0); i <32; i = bitSet.nextSetBit(i + 1)) {
            value|=(1<<(i%8))<<(3-i/8)*8;
        }
        return value;
    }
    public int extractSourceBlockNumber(int fecPayloadID){
        return fecPayloadID>>24;
    }
    public int extractEncodingSymbolID(int fecPayloadID){
        return fecPayloadID&0x0FFF;
    }
}
