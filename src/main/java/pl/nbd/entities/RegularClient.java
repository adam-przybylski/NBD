package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class RegularClient extends Client {

    private double discount;

    public RegularClient(String firstName, String lastName, String personalId, double discount) {
        super(firstName, lastName, personalId);
        this.discount = discount;
    }

    public RegularClient(UUID id,
                         String firstName,
                         String lastName,
                         String personalId,
                         double discount) {
        super(id, firstName, lastName, personalId);
        this.discount = discount;
    }

    public RegularClient() {

    }

    @Override
    public double getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public void setMembershipLevel(int membershipLevel) {

    }

    @Override
    public int getMembershipLevel() {
        return 0;
    }

    @Override
    public double applyDiscount(double price) {
        return price * this.discount;
    }

    @Override
    public String toString() {
        return "RegularClient{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", personalId='" + getPersonalId() + '\'' +
                ", discount=" + discount +
                '}';
    }

}
