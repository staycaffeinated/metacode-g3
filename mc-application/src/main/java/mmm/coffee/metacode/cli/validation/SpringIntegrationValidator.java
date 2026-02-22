/*
 * Copyright 2020 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.metacode.cli.validation;

import mmm.coffee.metacode.spring.constant.SpringIntegrations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Verify SpringIntegrations that have been selected together are supported.
 * For example, the end-user can select either PostgreSQL or MongoDB, but the
 * code-generator does not support having both databases together.
 */
public class SpringIntegrationValidator implements ValidationTrait {

    private final List<SpringIntegrations> selectedIntegrations = new ArrayList<>();

    private boolean evaluated = false;

    private String errorMessage = "";

    private SpringIntegrationValidator(SpringIntegrations[] selected) {
        if (selected == null) return;
        selectedIntegrations.addAll(Arrays.asList(selected));
    }

    public static SpringIntegrationValidator of(SpringIntegrations[] selected) {
        return new SpringIntegrationValidator(selected);
    }

    @Override
    public boolean isValid() {
        return !containsMutuallyExclusiveSelections();
    }

    @Override
    public boolean isInvalid() {
        return containsMutuallyExclusiveSelections();
    }

    @Override
    public String errorMessage() {
        if (!evaluated) containsMutuallyExclusiveSelections();
        return errorMessage;
    }

    private boolean containsMutuallyExclusiveSelections() {
        evaluated = true;
        AtomicBoolean containsPostgres = new AtomicBoolean(false);
        AtomicBoolean containsMongoDb = new AtomicBoolean(false);
        AtomicBoolean containsLiquibase = new AtomicBoolean(false);
        AtomicBoolean containsFlyway = new AtomicBoolean(false);
        selectedIntegrations.forEach(item -> {
            if (item.equals(SpringIntegrations.POSTGRES)) containsPostgres.set(true);
            if (item.equals(SpringIntegrations.MONGODB)) containsMongoDb.set(true);
            if (item.equals(SpringIntegrations.FLYWAY)) containsFlyway.set(true);
            if (item.equals(SpringIntegrations.LIQUIBASE)) containsLiquibase.set(true);
        });
        boolean containsMutEx = false;
        if (containsPostgres.get() && containsMongoDb.get()) {
            errorMessage = String.format("Two mutually-exclusive items were selected together: %s and %s."
                            + " Only one of these is allowed per project.", SpringIntegrations.POSTGRES,
                    SpringIntegrations.MONGODB);
            containsMutEx = true;
        }
        if (containsLiquibase.get() && containsFlyway.get()) {
            errorMessage = String.format("Two mutually-exclusive items were selected together: %s and %s."
                            + " Only one of these is allowed per project.", SpringIntegrations.FLYWAY,
                    SpringIntegrations.LIQUIBASE);
            containsMutEx = true;
        }
        return containsMutEx;
    }
}
