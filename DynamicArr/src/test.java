import java.util.Iterator;

public class test {
    static void main() {
        DanamicArray arr= new DanamicArray();
        arr.addLast(2);
        arr.addLast(3);
        arr.addLast(4);
        arr.addLast(2);



        //consumer
        arr.loop((value)->System.out.println(value));

        //iterator
        for(Integer element : arr){
            System.out.println(element);
        }
        //两种迭代器写法，强化for（上）简便，经典写法(下)
        Iterator<Integer> it= arr.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }

    }
}
