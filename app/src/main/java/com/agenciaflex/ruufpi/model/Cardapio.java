package com.agenciaflex.ruufpi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanssantos on 3/15/15.
 */
public class Cardapio {

    private int id;
    private int unidade_id;
    private int type_id;
    private String day;
    private String horario;
    private List<Item> items;

    public Cardapio() {
        items = new ArrayList<Item>();
    }

    public Cardapio(int id, int unidade_id, int type_id, String day, String horario) {
        this.id = id;
        this.unidade_id = unidade_id;
        this.type_id = type_id;
        this.day = day;
        this.horario = horario;
    }

    public Cardapio(int id, int unidade_id, int type_id, String day, String horario, List<Item> items) {
        this.id = id;
        this.unidade_id = unidade_id;
        this.type_id = type_id;
        this.day = day;
        this.horario = horario;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnidade_id() {
        return unidade_id;
    }

    public void setUnidade_id(int unidade_id) {
        this.unidade_id = unidade_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cardapio{" +
                "id=" + id +
                ", unidade_id=" + unidade_id +
                ", type_id=" + type_id +
                ", day='" + day + '\'' +
                ", horario='" + horario + '\'' +
                ", items=" + items +
                '}';
    }
}
