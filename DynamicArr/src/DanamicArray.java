import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class DanamicArray implements Iterable<Integer>{
    private int size=0;
    private int capacity=8;
    private int[] array={};

    public void addLast(int element){
        checkCapcity();
        array[size]=element;
        size++;
    }

    public void add(int index,int element){
        checkCapcity();
        if(index>=0&&index<=size){
            System.arraycopy(array,index,array,index+1,size-index);
            array[index]=element;
            size++;
        }
    }

    private void checkCapcity() {
        if(size==0){
            array=new int[capacity];
        }
        if(size==capacity){
            capacity+=capacity>>>1;
            int[] newarr=new int[capacity];
            System.arraycopy(array,0,newarr,0,size);
            array=newarr;
        }
    }


    public int get(int index){
        return array[index];
    }

    public void remove(int index){
        System.arraycopy(array,index+1,array,index,size-index-1);
        size-=1;
    }

    //使用函数式接口consumer进行遍历
    public void loop (Consumer<Integer> consumer){
        for(int i=0;i<=size;i++){
            consumer.accept(array[i]);
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>(){
            int pos=0;
            @Override
            public boolean hasNext() {
                return pos<size;
            }

            @Override
            public Integer next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return array[pos++];
            }
        };
    }
}
