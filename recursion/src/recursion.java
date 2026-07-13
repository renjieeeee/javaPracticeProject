import java.util.function.Consumer;

public class recursion {
    //递归阶乘
    public static int mutiply(int n){
        if(n == 1){
            return 1;
        }

        return n* mutiply(n-1);
    }

    //递归字符串反转
    public static void ReverseString(int start, String s, Consumer<String> consumer){
        if(start==s.length()){
            return;
        }
        ReverseString(start+1,s,consumer);
        consumer.accept(String.valueOf(s.charAt(start)));
    }

    public static int search(int[] arr,int target){

        return BinarySearch(arr,target,0,arr.length-1);
    }

    //递归二分查找
    private static int BinarySearch(int[] arr,int target,int i,int j){
        if(i>j){
            return -1;
        }
        int mid=(i+j)>>>1;
        if(arr[mid]<target){
            BinarySearch(arr,target,mid+1,j);
        }
        if (target<arr[mid]) {
            BinarySearch(arr,target,i,mid-1);
        }else return mid;
        return -1;
    }

    //递归冒泡排序
    public static int[] BubbleSort(int[] arr){
        bubble(arr,arr.length-1);
        return arr;
    }
    //l为排序的右边界

    private static void bubble(int[] arr,int l){
        int temp;
        if(l==1){
            return;
        }

        for(int i=0;i<l;i++){
            if(arr[i]>arr[i+1]) {
                temp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = temp;
            }
        }
        bubble(arr,l-1);
        return;
    }

    //递归十进制转二进制
    public static String toBinaryString(int i){
        if (i==0) {
            return "0";
        }
        StringBuffer s=new StringBuffer();
        toBinary(i,s);
        return s.toString();
    }
    private static void toBinary(int i,StringBuffer s){
        int y;
        if(i==0){
            return;
        }
        y=i%2;
        i=i/2;
        toBinary(i,s);
        s.append(y);
    }
}
