package com.travelzen.datastructure.skiplist;

public class Node<T> {

    private T value;
    private Node<T> next;
    private Node<T> down;
    private Node<T> up;
    private Node<T> over;

    public Node(){
        value=null;
        next=null;
        down=null;
        up=null;
        over=null;
    }

    public Node(T value){
        this.value=value;
        next=null;
        down=null;
        up=null;
        over=null;
    }

    /**
     * @return the next
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * @return the down
     */
    public Node<T> getDown() {
        return down;
    }

    /**
     * @param down the down to set
     */
    public void setDown(Node<T> down) {
        this.down = down;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return the up
     */
    public Node<T> getUp() {
        return up;
    }

    /**
     * @param up the up to set
     */
    public void setUp(Node<T> up) {
        this.up = up;
    }

    /**
     * @return the over
     */
    public Node<T> getOver() {
        return over;
    }

    /**
     * @param over the over to set
     */
    public void setOver(Node<T> over) {
        this.over = over;
    }
}
