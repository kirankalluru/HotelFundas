package com.example.demo.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomIdGenerator implements IdentifierGenerator {
    private static final long START_VALUE = 10000; // Starting from 10000
    private static final long INCREMENT_SIZE = 1000; // Increment by 1000

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Connection connection = session.connection();
        try {
            Statement statement = connection.createStatement();

            // Get the table name from the entity metadata
            EntityPersister persister = session.getEntityPersister(object.getClass().getName(), object);
            String tableName = ((SingleTableEntityPersister) persister).getTableName();

            ResultSet rs = statement.executeQuery(
                "SELECT COALESCE(MAX(id), " + (START_VALUE - INCREMENT_SIZE) + ") FROM " + tableName
            );

            if (rs.next()) {
                long maxId = rs.getLong(1);
                return maxId + INCREMENT_SIZE;
            }

            return START_VALUE;

        } catch (SQLException e) {
            throw new HibernateException("Unable to generate ID", e);
        }
    }
} 