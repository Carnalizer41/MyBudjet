package com.carnalizer.mybudjet.entities;

import java.util.List;

public class CategoryBlock {

    private String blockName;
    private List<Category> categories;

    public CategoryBlock(String blockName, List<Category> categories) {
        this.blockName = blockName;
        this.categories = categories;
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public String getBlockName() {
        return blockName;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
