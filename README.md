
================

# General Info: 

Title: MP8 - Implementing Doubly Linked, Circular Lists  
Author: Gun Woo Kim  
Due Date: 4/19/2024  
  
In this mini project, I implemented a doubly linked, circular list. The list has a dummy node at the beginning which points to the first node in the list. The last node in the list points to the dummy node. This helps to simplify the implementation of the list since we don't have to worry about edge cases when adding or removing nodes. Without dummy node, we need to hadle cases when the iterator is at the boundaries of the list. Also dummy node eases the operation in an empty list, because we can performs operations like we have 1 node doubly and circularly connected. It is also good that we always have the CDLL.front pointing to the dummy node, so we can always have a reference to the first and last node in the list.

  
## Acknowledgements:

- doubly linked list reading: https://rebelsky.cs.grinnell.edu/Courses/CSC207/2024Sp/readings/doubly-linked-lists.html
