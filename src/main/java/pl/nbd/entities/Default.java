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
public class Default extends Client {

    public Default(java.util.UUID id, int membershipLevel, double discount, java.lang.String discriminator, java.lang.String firstName, java.lang.String lastName, java.lang.String personalId) {
        super(id, firstName, lastName, personalId);
        this.discriminator = "default";
    }
    Default(java.util.UUID id, int membershipLevel, double discount, java.lang.String firstName, java.lang.String lastName, java.lang.String personalId) {
        super(id, firstName, lastName, personalId);
        this.discriminator = "default";
    }
    public Default(String firstName, String lastName, String personalId) {
        super(firstName, lastName, personalId);
        this.discriminator = "default";
    }


    public Default(UUID id,
                   String firstName,
                   String lastName,
                   String personalId,
                   String discriminator) {
        super(id, firstName, lastName, personalId);
        this.discriminator = "default";
    }

    public Default() {

    }

    @CqlName("discriminator")
    private String discriminator;


    public double applyDiscount(double price) {
        return 0;
    }

    @Override
    public String toString() {
        return "Default{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", personalId='" + getPersonalId() + '\'' +
                '}';
    }


    public void setDiscount(double discount) {

    }


    public void setMembershipLevel(int membershipLevel) {

    }


    public int getMembershipLevel() {
        return 0;
    }


    public double getDiscount() {
        return 0;
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Default client) {
            return this.getId().equals(client.getId()) &&
                    this.getFirstName().equals(client.getFirstName()) &&
                    this.getLastName().equals(client.getFirstName()) &&
                    this.getPersonalId().equals(client.getFirstName()) &&
                    this.getDiscriminator().equals(client.getDiscriminator());
        }
        return false;
    }
}
