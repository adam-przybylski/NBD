package pl.nbd.entities;

public abstract class Client {


    private long id;
    private String firstName;
    private String lastName;

    private String personalId;
    private boolean archived;

    private long version;

    public Client(String firstName, String lastName, String personalId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
    }

    public Client() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isArchived() {
        return archived;
    }

    abstract double applyDiscount(double price);

    public String toString() {
        String archived = isArchived() ? "archived" : "active";
        return String.format("%s %s (%s) - %s", firstName, lastName, personalId, archived);
    }
}
