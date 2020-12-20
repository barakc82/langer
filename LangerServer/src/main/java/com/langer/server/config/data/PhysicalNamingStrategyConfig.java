package com.langer.server.config.data;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhysicalNamingStrategyConfig
{
    @Autowired
    private TableNamer tableNamer;

    @Bean
    public PhysicalNamingStrategy physicalNamingStrategy()
    {
        return new ProfileBasedPhysicalNamingStrategy();
    }

    public class ProfileBasedPhysicalNamingStrategy extends SpringPhysicalNamingStrategy
    {

        @Override
        public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv)
        {
            Identifier id = super.toPhysicalTableName(identifier, jdbcEnv);
            return new Identifier(tableNamer.name(id.getText()), id.isQuoted());
        }
    }
}