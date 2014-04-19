package com.travelzen.datastructure.skiplist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/*
 */
public class SkipListSet<E extends Comparable>  {

    int numElements;
    int numLevels;
    Node<E> firstNode, lastNode, bFirstNode, bLastNode;

    public SkipListSet() {
        bFirstNode = firstNode = new Node();
        bLastNode = lastNode = new Node();
        firstNode.setNext(lastNode);
        lastNode.setUp(firstNode);
        numLevels = 1;
        numElements = 0;
    }

    public SkipListSet(Collection<? extends E> c) {
        bFirstNode = firstNode = new Node();
        bLastNode = lastNode = new Node();
        firstNode.setNext(lastNode);
        lastNode.setUp(firstNode);
        numLevels = 1;
        numElements = 0;

        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            add((E) iter.next());
        }
    }

    public boolean add(E o) {
        Node add = new Node(o);
        Node cur, up, next, temp;
        cur = firstNode.getNext();

        boolean flag = true;
        //first add a element
        try {
            if (cur.getNext() == null) {
                temp = cur.getUp();
                temp.setNext(add);
                add.setUp(temp);
                add.setNext(cur);
                cur.setUp(add);
                numElements++;
            } else {
                while (true) {
                    int comr = ((E) add.getValue()).compareTo(cur.getValue());
                    //the one to be added is bigger than the current one
                    if (comr > 0) {
                        if (cur.getNext().getValue() != null) {
                            cur = cur.getNext();
                        } else if (cur.getDown() != null && cur.getNext().getValue() == null) {
                            cur = cur.getDown();
                        } else if (cur.getDown() == null && cur.getNext().getValue() == null) {
                            add.setNext(cur.getNext());
                            cur.getNext().setUp(add);
                            cur.setNext(add);
                            add.setUp(cur);
                            numElements++;
                            promote(add);
                            break;
                        }
                    } else if (comr < 0) {
                        if (cur.getDown() != null) {
                            cur = cur.getUp().getDown();
                            cur=cur.getNext();
                        } else {
                            cur.getUp().setNext(add);
                            add.setUp(cur.getUp());
                            add.setNext(cur);
                            cur.setUp(add);
                            numElements++;
                            promote(add);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    private void promote(Node<E> add) {
        boolean flag;
        int tempLevel = 1;
        Node<E> cur, temp, up;
        temp = null;
        cur = add;
        while (true) {
            if ((tempLevel + 1) <= numElements) {
                flag = Math.random() > 0.5;
                if (flag) {
//        System.out.println("promote"+add.getValue());
                    if (tempLevel == numLevels) {
                        Node<E> tempf = new Node();
                        Node<E> tempb = new Node();
                        temp = new Node(add.getValue());
                        temp.setDown(cur);
                        cur.setOver(temp);
                        tempf.setDown(firstNode);
                        firstNode.setOver(tempf);
                        firstNode = tempf;
                        tempb.setDown(lastNode);
                        lastNode.setOver(tempb);
                        lastNode = tempb;

                        firstNode.setNext(temp);
                        temp.setNext(lastNode);
                        lastNode.setUp(temp);
                        temp.setUp(firstNode);

                        tempLevel++;
                        numLevels++;
                    } else {
                        temp = new Node(add.getValue());
                        temp.setDown(cur);
                        cur.setOver(temp);
                        up = cur.getUp();
                        while (up.getOver() == null) {
                            up = up.getUp();
                        }
                        up = up.getOver();
                        temp.setNext(up.getNext());
                        up.getNext().setUp(temp);
                        up.setNext(temp);
                        temp.setUp(up);
                        tempLevel++;
                    }
                    cur = temp;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void addElement(Node<E> before, Node<E> after) {
    }

    public boolean remove(E o) {
        Node elm = new Node(o);
        Node cur, up, next, temp;
        cur = firstNode.getNext();

        boolean flag = true;
        //first add a element
        try {
            if (cur.getNext() == null) {
                flag = false;
            } else {
                while (true) {
                    int comr = ((E) elm.getValue()).compareTo(cur.getValue());
                    //the one to be added is bigger than the current one
                    if (comr > 0) {
                        if (cur.getNext().getValue() != null) {
                            cur = cur.getNext();
                        } else if (cur.getDown() != null && cur.getNext().getValue() == null) {
                            cur = cur.getDown();
                        } else if (cur.getDown() == null && cur.getNext().getValue() == null) {
                            flag = false;
                            break;
                        }
                    } else if (comr < 0) {
                        if (cur.getDown() != null) {
                            cur = cur.getUp().getDown();
                            cur=cur.getNext();
                        } else {
                            flag = false;
                            break;
                        }
                    } else {
                        delete(cur);
                        this.numElements--;
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        if(!flag){
            System.out.println(o.toString()+" is not in the list!\n");
        }else{
            System.out.println("remove "+o.toString()+" success!\n");
        }
        return flag;
    }

    private void delete(Node<E> elm) {
        while (elm != null) {
            elm.getUp().setNext(elm.getNext());
            elm.getNext().setUp(elm.getUp());
            if(firstNode.getNext().getValue()==null){
                firstNode=firstNode.getDown();
                lastNode=lastNode.getDown();
                numLevels--;
            }
            elm = elm.getDown();
        }
    }

    
    public void clear() {
        firstNode.setNext(lastNode);
        firstNode.setDown(null);
        lastNode.setUp(firstNode);
        lastNode.setDown(null);
        numLevels = 1;
        numElements = 0;
    }

    public boolean contains(E o) {
        Node elm = new Node(o);
        Node cur;
        cur = firstNode.getNext();

        boolean flag = true;
        //first add a element
        try {
            if (cur.getNext() == null) {
                flag = false;
            } else {
                while (true) {
                    int comr = ((E) elm.getValue()).compareTo(cur.getValue());
                    //the one to be added is bigger than the current one
                    if (comr > 0) {
                        if (cur.getNext().getValue() != null) {
                            cur = cur.getNext();
                        } else if (cur.getDown() != null && cur.getNext().getValue() == null) {
                            cur = cur.getDown();
                        } else if (cur.getDown() == null && cur.getNext().getValue() == null) {
                            flag = false;
                            break;
                        }
                    } else if (comr < 0) {
                        if (cur.getDown() != null) {
                            cur = cur.getUp().getDown();
                            cur=cur.getNext();
                        } else {
                            flag = false;
                            break;
                        }
                    } else {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    public Iterator iterator() {
        Iterator iter = new Itr();
        return iter;
    }

    public int size() {
        return numElements;
    }

    public void printAll(){
        Node cur,temp;
        cur=firstNode;
        int tempLevel=numLevels;
        while(cur!=null){
            temp=cur;
            System.out.print("level_"+tempLevel--+" = ");
            while(temp!=null){
                System.out.print(temp.getValue()+" ");
                temp=temp.getNext();
            }
            cur=cur.getDown();
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> goal = new ArrayList<Integer>();
        int h;
        System.out.print("��ʼ��ֵ��\t");
        for (int i = 0; i < 20; i++) {
//            goal.add(((int) (Math.random() * 1000)) + "");
            goal.add(i+1);
//            System.out.print(goal.get(i) + " ");
        }
        System.out.println("");
        SkipListSet sls = new SkipListSet(goal);
        sls.printAll();
        h=(Integer)sls.firstNode.getNext().getValue();
        sls.remove(h);
        sls.remove(0);
        sls.printAll();

        sls.add(h);
        sls.printAll();

        System.out.println("clear all elements!\n");
        sls.clear();
        sls.printAll();
    }

    private class Itr implements Iterator<E> {

        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;
        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        public boolean hasNext() {
            return cursor != size();
        }

        public E next() {
            return null;
        }

        public void remove() {
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            if (lastRet < cursor) {
                cursor--;
            }
            lastRet = -1;
        }
    }
}


