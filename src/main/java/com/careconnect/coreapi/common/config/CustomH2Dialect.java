package com.careconnect.coreapi.common.config;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.type.StandardBasicTypes;

/**
 * Custom H2 dialect to provide PostgreSQL compatibility functions.
 * Maps PostgreSQL functions to their H2 equivalents for seamless migration.
 */
public class CustomH2Dialect extends H2Dialect {

    public CustomH2Dialect() {
        super();
    }

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        
        // Map PostgreSQL's gen_random_uuid() to H2's RANDOM_UUID()
        functionContributions.getFunctionRegistry().registerPattern(
            "gen_random_uuid",
            "random_uuid()",
            functionContributions.getTypeConfiguration().getBasicTypeRegistry()
                .resolve(StandardBasicTypes.UUID)
        );
    }
}
