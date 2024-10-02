package org.redis;

public class Pair<T, O> {
    public T left;
    public O right; 

    public  Pair(T _left, O _right) { 
        this.left = _left;
        this.right = _right;
    }
}
