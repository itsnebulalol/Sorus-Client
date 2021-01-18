package org.sorus.launchwrapper;

import java.util.ArrayList;
import java.util.Collection;

public class CustomArrayList<E> extends ArrayList<E> {

    public CustomArrayList(Collection<E> collection) {
        super(collection);
    }

    @Override
    public boolean add(E e) {
        super.add(this.size() - 1, e);
        return true;
    }

    public void append(E e) {
        super.add(e);
    }

    @Override
    public E get(int index) {
        return super.get(index - 1);
    }

}
