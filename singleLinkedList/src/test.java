public class test {
    static void main() {
        singleLinkList s= new singleLinkList();
        s.addFirst(4);
        s.addFirst(3);
        s.addFirst(2);
        s.addFirst(1);
        s.addLast(5);

        s.loop((value)->System.out.print(value+" "));

        s.insert(7,2);
        s.removeLast();
        System.out.println(s.get(3));

        s.loop((value)->System.out.print(value+" "));

    }
}
