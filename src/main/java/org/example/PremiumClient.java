package org.example;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("premiumclient")
public class PremiumClient extends Client{

    private int membershipLevel;
    private double discount;

    public PremiumClient(String firstName, String lastName, String personalId, int membershipLevel, double discount) {
        super(firstName, lastName, personalId);
        this.membershipLevel = membershipLevel;
        this.discount = discount;
    }

    public PremiumClient() {

    }


    @Override
    public double applyDiscount(double price) {
        return this.discount / membershipLevel;
    }
}
