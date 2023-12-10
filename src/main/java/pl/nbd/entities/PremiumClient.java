package pl.nbd.entities;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("clients")
public class PremiumClient extends Client {

    @CqlName("discriminator")
    private String discriminator;

    @CqlName("membership_level")
    private int membershipLevel;

    @CqlName("discount")
    private double discount;

    public PremiumClient(java.util.UUID id, int membershipLevel, double discount, java.lang.String discriminator, java.lang.String firstName, java.lang.String lastName, java.lang.String personalId) {
        super(id, firstName, lastName, personalId);
        this.membershipLevel = membershipLevel;
        this.discount = discount;
        this.discriminator = "premium";
    }
    public PremiumClient(java.util.UUID id, int membershipLevel, double discount, java.lang.String firstName, java.lang.String lastName, java.lang.String personalId) {
        super(id, firstName, lastName, personalId);
        this.membershipLevel = membershipLevel;
        this.discount = discount;
        this.discriminator = "premium";
    }
    public PremiumClient(String firstName, String lastName, String personalId, int membershipLevel, double discount, String discriminator) {
        super(firstName, lastName, personalId);
        this.membershipLevel = membershipLevel;
        this.discount = discount;
        this.discriminator = "premium";
    }

    public PremiumClient(UUID id,
                          String firstName,
                        String lastName,
                       String personalId,
                       int membershipLevel,
                    double discount,
                         String discriminator) {
        super(id, firstName, lastName, personalId);
        this.membershipLevel = membershipLevel;
        this.discount = discount;
        this.discriminator = "premium";
    }

    public PremiumClient() {

    }

    public int getMembershipLevel() {
        return membershipLevel;
    }


    public void setMembershipLevel(int membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


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

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
