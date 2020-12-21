package com.match.service.search;

/**
 *
 */
public class HouseSuggest {
    private String input;//输入的
    private int weight = 10;//默认权重

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
