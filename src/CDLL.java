import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * circular, doubly-linked lists.
 *
 * Implemented with Fail Fast policy.
 * 
 * @author Gun Woo Kim, Sam Rebelsky
 */
public class CDLL<T> implements SimpleList<T> {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The front of the list
   */
  Node2<T> front;

  /**
   * The number of values in the list.
   */
  int size;

  /**
   * Modification Counter (for fast fail policy implementation)
   */
  int modCnt = 0;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty list.
   */
  public CDLL() {
    this.front = new Node2<T>(null, null, null); // Create node
    this.front.next = this.front; // Point to itself
    this.front.prev = this.front; // Point to itself
    this.size = 0;
  } // SimpleDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      int pos = 0;

      Node2<T> prev = CDLL.this.front;
      Node2<T> next = CDLL.this.front.next;

      //The node to be updated by remove or set.  Has a value of
      Node2<T> update = null;

      // individual iterator's modification counter
      int iteratorModCnt = CDLL.this.modCnt;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      // Helper method to check for concurrent modification
      private void checkForModification(){
        if (CDLL.this.modCnt != this.iteratorModCnt) {
          throw new ConcurrentModificationException();
        }
      }

      public void add(T val) throws UnsupportedOperationException {
        // fail fast check
        checkForModification();

        this.prev = this.prev.insertAfter(val);

        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++CDLL.this.size;

        // Update the position.  (See SimpleArrayList.java for more of
        // an explanation.)
        ++this.pos;

        // Update the modification counter
        ++CDLL.this.modCnt;
        this.iteratorModCnt = CDLL.this.modCnt;

      } // add(T)

      public boolean hasNext() {
        return this.next != CDLL.this.front;
      } // hasNext()

      public boolean hasPrevious() {
        return this.prev != CDLL.this.front;
      } // hasPrevious()

      public T next() {
        // fail fast check
        checkForModification();

        // Identify the node to update
        this.update = this.next;
        // Advance the cursor
        this.prev = this.next;
        this.next = this.next.next;
        // Note the movement
        ++this.pos;
        // if the cursor is at the front (dummy node), move it to the first element
        if (this.update == CDLL.this.front) {
          this.update = this.next;
          this.prev = this.next;
          this.next = this.next.next;
          this.pos = 0;
        } // if
        // And return the value
        return this.update.value;
      } // next()

      public int nextIndex() {
        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        // fail fast check
        checkForModification();
        
        this.update = this.prev;
        this.next = this.prev;
        this.prev = this.prev.prev;
        --this.pos;

        // if the cursor is at the front (dummy node), move it to the last element
        if (this.update == CDLL.this.front) {
          this.update = this.prev;
          this.next = this.update;
          this.prev = this.update.prev;
          this.pos = CDLL.this.size - 1;
        } // if
        
        return this.update.value;
      } // previous()

      public void remove() {
        // fail fast check
        checkForModification();

        // Sanity check (happens iff there is no element in the list)
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Adjust the front if necessary. check if the node to be removed
        // is the first element in the list (the element after the dummy)
        if (CDLL.this.front.next == this.update) {
          CDLL.this.front.next = this.update.next;
          if (CDLL.this.size == 1) { // Special case when we remove the last remaining element
            CDLL.this.front.next = CDLL.this.front; // Make the list point to itself again
            CDLL.this.front.prev = CDLL.this.front;
          }
        }

        // Special handling for updating next and prev pointers of the iterator
        if (this.next == this.update) {
          this.next = this.update.next;
        }
        if (this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos; 
        }

        this.update.remove();

        --CDLL.this.size;
        // Update the modification counter
        ++CDLL.this.modCnt;
        this.iteratorModCnt = CDLL.this.modCnt;

        this.update = null;
      } // remove()

      public void set(T val) {
        // fail fast check
        checkForModification();

        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.value = val;
      } // set(T)
    };
  } // listIterator()

} // class SimpleDLL<T>
