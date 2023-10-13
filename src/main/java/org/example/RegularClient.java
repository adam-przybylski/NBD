package org.example;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("regularclient")
public class RegularClient extends Client {

    private double discount;

    public RegularClient(String firstName, String lastName, String personalId, double discount) {
        super(firstName, lastName, personalId);
        this.discount = discount;
    }

    public RegularClient() {

    }


    @Override
    public double applyDiscount(double price) {
        return price * this.discount;
    }

}
