package pl.nbd;

import pl.nbd.entities.*;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.repositories.ClientRepository;
import pl.nbd.repositories.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
        try (ClientRepository clientRepository = new ClientRepository()) {
            ArrayList<Client> clients = clientRepository.readAllClients();
            for (Client client : clients) {
                System.out.println(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
