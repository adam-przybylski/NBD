package pl.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import pl.nbd.entities.Client;
import pl.nbd.entities.Default;
import pl.nbd.entities.PremiumClient;
import pl.nbd.entities.RegularClient;
import pl.nbd.providers.ClientGetByIdProvider;

import java.util.UUID;

@Dao
public interface ClientDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientGetByIdProvider.class,
            entityHelpers = {Default.class, PremiumClient.class, RegularClient.class})
    Client findById(UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = ClientGetByIdProvider.class,
            entityHelpers = {Default.class, PremiumClient.class, RegularClient.class})
    void create(Client client);

    @Delete
    void remove(Client client);
}
