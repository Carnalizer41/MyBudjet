package com.carnalizer.mybudjet.entities;

import java.util.List;

public class Category {

    private String categorykName;
    private List<Subcategory> subcategories;

    public Category(String blockName, List<Subcategory> categories) {
        this.categorykName = blockName;
        this.subcategories = categories;
    }

    public void addSubcategory(Subcategory subcategory){
        subcategories.add(subcategory);
    }

    public String getCategorykName() {
        return categorykName;
    }

    public List<Subcategory> getSubategories() {
        return subcategories;
    }
}
