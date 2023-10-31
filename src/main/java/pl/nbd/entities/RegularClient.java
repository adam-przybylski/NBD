package pl.nbd.entities;

public class RegularClient extends Client {

    private double discount;

    public RegularClient(String firstName, String lastName, String personalId, double discount) {
        super(firstName, lastName, personalId);
        this.discount = discount;
    }

    public RegularClient() {

    }


    @Override
    public double applyDiscount(double price) {
        return price * this.discount;
    }

}
