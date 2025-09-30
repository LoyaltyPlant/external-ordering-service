package com.loyaltyplant.server.services.externalordering.repository;

import com.loyaltyplant.oss.plantsql.SQLTemplateFactory;
import com.loyaltyplant.oss.plantsql.map.SQLMappers;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExampleRepository {
    private final SQLTemplateFactory psql;

    @Autowired
    public ExampleRepository(final @Qualifier("psql") SQLTemplateFactory psql) {
        this.psql = psql;
    }

    public long exampleInsert() {
        @Language("PostgreSQL")
        final String query = "INSERT INTO orders VALUES (:id, :external_id, :status)";

        return psql.template(query)
                .parameter("status", 0)
                .parameter("id", 1)
                .parameter("external_id", 12345)
                .executeInsert()
                .getGeneratedLongKey();
    }

    public void exampleUpdate() {
        @Language("PostgreSQL")
        final String query = "UPDATE orders SET status = :status WHERE id = :id";

        psql.template(query)
                .parameter("status", 1)
                .parameter("id", 1)
                .executeUpdate();
    }


    /**
     * For complex objects use AbstractSQLCollector.
     * It provides more convenient way to map SQL result to object.
     * See com.loyaltyplant.oss.plantsql.map.AbstractSQLCollector<T> for more details.
     */
    public String exampleSelect() {
        @Language("PostgreSQL")
        final String query = "SELECT external_id FROM orders WHERE id = :id";

        return psql.template(query)
                .parameter("id", 1)
                .executeQuery(SQLMappers.STRING.forNotNullColumn(1))
                .getFirstResultOrThrow(() -> new RuntimeException("Order not found"));
    }
}
