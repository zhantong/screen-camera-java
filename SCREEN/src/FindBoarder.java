/**
 * Created by zhantong on 15/11/11.
 */
public class FindBoarder {
    public static boolean containsBlack(int[][] biMatrix,int a,int b,int fixed,boolean horizontal){
        if(horizontal){
            for(int x=a;x<=b;x++){
                if(biMatrix[x][fixed]==0){
                    return true;
                }

            }
        }
        else{
            for(int y=a;y<=b;y++){
                if(biMatrix[fixed][y]==0){
                    return true;
                }
            }
        }
        return false;
    }
    public static int[] findBoarder(int[][] biMatrix){
        int init=30;
        int left=biMatrix.length/2-init;
        int right=biMatrix.length/2+init;
        int up=biMatrix[0].length/2-init;
        int down=biMatrix[0].length/2+init;
        boolean flag;
        while(true){
            flag=false;
            while(containsBlack(biMatrix,up,down,right,false)){
                right++;
                flag=true;

            }
            while(containsBlack(biMatrix,left,right,down,true)){
                down++;
                flag=true;
            }
            while(containsBlack(biMatrix,up,down,left,false)){
                left--;
                flag=true;
            }
            while(containsBlack(biMatrix,left,right,up,true)){
                up--;
                flag=true;
            }
            if(!flag){
                break;
            }
        }
        int x0=0,y0=0,x1=0,y1=0,x2=0,y2=0,x3=0,y3=0;
        left++;
        int upTemp=up;
        int downTemp=down;
        while(upTemp!=downTemp){
            upTemp++;
            if(biMatrix[left][upTemp]==0){
                x0=left;
                y0=upTemp;
                break;
            }
            downTemp--;
            if(biMatrix[left][downTemp]==0){
                x3=left;
                y3=downTemp;
                break;
            }
        }
        right--;
        upTemp=up;
        downTemp=down;
        while(upTemp!=downTemp){
            upTemp++;
            if(biMatrix[right][upTemp]==0){
                x1=right;
                y1=upTemp;
                break;
            }
            downTemp--;
            if(biMatrix[right][downTemp]==0){
                x2=right;
                y2=downTemp;
                break;
            }
        }
        up++;
        int leftTemp=left;
        int rightTemp=right;
        while(leftTemp!=rightTemp){
            leftTemp++;
            if(biMatrix[leftTemp][up]==0){
                x0=leftTemp;
                y0=up;
                break;
            }
            rightTemp--;
            if(biMatrix[rightTemp][up]==0){
                x1=rightTemp;
                y1=up;
                break;
            }
        }
        down--;
        leftTemp=left;
        rightTemp=right;
        while(leftTemp!=rightTemp){
            leftTemp++;
            if(biMatrix[leftTemp][down]==0){
                x3=leftTemp;
                y3=down;
                break;
            }
            rightTemp--;
            if(biMatrix[rightTemp][down]==0){
                x2=rightTemp;
                y2=down;
                break;
            }
        }
        //System.out.println(x0+" "+y0+" "+x1+" "+y1+" "+x2+" "+y2+" "+x3+" "+y3);
        return new int[] {x0,y0,x1,y1,x2,y2,x3,y3};
    }
}
