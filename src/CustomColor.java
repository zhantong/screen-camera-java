/**
 * Created by zhantong on 2016/12/2.
 */
public class CustomColor {
    public static final CustomColor Y0U0V0=new CustomColor(0,0,0);
    public static final CustomColor Y0U0V1=new CustomColor(0,0,255);
    public static final CustomColor Y0U1V0=new CustomColor(0,255,0);
    public static final CustomColor Y0U1V1=new CustomColor(0,255,255);
    public static final CustomColor Y1U0V0=new CustomColor(255,0,0);
    public static final CustomColor Y1U0V1=new CustomColor(255,0,255);
    public static final CustomColor Y1U1V0=new CustomColor(255,255,0);
    public static final CustomColor Y1U1V1=new CustomColor(255,255,255);
    public static final CustomColor BLACK=new CustomColor(0,0,0);
    public static final CustomColor WHITE=new CustomColor(255,255,255);
    private int value;
    public CustomColor(int y,int u,int v){
        value = ((y & 0xFF) << 16) |
                ((u & 0xFF) << 8)  |
                ((v & 0xFF) << 0);
    }
    public int getYUV(){
        return value;
    }
    public int getRGB(){
        return value;
    }
    public int getY(){
        return (getYUV()>>16)&0xff;
    }
    public int getU(){
        return (getYUV()>>8)&0xff;
    }
    public int getV(){
        return (getYUV()>>0)&0xff;
    }
}
