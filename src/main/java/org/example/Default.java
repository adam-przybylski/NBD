package org.example;

public class Default implements ClientType {

    @Override
    public double applyDiscount(double price) {
        return 0;
    }

    @Override
    public String getType() {
        return "Default";
    }
}
