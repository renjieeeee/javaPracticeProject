import java.util.function.Consumer;

public class singleLinkList {
    //建立head节点代表哨兵节点，便于修改链表
    private Node head=new Node(0,null);

    //建立成员内部类Node，表示单向链表的节点，它拥有value和next两个属性
    private static class Node{
        int value;
        Node next;
    //Node类的有参构造
        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    public void addFirst(int value){
        head.next = new Node(value,head.next);
    }

    public void addLast(int value){
        Node Last=findLast();
        Last.next=new Node(value,null);
    }


    public int get(int index){

        Node p= findNode(index);
        return p.value;
    }


    public void insert(int value,int index){

        Node prev = findNode(index-1);
        prev.next=new Node(value,prev.next);
    }

    //* 工具类findNode能通过遍历找到索引节点
    private Node findNode(int index) {
        Node p=head;
        for(int i = -1; p.next != null; p=p.next,i++){
            if (i==index){
              return p;}
        }
        return null;
    }

    private static void iligalIndex() {
        throw new IllegalArgumentException("非法索引");
    }

    public void removeLast(){
        Node p=head.next;
        while(p.next.next!=null){
            p=p.next;
        }
        p.next=null;
    }



    private Node findLast() {
        Node p=head.next;
        while(p.next!=null){
            p=p.next;
        }
        return p;
    }

    //* 方法loop用于遍历链表，传入Consumer消费函数式接口，返回空值，由Consumer的实现类决定执行的代码
    public void loop(Consumer<Integer> consumer){
        for( Node p=head.next;p!=null; p=p.next){
            consumer.accept(p.value);
        }



    }
}
