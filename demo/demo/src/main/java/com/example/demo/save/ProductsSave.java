package com.example.demo.save;

import com.example.demo.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsSave {
    private List<Integer> count = new ArrayList<>();

    public ProductsSave(List<Integer> count) {
        this.count = count;
    }

    public ProductsSave() {

    }

    public void addCount(Integer count){
        this.count.add(count);
    }

    public List<Integer> getCount() {
        return count;
    }

    public void setCount(List<Integer> count) {
        this.count = count;
    }
}
