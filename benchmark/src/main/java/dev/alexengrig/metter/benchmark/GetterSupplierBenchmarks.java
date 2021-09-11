/*
 * Copyright 2020-2021 Alexengrig Dev.
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

package dev.alexengrig.metter.benchmark;

import dev.alexengrig.metter.benchmark.domain.Domain12;
import dev.alexengrig.metter.benchmark.domain.Domain12GetterSupplier;
import dev.alexengrig.metter.benchmark.util.HandlingUtils;
import dev.alexengrig.metter.benchmark.util.ReflectionUtils;
import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

@Fork(2)
@State(Scope.Benchmark)
@Warmup(iterations = 10)
@Measurement(iterations = 20)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public class GetterSupplierBenchmarks {
    private static final Supplier<Map<String, Function<Domain12, Object>>> DOMAIN12_MAP_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain12, Object>>> DOMAIN12_REFLECTION_SUPPLIER;

    //    MANUALLY
    private static final Supplier<Map<String, Function<Domain12, Object>>> DOMAIN12_HANDLING_SUPPLIER;

    //    MAP
    private static final Supplier<Map<String, Function<Domain12, Object>>> DOMAIN12_GENERATION_SUPPLIER;

    static {
        HashMap<String, Function<Domain12, Object>> map = new HashMap<>(12);
        map.put("int0", Domain12::getInt0);
        map.put("int1", Domain12::getInt1);
        map.put("int2", Domain12::getInt2);
        map.put("long0", Domain12::getLong0);
        map.put("long1", Domain12::getLong1);
        map.put("long2", Domain12::getLong2);
        map.put("bool0", Domain12::isBool0);
        map.put("bool1", Domain12::isBool1);
        map.put("bool2", Domain12::isBool2);
        map.put("string0", Domain12::getString0);
        map.put("string1", Domain12::getString1);
        map.put("string2", Domain12::getString2);
        DOMAIN12_MAP_SUPPLIER = () -> map;
    }

    static {
        HashMap<String, Function<Domain12, Object>> map = new HashMap<>(12);
        map.put("int0", ReflectionUtils.getMethod(Domain12.class, "getInt0"));
        map.put("int1", ReflectionUtils.getMethod(Domain12.class, "getInt1"));
        map.put("int2", ReflectionUtils.getMethod(Domain12.class, "getInt2"));
        map.put("long0", ReflectionUtils.getMethod(Domain12.class, "getLong0"));
        map.put("long1", ReflectionUtils.getMethod(Domain12.class, "getLong1"));
        map.put("long2", ReflectionUtils.getMethod(Domain12.class, "getLong2"));
        map.put("bool0", ReflectionUtils.getMethod(Domain12.class, "isBool0"));
        map.put("bool1", ReflectionUtils.getMethod(Domain12.class, "isBool1"));
        map.put("bool2", ReflectionUtils.getMethod(Domain12.class, "isBool2"));
        map.put("string0", ReflectionUtils.getMethod(Domain12.class, "getString0"));
        map.put("string1", ReflectionUtils.getMethod(Domain12.class, "getString1"));
        map.put("string2", ReflectionUtils.getMethod(Domain12.class, "getString2"));
        DOMAIN12_REFLECTION_SUPPLIER = () -> map;
    }

//    REFLECTION

    static {
        HashMap<String, Function<Domain12, Object>> map = new HashMap<>(12);
        map.put("int0", HandlingUtils.getMethod(Domain12.class, "getInt0", int.class));
        map.put("int1", HandlingUtils.getMethod(Domain12.class, "getInt1", int.class));
        map.put("int2", HandlingUtils.getMethod(Domain12.class, "getInt2", int.class));
        map.put("long0", HandlingUtils.getMethod(Domain12.class, "getLong0", long.class));
        map.put("long1", HandlingUtils.getMethod(Domain12.class, "getLong1", long.class));
        map.put("long2", HandlingUtils.getMethod(Domain12.class, "getLong2", long.class));
        map.put("bool0", HandlingUtils.getMethod(Domain12.class, "isBool0", boolean.class));
        map.put("bool1", HandlingUtils.getMethod(Domain12.class, "isBool1", boolean.class));
        map.put("bool2", HandlingUtils.getMethod(Domain12.class, "isBool2", boolean.class));
        map.put("string0", HandlingUtils.getMethod(Domain12.class, "getString0", String.class));
        map.put("string1", HandlingUtils.getMethod(Domain12.class, "getString1", String.class));
        map.put("string2", HandlingUtils.getMethod(Domain12.class, "getString2", String.class));
        DOMAIN12_HANDLING_SUPPLIER = () -> map;
    }

    static {
        DOMAIN12_GENERATION_SUPPLIER = new Domain12GetterSupplier();
    }

    @SneakyThrows(RunnerException.class)
    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(GetterSupplierBenchmarks.class.getSimpleName())
                .build();
        Runner runner = new Runner(options);
        runner.run();
    }

//    METHOD HANDLING

    private <T> List<Object> getDomainValues(T domain, Supplier<Map<String, Function<T, Object>>> supplier) {
        Map<String, Function<T, Object>> getters = supplier.get();
        ArrayList<Object> values = new ArrayList<>(getters.size());
        for (Function<T, Object> getter : getters.values()) {
            Object value = getter.apply(domain);
            values.add(value);
        }
        return values;
    }

    @Benchmark
    public Object get_allValuesOf_domain12_via_manually() {
        Domain12 domain = new Domain12();
        ArrayList<Object> values = new ArrayList<>(12);
        values.add(domain.getInt0());
        values.add(domain.getInt1());
        values.add(domain.getInt2());
        values.add(domain.getLong0());
        values.add(domain.getLong1());
        values.add(domain.getLong2());
        values.add(domain.isBool0());
        values.add(domain.isBool1());
        values.add(domain.isBool2());
        values.add(domain.getString0());
        values.add(domain.getString1());
        values.add(domain.getString2());
        return values;
    }

    @Benchmark
    public Object get_allValuesOf_domain12_via_map() {
        return getDomainValues(new Domain12(), DOMAIN12_MAP_SUPPLIER);
    }

//    GENERATION

    @Benchmark
    public Object get_allValuesOf_domain12_via_reflection() {
        return getDomainValues(new Domain12(), DOMAIN12_REFLECTION_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain12_via_handling() {
        return getDomainValues(new Domain12(), DOMAIN12_HANDLING_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain12_via_generation() {
        return getDomainValues(new Domain12(), DOMAIN12_GENERATION_SUPPLIER);
    }
}
