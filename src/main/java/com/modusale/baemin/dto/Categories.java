package com.modusale.baemin.dto;

import lombok.Getter;

import java.util.Iterator;
import java.util.List;

public class Categories implements Iterable<Category> {
    private List<Category> categories;

    public Categories(List<Category> categories) {
        this.categories=categories;
    }

    public int size(){
        return categories.size();
    }

    public boolean isEmpty(){
        return categories.isEmpty();
    }

    public void remove(int index){
        categories.remove(index);
    }

    @Override
    public Iterator<Category> iterator() {
        return categories.iterator();
    }
}
