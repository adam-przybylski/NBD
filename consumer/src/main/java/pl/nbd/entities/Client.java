package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.nbd.mappers.MongoUUID;

public abstract class Client {


    @BsonProperty("_id")
    private MongoUUID id;

    @BsonProperty("firstName")
    private String firstName;

    @BsonProperty("lastName")
    private String lastName;

    @BsonProperty("personalId")
    private String personalId;


    public Client(String firstName, String lastName, String personalId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
    }

    public Client(MongoUUID id, String firstName, String lastName, String personalId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
    }

    public Client() {

    }

    public MongoUUID getId() {
        return id;
    }

    public void setId(MongoUUID id) {
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
