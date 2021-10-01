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

import dev.alexengrig.metter.benchmark.domain.Domain128;
import dev.alexengrig.metter.benchmark.domain.Domain16;
import dev.alexengrig.metter.benchmark.domain.Domain32;
import dev.alexengrig.metter.benchmark.domain.Domain64;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN128_CUSTOM_MAP_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN128_GENERATION_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN128_HANDLING_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN128_MAP_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN128_REFLECTION_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN16_GENERATION_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN16_HANDLING_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN16_MAP_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN16_REFLECTION_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN32_GENERATION_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN32_HANDLING_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN32_MAP_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN32_REFLECTION_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN64_GENERATION_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN64_HANDLING_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN64_MAP_SUPPLIER;
import static dev.alexengrig.metter.benchmark.GetterSupplierPayloads.DOMAIN64_REFLECTION_SUPPLIER;

@Fork(value = 1, jvmArgs = "-ea")
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 10, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@SuppressWarnings("DuplicatedCode")
public class GetterSupplierBenchmarks {

    private static <T> List<Object> getDomainValues(T domain, Supplier<Map<String, Function<T, Object>>> supplier) {
        Map<String, Function<T, Object>> getters = supplier.get();
        ArrayList<Object> values = new ArrayList<>(getters.size());
        for (Function<T, Object> getter : getters.values()) {
            Object value = getter.apply(domain);
            values.add(value);
        }
        return values;
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

//    64

    @Benchmark
    public Object get_allValuesOf_domain64_via_manually() {
        Domain64 domain = new Domain64();
        ArrayList<Object> values = new ArrayList<>(64);
        values.add(domain.getInt0());
        values.add(domain.getInt1());
        values.add(domain.getInt2());
        values.add(domain.getInt3());
        values.add(domain.getInt4());
        values.add(domain.getInt5());
        values.add(domain.getInt6());
        values.add(domain.getInt7());
        values.add(domain.getInt8());
        values.add(domain.getInt9());
        values.add(domain.getInt10());
        values.add(domain.getInt11());
        values.add(domain.getInt12());
        values.add(domain.getInt13());
        values.add(domain.getInt14());
        values.add(domain.getInt15());
        values.add(domain.getLong0());
        values.add(domain.getLong1());
        values.add(domain.getLong2());
        values.add(domain.getLong3());
        values.add(domain.getLong4());
        values.add(domain.getLong5());
        values.add(domain.getLong6());
        values.add(domain.getLong7());
        values.add(domain.getLong8());
        values.add(domain.getLong9());
        values.add(domain.getLong10());
        values.add(domain.getLong11());
        values.add(domain.getLong12());
        values.add(domain.getLong13());
        values.add(domain.getLong14());
        values.add(domain.getLong15());
        values.add(domain.isBool0());
        values.add(domain.isBool1());
        values.add(domain.isBool2());
        values.add(domain.isBool3());
        values.add(domain.isBool4());
        values.add(domain.isBool5());
        values.add(domain.isBool6());
        values.add(domain.isBool7());
        values.add(domain.isBool8());
        values.add(domain.isBool9());
        values.add(domain.isBool10());
        values.add(domain.isBool11());
        values.add(domain.isBool12());
        values.add(domain.isBool13());
        values.add(domain.isBool14());
        values.add(domain.isBool15());
        values.add(domain.getString0());
        values.add(domain.getString1());
        values.add(domain.getString2());
        values.add(domain.getString3());
        values.add(domain.getString4());
        values.add(domain.getString5());
        values.add(domain.getString6());
        values.add(domain.getString7());
        values.add(domain.getString8());
        values.add(domain.getString9());
        values.add(domain.getString10());
        values.add(domain.getString11());
        values.add(domain.getString12());
        values.add(domain.getString13());
        values.add(domain.getString14());
        values.add(domain.getString15());
        assert values.size() == 64 : "Values size must be 64, not " + values.size();
        return values;
    }

    @Benchmark
    public Object get_allValuesOf_domain64_via_map() {
        return getDomainValues(new Domain64(), DOMAIN64_MAP_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain64_via_reflection() {
        return getDomainValues(new Domain64(), DOMAIN64_REFLECTION_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain64_via_handling() {
        return getDomainValues(new Domain64(), DOMAIN64_HANDLING_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain64_via_generation() {
        return getDomainValues(new Domain64(), DOMAIN64_GENERATION_SUPPLIER);
    }

//    128

    @Benchmark
    public Object get_allValuesOf_domain128_via_manually() {
        Domain128 domain = new Domain128();
        ArrayList<Object> values = new ArrayList<>(64);
        values.add(domain.getInt0());
        values.add(domain.getInt1());
        values.add(domain.getInt2());
        values.add(domain.getInt3());
        values.add(domain.getInt4());
        values.add(domain.getInt5());
        values.add(domain.getInt6());
        values.add(domain.getInt7());
        values.add(domain.getInt8());
        values.add(domain.getInt9());
        values.add(domain.getInt10());
        values.add(domain.getInt11());
        values.add(domain.getInt12());
        values.add(domain.getInt13());
        values.add(domain.getInt14());
        values.add(domain.getInt15());
        values.add(domain.getInt16());
        values.add(domain.getInt17());
        values.add(domain.getInt18());
        values.add(domain.getInt19());
        values.add(domain.getInt20());
        values.add(domain.getInt21());
        values.add(domain.getInt22());
        values.add(domain.getInt23());
        values.add(domain.getInt24());
        values.add(domain.getInt25());
        values.add(domain.getInt26());
        values.add(domain.getInt27());
        values.add(domain.getInt28());
        values.add(domain.getInt29());
        values.add(domain.getInt30());
        values.add(domain.getInt31());
        values.add(domain.getLong0());
        values.add(domain.getLong1());
        values.add(domain.getLong2());
        values.add(domain.getLong3());
        values.add(domain.getLong4());
        values.add(domain.getLong5());
        values.add(domain.getLong6());
        values.add(domain.getLong7());
        values.add(domain.getLong8());
        values.add(domain.getLong9());
        values.add(domain.getLong10());
        values.add(domain.getLong11());
        values.add(domain.getLong12());
        values.add(domain.getLong13());
        values.add(domain.getLong14());
        values.add(domain.getLong15());
        values.add(domain.getLong16());
        values.add(domain.getLong17());
        values.add(domain.getLong18());
        values.add(domain.getLong19());
        values.add(domain.getLong20());
        values.add(domain.getLong21());
        values.add(domain.getLong22());
        values.add(domain.getLong23());
        values.add(domain.getLong24());
        values.add(domain.getLong25());
        values.add(domain.getLong26());
        values.add(domain.getLong27());
        values.add(domain.getLong28());
        values.add(domain.getLong29());
        values.add(domain.getLong30());
        values.add(domain.getLong31());
        values.add(domain.isBool0());
        values.add(domain.isBool1());
        values.add(domain.isBool2());
        values.add(domain.isBool3());
        values.add(domain.isBool4());
        values.add(domain.isBool5());
        values.add(domain.isBool6());
        values.add(domain.isBool7());
        values.add(domain.isBool8());
        values.add(domain.isBool9());
        values.add(domain.isBool10());
        values.add(domain.isBool11());
        values.add(domain.isBool12());
        values.add(domain.isBool13());
        values.add(domain.isBool14());
        values.add(domain.isBool15());
        values.add(domain.isBool16());
        values.add(domain.isBool17());
        values.add(domain.isBool18());
        values.add(domain.isBool19());
        values.add(domain.isBool20());
        values.add(domain.isBool21());
        values.add(domain.isBool22());
        values.add(domain.isBool23());
        values.add(domain.isBool24());
        values.add(domain.isBool25());
        values.add(domain.isBool26());
        values.add(domain.isBool27());
        values.add(domain.isBool28());
        values.add(domain.isBool29());
        values.add(domain.isBool30());
        values.add(domain.isBool31());
        values.add(domain.getString0());
        values.add(domain.getString1());
        values.add(domain.getString2());
        values.add(domain.getString3());
        values.add(domain.getString4());
        values.add(domain.getString5());
        values.add(domain.getString6());
        values.add(domain.getString7());
        values.add(domain.getString8());
        values.add(domain.getString9());
        values.add(domain.getString10());
        values.add(domain.getString11());
        values.add(domain.getString12());
        values.add(domain.getString13());
        values.add(domain.getString14());
        values.add(domain.getString15());
        values.add(domain.getString16());
        values.add(domain.getString17());
        values.add(domain.getString18());
        values.add(domain.getString19());
        values.add(domain.getString20());
        values.add(domain.getString21());
        values.add(domain.getString22());
        values.add(domain.getString23());
        values.add(domain.getString24());
        values.add(domain.getString25());
        values.add(domain.getString26());
        values.add(domain.getString27());
        values.add(domain.getString28());
        values.add(domain.getString29());
        values.add(domain.getString30());
        values.add(domain.getString31());
        assert values.size() == 128 : "Values size must be 128, not " + values.size();
        return values;
    }

    @Benchmark
    public Object get_allValuesOf_domain128_via_map() {
        return getDomainValues(new Domain128(), DOMAIN128_MAP_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain128_via_reflection() {
        return getDomainValues(new Domain128(), DOMAIN128_REFLECTION_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain128_via_handling() {
        return getDomainValues(new Domain128(), DOMAIN128_HANDLING_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain128_via_custom_map() {
        return getDomainValues(new Domain128(), DOMAIN128_CUSTOM_MAP_SUPPLIER);
    }

    @Benchmark
    public Object get_allValuesOf_domain128_via_generation() {
        return getDomainValues(new Domain128(), DOMAIN128_GENERATION_SUPPLIER);
    }

}
