package encore.model;

import java.util.NoSuchElementException;

public class SinglyLinkedQueue<T> {
    private QueueNode<T> head;
    private QueueNode<T> tail;
    private int size;

    public SinglyLinkedQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void enqueue(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot enqueue null element.");
        }
        QueueNode<T> newNode = new QueueNode<>(element);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        T data = head.getData();
        head = head.getNext();
        size--;
        if (head == null) {
            tail = null;
        }
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return head.getData();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
