package org.example;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("default")
public class Default extends Client {

    public Default(String firstName, String lastName, String personalId) {
        super(firstName, lastName, personalId);
    }

    public Default() {

    }

    @Override
    public double applyDiscount(double price) {
        return 0;
    }

}
