package pl.nbd.entities;

public class Default extends Client {

    public Default(String firstName, String lastName, String personalId) {
        super(firstName, lastName, personalId);
    }

    public Default() {

    }

    @Override
    public double applyDiscount(double price) {
        return 0;
    }

}
