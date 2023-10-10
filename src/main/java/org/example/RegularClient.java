package org.example;

public class RegularClient implements ClientType {

        @Override
        public double applyDiscount(double price) {
            return price * 0.7;
        }

        @Override
        public String getType() {
            return "Regular";
        }
}
