import java.io.*;

/**
 * Created by zhantong on 16/3/17.
 */
public class ImagesToVideo {
    private String workingDirectory;
    private ProcessBuilder builder;

    public static void main(String[] args) {
        ImagesToVideo imagesToVideo;
        if (true) {
            imagesToVideo = new ImagesToVideo.YUVImageBuilder()
                    .setWorkdingDirectory("/Users/zhantong/Desktop/ShiftCodeColorML6")
                    .setInputResolution("2000x1880")
                    .setInputFrameRate("22")
                    .setInputFilePath("all.yuv")
                    .setOutputFilePath("40_0.1_22.mp4")
                    .build();
        }
        if (false) {
            imagesToVideo = new ImagesToVideo.CompressedImageBuilder()
                    .setWorkdingDirectory("/Users/zhantong/Desktop/ShiftCodeColorML6")
                    .setInputFileNameRegex("%06d.png")
                    .setInputFrameRate("22")
                    .setOutputFilePath("40_0.1_22.mp4")
                    .build();
        }
        if (imagesToVideo != null) {
            try {
                imagesToVideo.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ImagesToVideo(String workingDirectory, ProcessBuilder builder) {
        this.workingDirectory = workingDirectory;
        this.builder = builder;
    }

    public boolean run() throws IOException, InterruptedException {
        builder.directory(new File(workingDirectory));
        builder.redirectErrorStream(true);
        Process p = builder.start();
        String output = Utils.inputStreamToString(p.getInputStream());
        int returnValue = p.waitFor();
        System.out.println("ffmpeg ended with return value " + returnValue + " output:");
        System.out.println(output);
        return returnValue == 0;
    }

    private static class Builder<T> {
        protected String workdingDirectory = "";

        public T setWorkdingDirectory(String workdingDirectory) {
            this.workdingDirectory = workdingDirectory;
            return (T) this;
        }

        protected void checkParams() {
            if (workdingDirectory.isEmpty()) {
                throw new IllegalArgumentException("empty workdingDirectory");
            }
        }
    }

    public static class CompressedImageBuilder extends Builder<CompressedImageBuilder> {
        private String inputFrameRate = "15";
        private String inputFileNameRegex = "";
        private String inputFileStartIndex = "0";
        private String outputFrameRate = "30";
        private String codec = "libx264";
        private String pixelFormat = "yuv420p";
        private String outputFilePath = "";


        public CompressedImageBuilder setInputFrameRate(String inputFrameRate) {
            this.inputFrameRate = inputFrameRate;
            return this;
        }

        public CompressedImageBuilder setInputFileNameRegex(String inputFileNameRegex) {
            this.inputFileNameRegex = inputFileNameRegex;
            return this;
        }

        public CompressedImageBuilder setInputFileStartIndex(String inputFileStartIndex) {
            this.inputFileStartIndex = inputFileStartIndex;
            return this;
        }

        public CompressedImageBuilder setOutputFrameRate(String outputFrameRate) {
            this.outputFrameRate = outputFrameRate;
            return this;
        }

        public CompressedImageBuilder setCodec(String codec) {
            this.codec = codec;
            return this;
        }

        public CompressedImageBuilder setPixelFormat(String pixelFormat) {
            this.pixelFormat = pixelFormat;
            return this;
        }

        public CompressedImageBuilder setOutputFilePath(String outputFilePath) {
            this.outputFilePath = outputFilePath;
            return this;
        }

        protected void checkParams() {
            super.checkParams();
            if (inputFileNameRegex.isEmpty()) {
                throw new IllegalArgumentException("empty inputFileNameRegex");
            }
            if (outputFilePath.isEmpty()) {
                throw new IllegalArgumentException("empty outputFilePath");
            }
        }

        public ImagesToVideo build() {
            checkParams();
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(workdingDirectory));
            processBuilder.command("ffmpeg",
                    "-framerate", inputFrameRate,
                    "-start_number", inputFileStartIndex,
                    "-i", inputFileNameRegex,
                    "-c:v", codec,
                    "-r", outputFrameRate,
                    "-pix_fmt", pixelFormat,
                    outputFilePath);
            return new ImagesToVideo(workdingDirectory, processBuilder);
        }
    }

    public static class YUVImageBuilder extends Builder<YUVImageBuilder> {
        private String inputFilePath = "";
        private String inputFrameRate = "15";
        private String outputFrameRate = "30";
        private String inputCodec = "rawvideo";
        private String outputCodec = "libx264";
        private String outputFileFormat = "mp4";
        private String inputColorRange = "2";
        private String inputResolution = "";
        private String inputPixelFormat = "yuv420p";
        private String outputFilePath = "";
        private String threads = "auto";

        public YUVImageBuilder setInputFilePath(String inputFilePath) {
            this.inputFilePath = inputFilePath;
            return this;
        }

        public YUVImageBuilder setInputFrameRate(String inputFrameRate) {
            this.inputFrameRate = inputFrameRate;
            return this;
        }

        public YUVImageBuilder setOutputFrameRate(String outputFrameRate) {
            this.outputFrameRate = outputFrameRate;
            return this;
        }

        public YUVImageBuilder setInputCodec(String inputCodec) {
            this.inputCodec = inputCodec;
            return this;
        }

        public YUVImageBuilder setOutputFileFormat(String outputFileFormat) {
            this.outputFileFormat = outputFileFormat;
            return this;
        }

        public YUVImageBuilder setInputResolution(String inputResolution) {
            this.inputResolution = inputResolution;
            return this;
        }

        public YUVImageBuilder setInputColorRange(String inputColorRange) {
            this.inputColorRange = inputColorRange;
            return this;
        }

        public YUVImageBuilder setOutputCodec(String outputCodec) {
            this.outputCodec = outputCodec;
            return this;
        }

        public YUVImageBuilder setInputPixelFormat(String inputPixelFormat) {
            this.inputPixelFormat = inputPixelFormat;
            return this;
        }

        public YUVImageBuilder setOutputFilePath(String outputFilePath) {
            this.outputFilePath = outputFilePath;
            return this;
        }

        protected void checkParams() {
            super.checkParams();
            if (inputFilePath.isEmpty()) {
                throw new IllegalArgumentException("empty inputFilePath");
            }
            if (inputResolution.isEmpty()) {
                throw new IllegalArgumentException("empty inputResolution");
            }
            if (outputFilePath.isEmpty()) {
                throw new IllegalArgumentException("empty outputFilePath");
            }
        }

        public ImagesToVideo build() {
            checkParams();
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(workdingDirectory));
            processBuilder.command("ffmpeg",
                    "-vcodec", inputCodec,
                    "-pix_fmt", inputPixelFormat,
                    "-color_range", inputColorRange,
                    "-s", inputResolution,
                    "-r", inputFrameRate,
                    "-i", inputFilePath,
                    "-f", outputFileFormat,
                    "-c:v", outputCodec,
                    "-r", outputFrameRate,
                    "-threads", threads,
                    outputFilePath);
            return new ImagesToVideo(workdingDirectory, processBuilder);
        }
    }
}
