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
package mmm.coffee.metacode.common.trait;

import java.util.List;

/**
 * Functional interface for collection items
 */
@FunctionalInterface
public interface CollectTrait<T> {

    /**
     * Collects items, honoring the conditions set with {@code setConditions}
     *
     * @return the items meeting the conditions
     */
    List<T> collect();
}