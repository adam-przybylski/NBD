package pl.nbd.providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import pl.nbd.entities.Client;
import pl.nbd.entities.Default;
import pl.nbd.entities.PremiumClient;
import pl.nbd.entities.RegularClient;

import java.util.UUID;

public class ClientGetByIdProvider {
    private final CqlSession session;

    private EntityHelper<Default> defaultEntityHelper;
    private EntityHelper<PremiumClient> premiumEntityHelper;
    private EntityHelper<RegularClient> regularEntityHelper;

    public ClientGetByIdProvider(MapperContext ctx, EntityHelper<Default> defaultEntityHelper, EntityHelper<PremiumClient> premiumEntityHelper, EntityHelper<RegularClient> regularEntityHelper) {
        this.session = ctx.getSession();
        this.defaultEntityHelper = defaultEntityHelper;
        this.premiumEntityHelper = premiumEntityHelper;
        this.regularEntityHelper = regularEntityHelper;
    }

    public void create(Client client) {
        session.execute(
                switch (client.getDiscriminator()) {
                    case "default" -> {
                        Default defaultClient = (Default) client;
                        yield session.prepare(defaultEntityHelper.insert().build()).bind()
                                .setUuid(CqlIdentifier.fromCql("id"), defaultClient.getId())
                                .setString(CqlIdentifier.fromCql("first_name"), defaultClient.getFirstName())
                                .setString(CqlIdentifier.fromCql("last_name"), defaultClient.getLastName())
                                .setString(CqlIdentifier.fromCql("personal_id"), defaultClient.getPersonalId())
                                .setString(CqlIdentifier.fromCql("discriminator"), defaultClient.getDiscriminator()
                        );
                    }

                    case "premium" -> {
                        PremiumClient premiumClient = (PremiumClient) client;
                        yield session.prepare(premiumEntityHelper.insert().build()).bind()
                                .setUuid(CqlIdentifier.fromCql("id"), premiumClient.getId())
                                .setString(CqlIdentifier.fromCql("first_name"), premiumClient.getFirstName())
                                .setString(CqlIdentifier.fromCql("last_name"), premiumClient.getLastName())
                                .setString(CqlIdentifier.fromCql("personal_id"), premiumClient.getPersonalId())
                                .setInt(CqlIdentifier.fromCql("membership_level"), premiumClient.getMembershipLevel())
                                .setDouble(CqlIdentifier.fromCql("discount"), premiumClient.getDiscount())
                                .setString(CqlIdentifier.fromCql("discriminator"), premiumClient.getDiscriminator()
                        );
                    }

                    case "regular" -> {
                        RegularClient regularClient = (RegularClient) client;
                        yield session.prepare(regularEntityHelper.insert().build()).bind()
                                .setUuid(CqlIdentifier.fromCql("id"), regularClient.getId())
                                .setString(CqlIdentifier.fromCql("first_name"), regularClient.getFirstName())
                                .setString(CqlIdentifier.fromCql("last_name"), regularClient.getLastName())
                                .setString(CqlIdentifier.fromCql("personal_id"), regularClient.getPersonalId())
                                .setDouble(CqlIdentifier.fromCql("discount"), regularClient.getDiscount())
                                .setString(CqlIdentifier.fromCql("discriminator"), regularClient.getDiscriminator()
                        );
                    }

                    default -> throw new IllegalStateException("Unexpected value: " + client.getDiscriminator());
                }
        );
    }
    public Client findById(UUID id) {
        Select selectClient = QueryBuilder
                .selectFrom("clients")
                .all()
                .whereColumn("id")
                .isEqualTo(QueryBuilder.literal(id));
        Row row = session.execute(selectClient.build()).one();
        String discriminator = row.getString("discriminator");
        if (discriminator.equals("default")) {
            return getDefault(row);
        } else if (discriminator.equals("premium")) {
            return getPremium(row);
        } else {
            return getRegular(row);
        }
    }

    public Default getDefault(Row row) {
     return new Default(row.getUuid("id"),
                row.getString("first_name"),
                row.getString("last_name"),
                row.getString("personal_id"),
                row.getString("discriminator"));
    }

    public PremiumClient getPremium(Row row) {
        return new PremiumClient(row.getUuid("id"),
                row.getString("first_name"),
                row.getString("last_name"),
                row.getString("personal_id"),
                row.getInt("membership_level"),
                row.getDouble("discount"),
                row.getString("discriminator"));
    }

    public RegularClient getRegular(Row row) {
        return new RegularClient(row.getUuid("id"),
                row.getString("first_name"),
                row.getString("last_name"),
                row.getString("personal_id"),
                row.getDouble("discount"),
                row.getString("discriminator"));
    }
}
