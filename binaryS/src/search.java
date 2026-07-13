public class search {
    public static int binarysearchleftmost(int[]a ,int t){
        int left=0,right=a.length-1;
        while(left<=right){
            int m=(left+right)>>>1;
            if (a[m]>=t){
                right=m-1;
            }else left=m+1;

        }
        return left;
    }
}
