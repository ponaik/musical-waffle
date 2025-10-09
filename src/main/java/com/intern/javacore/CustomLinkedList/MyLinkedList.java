package com.intern.javacore.CustomLinkedList;

public class MyLinkedList<E> {
    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E val) { this.value = val; }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(E el) {
        Node<E> n = new Node<>(el);
        n.next = head;
        head = n;
        if (tail == null) tail = n;
        size++;
    }

    public void addLast(E el) {
        Node<E> n = new Node<>(el);
        if (tail == null) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public void add(int index, E el) {
        checkPositionIndex(index); // allow index == size for append
        if (index == 0) {
            addFirst(el);
            return;
        }
        if (index == size) {
            addLast(el);
            return;
        }
        Node<E> prev = nodeAt(index - 1);
        Node<E> n = new Node<>(el);
        n.next = prev.next;
        prev.next = n;
        size++;
    }

    public E getFirst() {
        ensureNotEmpty();
        return head.value;
    }

    public E getLast() {
        ensureNotEmpty();
        return tail.value;
    }

    public E get(int index) {
        checkElementIndex(index); // index must be 0..size-1
        return nodeAt(index).value;
    }

    public E removeFirst() {
        ensureNotEmpty();
        E val = head.value;
        head = head.next;
        size--;
        if (head == null) tail = null;
        return val;
    }

    public E removeLast() {
        ensureNotEmpty();
        if (size == 1) {
            E val = head.value;
            head = tail = null;
            size = 0;
            return val;
        }
        Node<E> prev = nodeAt(size - 2);
        E val = prev.next.value;
        prev.next = null;
        tail = prev;
        size--;
        return val;
    }

    public E remove(int index) {
        checkElementIndex(index);
        if (index == 0) return removeFirst();
        if (index == size - 1) return removeLast();
        Node<E> prev = nodeAt(index - 1);
        Node<E> toRemove = prev.next;
        prev.next = toRemove.next;
        size--;
        return toRemove.value;
    }

    // --- helper methods ---

    private Node<E> nodeAt(int index) {
        Node<E> curr = head;
        for (int i = 0; i < index; i++) curr = curr.next;
        return curr;
    }

    private void ensureNotEmpty() {
        if (size == 0) throw new IllegalStateException("List is empty");
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkPositionIndex(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // Optional convenience: string representation
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.value);
            if (curr.next != null) sb.append(", ");
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }
}

