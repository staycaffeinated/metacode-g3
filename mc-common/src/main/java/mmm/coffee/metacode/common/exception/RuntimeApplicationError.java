/*
 * Copyright 2022 Jon Caulfield
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
package mmm.coffee.metacode.common.exception;

import mmm.coffee.metacode.annotations.jacoco.ExcludeFromJacocoGeneratedReport;

/**
 * Our application-specific runtime exception
 */
@ExcludeFromJacocoGeneratedReport // Ignore code coverage for this class
public class RuntimeApplicationError extends RuntimeException {
    public RuntimeApplicationError() {
    }

    public RuntimeApplicationError(String msg) {
        super(msg);
    }

    public RuntimeApplicationError(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RuntimeApplicationError(Throwable t) {
        super(t);
    }
}
