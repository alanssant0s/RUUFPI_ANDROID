package com.agenciaflex.ruufpi.model;

/**
 * Created by alanssantos on 4/1/15.
 */
public class Item {

    private int id;
    private int item_category_id;
    private String name;

    public Item() {
    }

    public Item(int id, int item_category_id, String name) {
        this.id = id;
        this.item_category_id = item_category_id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItem_category_id() {
        return item_category_id;
    }

    public void setItem_category_id(int item_category_id) {
        this.item_category_id = item_category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", item_category_id=" + item_category_id +
                ", name='" + name + '\'' +
                '}';
    }
}
