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

import dev.alexengrig.metter.benchmark.domain.Domain16;
import dev.alexengrig.metter.benchmark.domain.Domain16GetterSupplier;
import dev.alexengrig.metter.benchmark.domain.Domain32;
import dev.alexengrig.metter.benchmark.domain.Domain32GetterSupplier;
import dev.alexengrig.metter.benchmark.util.HandlingUtils;
import dev.alexengrig.metter.benchmark.util.ReflectionUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
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

@Fork(value = 1, jvmArgs = "-ea")
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 10, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public class GetterSupplierBenchmarks {

//    16

    private static final Supplier<Map<String, Function<Domain16, Object>>> DOMAIN16_MAP_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain16, Object>>> DOMAIN16_REFLECTION_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain16, Object>>> DOMAIN16_HANDLING_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain16, Object>>> DOMAIN16_GENERATION_SUPPLIER;

//    32

    private static final Supplier<Map<String, Function<Domain32, Object>>> DOMAIN32_MAP_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain32, Object>>> DOMAIN32_REFLECTION_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain32, Object>>> DOMAIN32_HANDLING_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain32, Object>>> DOMAIN32_GENERATION_SUPPLIER;

    static {
//        16
        HashMap<String, Function<Domain16, Object>> map16 = new HashMap<String, Function<Domain16, Object>>(16) {{
            put("int0", Domain16::getInt0);
            put("int1", Domain16::getInt1);
            put("int2", Domain16::getInt2);
            put("int3", Domain16::getInt3);
            put("long0", Domain16::getLong0);
            put("long1", Domain16::getLong1);
            put("long2", Domain16::getLong2);
            put("long3", Domain16::getLong3);
            put("bool0", Domain16::isBool0);
            put("bool1", Domain16::isBool1);
            put("bool2", Domain16::isBool2);
            put("bool3", Domain16::isBool3);
            put("string0", Domain16::getString0);
            put("string1", Domain16::getString1);
            put("string2", Domain16::getString2);
            put("string3", Domain16::getString3);
        }};
        assert map16.size() == 16 : "Map size must be 16, not " + map16.size();
        DOMAIN16_MAP_SUPPLIER = () -> map16;
        HashMap<String, Function<Domain16, Object>> reflectionMap16 = new HashMap<String, Function<Domain16, Object>>(16) {{
            put("int0", ReflectionUtils.getMethod(Domain16.class, "getInt0"));
            put("int1", ReflectionUtils.getMethod(Domain16.class, "getInt1"));
            put("int2", ReflectionUtils.getMethod(Domain16.class, "getInt2"));
            put("int3", ReflectionUtils.getMethod(Domain16.class, "getInt3"));
            put("long0", ReflectionUtils.getMethod(Domain16.class, "getLong0"));
            put("long1", ReflectionUtils.getMethod(Domain16.class, "getLong1"));
            put("long2", ReflectionUtils.getMethod(Domain16.class, "getLong2"));
            put("long3", ReflectionUtils.getMethod(Domain16.class, "getLong3"));
            put("bool0", ReflectionUtils.getMethod(Domain16.class, "isBool0"));
            put("bool1", ReflectionUtils.getMethod(Domain16.class, "isBool1"));
            put("bool2", ReflectionUtils.getMethod(Domain16.class, "isBool2"));
            put("bool3", ReflectionUtils.getMethod(Domain16.class, "isBool3"));
            put("string0", ReflectionUtils.getMethod(Domain16.class, "getString0"));
            put("string1", ReflectionUtils.getMethod(Domain16.class, "getString1"));
            put("string2", ReflectionUtils.getMethod(Domain16.class, "getString2"));
            put("string3", ReflectionUtils.getMethod(Domain16.class, "getString3"));
        }};
        assert reflectionMap16.size() == 16 : "Reflection map size must be 16, not " + reflectionMap16.size();
        DOMAIN16_REFLECTION_SUPPLIER = () -> reflectionMap16;
        HashMap<String, Function<Domain16, Object>> handlingMap16 = new HashMap<String, Function<Domain16, Object>>(16) {{
            put("int0", HandlingUtils.getMethod(Domain16.class, "getInt0", int.class));
            put("int1", HandlingUtils.getMethod(Domain16.class, "getInt1", int.class));
            put("int2", HandlingUtils.getMethod(Domain16.class, "getInt2", int.class));
            put("int3", HandlingUtils.getMethod(Domain16.class, "getInt3", int.class));
            put("long0", HandlingUtils.getMethod(Domain16.class, "getLong0", long.class));
            put("long1", HandlingUtils.getMethod(Domain16.class, "getLong1", long.class));
            put("long2", HandlingUtils.getMethod(Domain16.class, "getLong2", long.class));
            put("long3", HandlingUtils.getMethod(Domain16.class, "getLong3", long.class));
            put("bool0", HandlingUtils.getMethod(Domain16.class, "isBool0", boolean.class));
            put("bool1", HandlingUtils.getMethod(Domain16.class, "isBool1", boolean.class));
            put("bool2", HandlingUtils.getMethod(Domain16.class, "isBool2", boolean.class));
            put("bool3", HandlingUtils.getMethod(Domain16.class, "isBool3", boolean.class));
            put("string0", HandlingUtils.getMethod(Domain16.class, "getString0", String.class));
            put("string1", HandlingUtils.getMethod(Domain16.class, "getString1", String.class));
            put("string2", HandlingUtils.getMethod(Domain16.class, "getString2", String.class));
            put("string3", HandlingUtils.getMethod(Domain16.class, "getString3", String.class));
        }};
        assert handlingMap16.size() == 16 : "Handling map size must be 16, not " + handlingMap16.size();
        DOMAIN16_HANDLING_SUPPLIER = () -> handlingMap16;
        Domain16GetterSupplier getterSupplier16 = new Domain16GetterSupplier();
        assert getterSupplier16.get().size() == 16 : "Getter supplier map size must be 16, not " + getterSupplier16.get().size();
        DOMAIN16_GENERATION_SUPPLIER = getterSupplier16;
//        32
        HashMap<String, Function<Domain32, Object>> map32 = new HashMap<String, Function<Domain32, Object>>(32) {{
            put("int0", Domain32::getInt0);
            put("int1", Domain32::getInt1);
            put("int2", Domain32::getInt2);
            put("int3", Domain32::getInt3);
            put("int4", Domain32::getInt4);
            put("int5", Domain32::getInt5);
            put("int6", Domain32::getInt6);
            put("int7", Domain32::getInt7);
            put("long0", Domain32::getLong0);
            put("long1", Domain32::getLong1);
            put("long2", Domain32::getLong2);
            put("long3", Domain32::getLong3);
            put("long4", Domain32::getLong4);
            put("long5", Domain32::getLong5);
            put("long6", Domain32::getLong6);
            put("long7", Domain32::getLong7);
            put("bool0", Domain32::isBool0);
            put("bool1", Domain32::isBool1);
            put("bool2", Domain32::isBool2);
            put("bool3", Domain32::isBool3);
            put("bool4", Domain32::isBool4);
            put("bool5", Domain32::isBool5);
            put("bool6", Domain32::isBool6);
            put("bool7", Domain32::isBool7);
            put("string0", Domain32::getString0);
            put("string1", Domain32::getString1);
            put("string2", Domain32::getString2);
            put("string3", Domain32::getString3);
            put("string4", Domain32::getString4);
            put("string5", Domain32::getString5);
            put("string6", Domain32::getString6);
            put("string7", Domain32::getString7);
        }};
        assert map32.size() == 32 : "Map size must be 32, not " + map32.size();
        DOMAIN32_MAP_SUPPLIER = () -> map32;
        HashMap<String, Function<Domain32, Object>> reflectionMap32 = new HashMap<String, Function<Domain32, Object>>(32) {{
            put("int0", ReflectionUtils.getMethod(Domain32.class, "getInt0"));
            put("int1", ReflectionUtils.getMethod(Domain32.class, "getInt1"));
            put("int2", ReflectionUtils.getMethod(Domain32.class, "getInt2"));
            put("int3", ReflectionUtils.getMethod(Domain32.class, "getInt3"));
            put("int4", ReflectionUtils.getMethod(Domain32.class, "getInt4"));
            put("int5", ReflectionUtils.getMethod(Domain32.class, "getInt5"));
            put("int6", ReflectionUtils.getMethod(Domain32.class, "getInt6"));
            put("int7", ReflectionUtils.getMethod(Domain32.class, "getInt7"));
            put("long0", ReflectionUtils.getMethod(Domain32.class, "getLong0"));
            put("long1", ReflectionUtils.getMethod(Domain32.class, "getLong1"));
            put("long2", ReflectionUtils.getMethod(Domain32.class, "getLong2"));
            put("long3", ReflectionUtils.getMethod(Domain32.class, "getLong3"));
            put("long4", ReflectionUtils.getMethod(Domain32.class, "getLong4"));
            put("long5", ReflectionUtils.getMethod(Domain32.class, "getLong5"));
            put("long6", ReflectionUtils.getMethod(Domain32.class, "getLong6"));
            put("long7", ReflectionUtils.getMethod(Domain32.class, "getLong7"));
            put("bool0", ReflectionUtils.getMethod(Domain32.class, "isBool0"));
            put("bool1", ReflectionUtils.getMethod(Domain32.class, "isBool1"));
            put("bool2", ReflectionUtils.getMethod(Domain32.class, "isBool2"));
            put("bool3", ReflectionUtils.getMethod(Domain32.class, "isBool3"));
            put("bool4", ReflectionUtils.getMethod(Domain32.class, "isBool4"));
            put("bool5", ReflectionUtils.getMethod(Domain32.class, "isBool5"));
            put("bool6", ReflectionUtils.getMethod(Domain32.class, "isBool6"));
            put("bool7", ReflectionUtils.getMethod(Domain32.class, "isBool7"));
            put("string0", ReflectionUtils.getMethod(Domain32.class, "getString0"));
            put("string1", ReflectionUtils.getMethod(Domain32.class, "getString1"));
            put("string2", ReflectionUtils.getMethod(Domain32.class, "getString2"));
            put("string3", ReflectionUtils.getMethod(Domain32.class, "getString3"));
            put("string4", ReflectionUtils.getMethod(Domain32.class, "getString4"));
            put("string5", ReflectionUtils.getMethod(Domain32.class, "getString5"));
            put("string6", ReflectionUtils.getMethod(Domain32.class, "getString6"));
            put("string7", ReflectionUtils.getMethod(Domain32.class, "getString7"));
        }};
        assert reflectionMap32.size() == 32 : "Reflection map size must be 32, not " + reflectionMap32.size();
        DOMAIN32_REFLECTION_SUPPLIER = () -> reflectionMap32;
        HashMap<String, Function<Domain32, Object>> handlingMap32 = new HashMap<String, Function<Domain32, Object>>(32) {{
            put("int0", HandlingUtils.getMethod(Domain32.class, "getInt0", int.class));
            put("int1", HandlingUtils.getMethod(Domain32.class, "getInt1", int.class));
            put("int2", HandlingUtils.getMethod(Domain32.class, "getInt2", int.class));
            put("int3", HandlingUtils.getMethod(Domain32.class, "getInt3", int.class));
            put("int4", HandlingUtils.getMethod(Domain32.class, "getInt4", int.class));
            put("int5", HandlingUtils.getMethod(Domain32.class, "getInt5", int.class));
            put("int6", HandlingUtils.getMethod(Domain32.class, "getInt6", int.class));
            put("int7", HandlingUtils.getMethod(Domain32.class, "getInt7", int.class));
            put("long0", HandlingUtils.getMethod(Domain32.class, "getLong0", long.class));
            put("long1", HandlingUtils.getMethod(Domain32.class, "getLong1", long.class));
            put("long2", HandlingUtils.getMethod(Domain32.class, "getLong2", long.class));
            put("long3", HandlingUtils.getMethod(Domain32.class, "getLong3", long.class));
            put("long4", HandlingUtils.getMethod(Domain32.class, "getLong4", long.class));
            put("long5", HandlingUtils.getMethod(Domain32.class, "getLong5", long.class));
            put("long6", HandlingUtils.getMethod(Domain32.class, "getLong6", long.class));
            put("long7", HandlingUtils.getMethod(Domain32.class, "getLong7", long.class));
            put("bool0", HandlingUtils.getMethod(Domain32.class, "isBool0", boolean.class));
            put("bool1", HandlingUtils.getMethod(Domain32.class, "isBool1", boolean.class));
            put("bool2", HandlingUtils.getMethod(Domain32.class, "isBool2", boolean.class));
            put("bool3", HandlingUtils.getMethod(Domain32.class, "isBool3", boolean.class));
            put("bool4", HandlingUtils.getMethod(Domain32.class, "isBool4", boolean.class));
            put("bool5", HandlingUtils.getMethod(Domain32.class, "isBool5", boolean.class));
            put("bool6", HandlingUtils.getMethod(Domain32.class, "isBool6", boolean.class));
            put("bool7", HandlingUtils.getMethod(Domain32.class, "isBool7", boolean.class));
            put("string0", HandlingUtils.getMethod(Domain32.class, "getString0", String.class));
            put("string1", HandlingUtils.getMethod(Domain32.class, "getString1", String.class));
            put("string2", HandlingUtils.getMethod(Domain32.class, "getString2", String.class));
            put("string3", HandlingUtils.getMethod(Domain32.class, "getString3", String.class));
            put("string4", HandlingUtils.getMethod(Domain32.class, "getString4", String.class));
            put("string5", HandlingUtils.getMethod(Domain32.class, "getString5", String.class));
            put("string6", HandlingUtils.getMethod(Domain32.class, "getString6", String.class));
            put("string7", HandlingUtils.getMethod(Domain32.class, "getString7", String.class));
        }};
        assert handlingMap32.size() == 32 : "Handling map size must be 32, not " + handlingMap32.size();
        DOMAIN32_HANDLING_SUPPLIER = () -> handlingMap32;
        Domain32GetterSupplier getterSupplier32 = new Domain32GetterSupplier();
        assert getterSupplier32.get().size() == 32 : "Getter supplier map size must be 32, not " + getterSupplier32.get().size();
        DOMAIN32_GENERATION_SUPPLIER = getterSupplier32;
    }

    private static <T> List<Object> getDomainValues(T domain, Supplier<Map<String, Function<T, Object>>> supplier) {
        Map<String, Function<T, Object>> getters = supplier.get();
        ArrayList<Object> values = new ArrayList<>(getters.size());
        for (Function<T, Object> getter : getters.values()) {
            Object value = getter.apply(domain);
            values.add(value);
        }
        return values;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(GetterSupplierBenchmarks.class.getSimpleName())
                .build();
        Runner runner = new Runner(options);
        runner.run();
    }

//    16

    @Benchmark
    public Object get_allValuesOf_domain16_via_manually() {
        Domain16 domain = new Domain16();
        ArrayList<Object> values = new ArrayList<>(16);
        values.add(domain.getInt0());
        values.add(domain.getInt1());
        values.add(domain.getInt2());
        values.add(domain.getInt3());
        values.add(domain.getLong0());
        values.add(domain.getLong1());
        values.add(domain.getLong2());
        values.add(domain.getLong3());
        values.add(domain.isBool0());
        values.add(domain.isBool1());
        values.add(domain.isBool2());
        values.add(domain.isBool3());
        values.add(domain.getString0());
        values.add(domain.getString1());
        values.add(domain.getString2());
        values.add(domain.getString3());
        assert values.size() == 16 : "Values size must be 16, not " + values.size();
        return values;
    }

    @Benchmark
    public Object get_allValuesOf_domain16_via_map() {
        return getDomainValues(new Domain16(), DOMAIN16_MAP_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain16_via_reflection() {
        return getDomainValues(new Domain16(), DOMAIN16_REFLECTION_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain16_via_handling() {
        return getDomainValues(new Domain16(), DOMAIN16_HANDLING_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain16_via_generation() {
        return getDomainValues(new Domain16(), DOMAIN16_GENERATION_SUPPLIER);
    }

//    32

    @Benchmark
    public Object get_allValuesOf_domain32_via_manually() {
        Domain32 domain = new Domain32();
        ArrayList<Object> values = new ArrayList<>(32);
        values.add(domain.getInt0());
        values.add(domain.getInt1());
        values.add(domain.getInt2());
        values.add(domain.getInt3());
        values.add(domain.getInt4());
        values.add(domain.getInt5());
        values.add(domain.getInt6());
        values.add(domain.getInt7());
        values.add(domain.getLong0());
        values.add(domain.getLong1());
        values.add(domain.getLong2());
        values.add(domain.getLong3());
        values.add(domain.getLong4());
        values.add(domain.getLong5());
        values.add(domain.getLong6());
        values.add(domain.getLong7());
        values.add(domain.isBool0());
        values.add(domain.isBool1());
        values.add(domain.isBool2());
        values.add(domain.isBool3());
        values.add(domain.isBool4());
        values.add(domain.isBool5());
        values.add(domain.isBool6());
        values.add(domain.isBool7());
        values.add(domain.getString0());
        values.add(domain.getString1());
        values.add(domain.getString2());
        values.add(domain.getString3());
        values.add(domain.getString4());
        values.add(domain.getString5());
        values.add(domain.getString6());
        values.add(domain.getString7());
        assert values.size() == 32 : "Values size must be 32, not " + values.size();
        return values;
    }

    @Benchmark
    public Object get_allValuesOf_domain32_via_map() {
        return getDomainValues(new Domain32(), DOMAIN32_MAP_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain32_via_reflection() {
        return getDomainValues(new Domain32(), DOMAIN32_REFLECTION_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain32_via_handling() {
        return getDomainValues(new Domain32(), DOMAIN32_HANDLING_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain32_via_generation() {
        return getDomainValues(new Domain32(), DOMAIN32_GENERATION_SUPPLIER);
    }
}
