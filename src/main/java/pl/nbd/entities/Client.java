package pl.nbd.entities;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_room")
@CqlName("clients")
@PropertyStrategy(mutable = false)
public class Client {

    @PartitionKey
    @CqlName("id")
    private UUID id;


    @CqlName("first_name")
    private String firstName;

    @CqlName("last_name")
    private String lastName;


    @CqlName("personal_id")
    private String personalId;


    private String discriminator;

    public Client(java.util.UUID id, java.lang.String firstName, java.lang.String lastName, java.lang.String personalId, java.lang.String discriminator) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.discriminator = discriminator;
    }
    Client(java.util.UUID id, java.lang.String firstName, java.lang.String lastName, java.lang.String personalId, int membershipLevel, double discount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
    }

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

    public String toString() {
        return String.format("%s %s (%s)", firstName, lastName, personalId);
    }

    public double applyDiscount(double v) {
        return 2;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
