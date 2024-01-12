package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.nbd.mappers.MongoUUID;

@BsonDiscriminator(key = "_clazz", value = "PremiumClient")
public class PremiumClient extends Client {

    @BsonProperty("membershipLevel")
    private int membershipLevel;

    @BsonProperty("discount")
    private double discount;

    public PremiumClient(String firstName, String lastName, String personalId, int membershipLevel, double discount) {
        super(firstName, lastName, personalId);
        this.membershipLevel = membershipLevel;
        this.discount = discount;
    }

    public PremiumClient(@BsonProperty("id") MongoUUID id,
                         @BsonProperty("firstName") String firstName,
                         @BsonProperty("lastName") String lastName,
                         @BsonProperty("personalId") String personalId,
                         @BsonProperty("membershipLevel") int membershipLevel,
                         @BsonProperty("discount") double discount) {
        super(id, firstName, lastName, personalId);
        this.membershipLevel = membershipLevel;
        this.discount = discount;
    }

    public PremiumClient() {

    }
    @Override
    public int getMembershipLevel() {
        return membershipLevel;
    }

    @Override
    public void setMembershipLevel(int membershipLevel) {
        this.membershipLevel = membershipLevel;
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
    public double applyDiscount(double price) {
        return  price * (this.discount - (membershipLevel * 0.1));
    }

    @Override
    public String toString() {
        return "PremiumClient{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", personalId='" + getPersonalId() + '\'' +
                ", membershipLevel=" + membershipLevel +
                ", discount=" + discount +
                '}';
    }
}
