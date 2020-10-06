/*
 * Copyright 2020 Alexengrig Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexengrig.metter.motivation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public abstract class ChangeLogGeneratorTest {
    protected abstract ChangeLogGenerator<Man> createGenerator();

    @Test
    public void should_generate_changeLog() {
        ChangeLogGenerator<Man> generator = createGenerator();
        Man man = new Man("Tomas", 18);
        Man newMan = new Man("Tom", 19);
        assertEquals("name: Tomas -> Tom\nage: 18 -> 19", generator.generate(man, newMan));
    }
}