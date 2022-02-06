package com.modusale.baemin.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.Iterator;
import java.util.List;

@ToString
public class Categories implements Iterable<Category> {
    @Getter
    private List<Category> categories;

    public Categories(List<Category> categories) {
        this.categories=categories;
    }

    public void add(String category){
        this.categories.add(new Category(category,true));
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
