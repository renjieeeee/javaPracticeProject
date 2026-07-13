import java.util.function.Consumer;

class test {
    static void main() {
        //字符串反转测试

        String s = "RJNB";
        recursion.ReverseString(0, s, (value) -> {
            System.out.println(value);
        });

        //二分查找测试
        int[] arr={1,3,5,5,6};
        System.out.println(recursion.search(arr, 6));



    }

}