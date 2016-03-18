# Screen-Camera JAVA
---
## Screen-Camera简介
Screen-Camera可以利用手机摄像头传输文件。其原理是将需要传输的文件编码为动态二维码（即多个二维码组成的视频），由手机摄像头捕获到这些二维码后，解码还原为文件。由于整个传输过程只需要用到手机摄像头，因此而免去了如WiFi和蓝牙传输文件需要网络协议的支持，显得更为简单和便捷。
## JAVA版本
JAVA版本只用来生成二维码，解码部分在[Android版本]中实现。

JAVA版本采用JDK1.7作为开发环境，需要用到外部依赖包OpenRQ。
## 项目导入
由于JAVA版本采用IntelliJ IDEA作为集成开发环境，若使用eclipse进行编辑则可能需要重新设置依赖包等参数。

可以在IntelliJ中使用GitHub插件直接clone本项目，也可以在本项目页面下载zip包，解压后用IntelliJ打开。
## Reference
- ReedSolomon编解码部分用到了[zxing]项目的代码
- RaptorQ的实现用到了[OpenRQ]项目
[Android版本]:https://github.com/zhantong1994/screen-camera-android
[zxing]:https://github.com/zxing/zxing
[OpenRQ]:https://github.com/openrq-team/OpenRQ
