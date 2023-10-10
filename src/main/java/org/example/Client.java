package org.example;

public class Client {

    private String firstName;
    private String lastName;
    private final String personalId;
    private ClientType clientType;
    private boolean archived;

    public Client(String firstName, String lastName, String personalId, ClientType clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.clientType = clientType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isArchived() {
        return archived;
    }

    double applyDiscount(double price) {
        return price - clientType.applyDiscount(price);
    }

//    public String toString() {
//        String archived = isArchived() ? "archived" : "active";
//        return String.format("%s %s (%s) - %s", firstName, lastName, personalId, archived);
//    }
}
