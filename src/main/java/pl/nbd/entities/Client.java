package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public abstract class Client {


    private UUID id;


    private String firstName;


    private String lastName;


    private String personalId;


    public Client(String firstName, String lastName, String personalId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
    }

    public Client(UUID id, String firstName, String lastName, String personalId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
    }

    public Client() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    abstract double applyDiscount(double price);

    public String toString() {
        return String.format("%s %s (%s)", firstName, lastName, personalId);
    }

    public abstract void setDiscount(double discount);

    public abstract void setMembershipLevel(int membershipLevel);

    public abstract int getMembershipLevel();

    public abstract double getDiscount();
}
