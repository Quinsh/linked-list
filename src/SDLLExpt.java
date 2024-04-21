import java.io.PrintWriter;
import java.util.ListIterator;

public class SDLLExpt {
  public static void main(String[] args) {
    PrintWriter pen = new PrintWriter(System.out, true);

    CDLL<Integer> li = new CDLL<Integer>();
    ListIterator<Integer> iter = li.listIterator();
    ListIterator<Integer> iter2 = li.listIterator();

    iter.add(1);
    iter.add(2);
    
    // iter2.next(); // concurrent excpetioon test

    // do {
    //   Integer element = iter2.next();
    //   pen.println(element);
    // } while (iter2.hasNext());

    pen.flush();
  }
}
