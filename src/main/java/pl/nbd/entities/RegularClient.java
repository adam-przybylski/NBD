package pl.nbd.entities;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("clients")
public class RegularClient extends Client {

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public RegularClient(java.util.UUID id, java.lang.String discriminator, double discount, int membershipLevel, java.lang.String firstName, java.lang.String lastName, java.lang.String personalId) {
        super(id, firstName, lastName, personalId);
        this.discount = discount;
        this.discriminator = "regular";
    }
    RegularClient(java.util.UUID id, double discount, int membershipLevel, java.lang.String firstName, java.lang.String lastName, java.lang.String personalId) {
        super(id, firstName, lastName, personalId);
        this.discount = discount;
        this.discriminator = "regular";
    }
    @CqlName("discriminator")
    private String discriminator;

    @CqlName("discount")
    private double discount;

    public RegularClient(String firstName, String lastName, String personalId, double discount) {
        super(firstName, lastName, personalId);
        this.discount = discount;
        this.discriminator = "regular";
    }

    public RegularClient(UUID id,
                         String firstName,
                         String lastName,
                         String personalId,
                         double discount,
                         String discriminator) {
        super(id, firstName, lastName, personalId);
        this.discount = discount;
        this.discriminator = "regular";
    }

    public RegularClient() {

    }


    public double getDiscount() {
        return discount;
    }


    public void setDiscount(double discount) {
        this.discount = discount;
    }


    public void setMembershipLevel(int membershipLevel) {

    }


    public int getMembershipLevel() {
        return 0;
    }


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
