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

import dev.alexengrig.metter.benchmark.domain.*;
import dev.alexengrig.metter.benchmark.experimental.CustomMatrixGettersMap;
import dev.alexengrig.metter.benchmark.util.HandlingUtils;
import dev.alexengrig.metter.benchmark.util.ReflectionUtils;
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

@Fork(value = 1, jvmArgs = "-ea")
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 3)
@Measurement(iterations = 10, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@SuppressWarnings("DuplicatedCode")
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

//    64

    private static final Supplier<Map<String, Function<Domain64, Object>>> DOMAIN64_MAP_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain64, Object>>> DOMAIN64_REFLECTION_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain64, Object>>> DOMAIN64_HANDLING_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain64, Object>>> DOMAIN64_GENERATION_SUPPLIER;

//    128

    private static final Supplier<Map<String, Function<Domain128, Object>>> DOMAIN128_MAP_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain128, Object>>> DOMAIN128_REFLECTION_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain128, Object>>> DOMAIN128_HANDLING_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain128, Object>>> DOMAIN128_CUSTOM_MAP_SUPPLIER;
    private static final Supplier<Map<String, Function<Domain128, Object>>> DOMAIN128_GENERATION_SUPPLIER;

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
//        64
        HashMap<String, Function<Domain64, Object>> map64 = new HashMap<String, Function<Domain64, Object>>(64) {{
            put("int0", Domain64::getInt0);
            put("int1", Domain64::getInt1);
            put("int2", Domain64::getInt2);
            put("int3", Domain64::getInt3);
            put("int4", Domain64::getInt4);
            put("int5", Domain64::getInt5);
            put("int6", Domain64::getInt6);
            put("int7", Domain64::getInt7);
            put("int8", Domain64::getInt8);
            put("int9", Domain64::getInt9);
            put("int10", Domain64::getInt10);
            put("int11", Domain64::getInt11);
            put("int12", Domain64::getInt12);
            put("int13", Domain64::getInt13);
            put("int14", Domain64::getInt14);
            put("int15", Domain64::getInt15);
            put("long0", Domain64::getLong0);
            put("long1", Domain64::getLong1);
            put("long2", Domain64::getLong2);
            put("long3", Domain64::getLong3);
            put("long4", Domain64::getLong4);
            put("long5", Domain64::getLong5);
            put("long6", Domain64::getLong6);
            put("long7", Domain64::getLong7);
            put("long8", Domain64::getLong8);
            put("long9", Domain64::getLong9);
            put("long10", Domain64::getLong10);
            put("long11", Domain64::getLong11);
            put("long12", Domain64::getLong12);
            put("long13", Domain64::getLong13);
            put("long14", Domain64::getLong14);
            put("long15", Domain64::getLong15);
            put("bool0", Domain64::isBool0);
            put("bool1", Domain64::isBool1);
            put("bool2", Domain64::isBool2);
            put("bool3", Domain64::isBool3);
            put("bool4", Domain64::isBool4);
            put("bool5", Domain64::isBool5);
            put("bool6", Domain64::isBool6);
            put("bool7", Domain64::isBool7);
            put("bool8", Domain64::isBool8);
            put("bool9", Domain64::isBool9);
            put("bool10", Domain64::isBool10);
            put("bool11", Domain64::isBool11);
            put("bool12", Domain64::isBool12);
            put("bool13", Domain64::isBool13);
            put("bool14", Domain64::isBool14);
            put("bool15", Domain64::isBool15);
            put("string0", Domain64::getString0);
            put("string1", Domain64::getString1);
            put("string2", Domain64::getString2);
            put("string3", Domain64::getString3);
            put("string4", Domain64::getString4);
            put("string5", Domain64::getString5);
            put("string6", Domain64::getString6);
            put("string7", Domain64::getString7);
            put("string8", Domain64::getString8);
            put("string9", Domain64::getString9);
            put("string10", Domain64::getString10);
            put("string11", Domain64::getString11);
            put("string12", Domain64::getString12);
            put("string13", Domain64::getString13);
            put("string14", Domain64::getString14);
            put("string15", Domain64::getString15);
        }};
        assert map64.size() == 64 : "Map size must be 64, not " + map64.size();
        DOMAIN64_MAP_SUPPLIER = () -> map64;
        HashMap<String, Function<Domain64, Object>> reflectionMap64 = new HashMap<String, Function<Domain64, Object>>(64) {{
            put("int0", ReflectionUtils.getMethod(Domain64.class, "getInt0"));
            put("int1", ReflectionUtils.getMethod(Domain64.class, "getInt1"));
            put("int2", ReflectionUtils.getMethod(Domain64.class, "getInt2"));
            put("int3", ReflectionUtils.getMethod(Domain64.class, "getInt3"));
            put("int4", ReflectionUtils.getMethod(Domain64.class, "getInt4"));
            put("int5", ReflectionUtils.getMethod(Domain64.class, "getInt5"));
            put("int6", ReflectionUtils.getMethod(Domain64.class, "getInt6"));
            put("int7", ReflectionUtils.getMethod(Domain64.class, "getInt7"));
            put("int8", ReflectionUtils.getMethod(Domain64.class, "getInt8"));
            put("int9", ReflectionUtils.getMethod(Domain64.class, "getInt9"));
            put("int10", ReflectionUtils.getMethod(Domain64.class, "getInt10"));
            put("int11", ReflectionUtils.getMethod(Domain64.class, "getInt11"));
            put("int12", ReflectionUtils.getMethod(Domain64.class, "getInt12"));
            put("int13", ReflectionUtils.getMethod(Domain64.class, "getInt13"));
            put("int14", ReflectionUtils.getMethod(Domain64.class, "getInt14"));
            put("int15", ReflectionUtils.getMethod(Domain64.class, "getInt15"));
            put("long0", ReflectionUtils.getMethod(Domain64.class, "getLong0"));
            put("long1", ReflectionUtils.getMethod(Domain64.class, "getLong1"));
            put("long2", ReflectionUtils.getMethod(Domain64.class, "getLong2"));
            put("long3", ReflectionUtils.getMethod(Domain64.class, "getLong3"));
            put("long4", ReflectionUtils.getMethod(Domain64.class, "getLong4"));
            put("long5", ReflectionUtils.getMethod(Domain64.class, "getLong5"));
            put("long6", ReflectionUtils.getMethod(Domain64.class, "getLong6"));
            put("long7", ReflectionUtils.getMethod(Domain64.class, "getLong7"));
            put("long8", ReflectionUtils.getMethod(Domain64.class, "getLong8"));
            put("long9", ReflectionUtils.getMethod(Domain64.class, "getLong9"));
            put("long10", ReflectionUtils.getMethod(Domain64.class, "getLong10"));
            put("long11", ReflectionUtils.getMethod(Domain64.class, "getLong11"));
            put("long12", ReflectionUtils.getMethod(Domain64.class, "getLong12"));
            put("long13", ReflectionUtils.getMethod(Domain64.class, "getLong13"));
            put("long14", ReflectionUtils.getMethod(Domain64.class, "getLong14"));
            put("long15", ReflectionUtils.getMethod(Domain64.class, "getLong15"));
            put("bool0", ReflectionUtils.getMethod(Domain64.class, "isBool0"));
            put("bool1", ReflectionUtils.getMethod(Domain64.class, "isBool1"));
            put("bool2", ReflectionUtils.getMethod(Domain64.class, "isBool2"));
            put("bool3", ReflectionUtils.getMethod(Domain64.class, "isBool3"));
            put("bool4", ReflectionUtils.getMethod(Domain64.class, "isBool4"));
            put("bool5", ReflectionUtils.getMethod(Domain64.class, "isBool5"));
            put("bool6", ReflectionUtils.getMethod(Domain64.class, "isBool6"));
            put("bool7", ReflectionUtils.getMethod(Domain64.class, "isBool7"));
            put("bool8", ReflectionUtils.getMethod(Domain64.class, "isBool8"));
            put("bool9", ReflectionUtils.getMethod(Domain64.class, "isBool9"));
            put("bool10", ReflectionUtils.getMethod(Domain64.class, "isBool10"));
            put("bool11", ReflectionUtils.getMethod(Domain64.class, "isBool11"));
            put("bool12", ReflectionUtils.getMethod(Domain64.class, "isBool12"));
            put("bool13", ReflectionUtils.getMethod(Domain64.class, "isBool13"));
            put("bool14", ReflectionUtils.getMethod(Domain64.class, "isBool14"));
            put("bool15", ReflectionUtils.getMethod(Domain64.class, "isBool15"));
            put("string0", ReflectionUtils.getMethod(Domain64.class, "getString0"));
            put("string1", ReflectionUtils.getMethod(Domain64.class, "getString1"));
            put("string2", ReflectionUtils.getMethod(Domain64.class, "getString2"));
            put("string3", ReflectionUtils.getMethod(Domain64.class, "getString3"));
            put("string4", ReflectionUtils.getMethod(Domain64.class, "getString4"));
            put("string5", ReflectionUtils.getMethod(Domain64.class, "getString5"));
            put("string6", ReflectionUtils.getMethod(Domain64.class, "getString6"));
            put("string7", ReflectionUtils.getMethod(Domain64.class, "getString7"));
            put("string8", ReflectionUtils.getMethod(Domain64.class, "getString8"));
            put("string9", ReflectionUtils.getMethod(Domain64.class, "getString9"));
            put("string10", ReflectionUtils.getMethod(Domain64.class, "getString10"));
            put("string11", ReflectionUtils.getMethod(Domain64.class, "getString11"));
            put("string12", ReflectionUtils.getMethod(Domain64.class, "getString12"));
            put("string13", ReflectionUtils.getMethod(Domain64.class, "getString13"));
            put("string14", ReflectionUtils.getMethod(Domain64.class, "getString14"));
            put("string15", ReflectionUtils.getMethod(Domain64.class, "getString15"));
        }};
        assert reflectionMap64.size() == 64 : "Reflection map size must be 64, not " + reflectionMap64.size();
        DOMAIN64_REFLECTION_SUPPLIER = () -> reflectionMap64;
        HashMap<String, Function<Domain64, Object>> handlingMap64 = new HashMap<String, Function<Domain64, Object>>(64) {{
            put("int0", HandlingUtils.getMethod(Domain64.class, "getInt0", int.class));
            put("int1", HandlingUtils.getMethod(Domain64.class, "getInt1", int.class));
            put("int2", HandlingUtils.getMethod(Domain64.class, "getInt2", int.class));
            put("int3", HandlingUtils.getMethod(Domain64.class, "getInt3", int.class));
            put("int4", HandlingUtils.getMethod(Domain64.class, "getInt4", int.class));
            put("int5", HandlingUtils.getMethod(Domain64.class, "getInt5", int.class));
            put("int6", HandlingUtils.getMethod(Domain64.class, "getInt6", int.class));
            put("int7", HandlingUtils.getMethod(Domain64.class, "getInt7", int.class));
            put("int8", HandlingUtils.getMethod(Domain64.class, "getInt8", int.class));
            put("int9", HandlingUtils.getMethod(Domain64.class, "getInt9", int.class));
            put("int10", HandlingUtils.getMethod(Domain64.class, "getInt10", int.class));
            put("int11", HandlingUtils.getMethod(Domain64.class, "getInt11", int.class));
            put("int12", HandlingUtils.getMethod(Domain64.class, "getInt12", int.class));
            put("int13", HandlingUtils.getMethod(Domain64.class, "getInt13", int.class));
            put("int14", HandlingUtils.getMethod(Domain64.class, "getInt14", int.class));
            put("int15", HandlingUtils.getMethod(Domain64.class, "getInt15", int.class));
            put("long0", HandlingUtils.getMethod(Domain64.class, "getLong0", long.class));
            put("long1", HandlingUtils.getMethod(Domain64.class, "getLong1", long.class));
            put("long2", HandlingUtils.getMethod(Domain64.class, "getLong2", long.class));
            put("long3", HandlingUtils.getMethod(Domain64.class, "getLong3", long.class));
            put("long4", HandlingUtils.getMethod(Domain64.class, "getLong4", long.class));
            put("long5", HandlingUtils.getMethod(Domain64.class, "getLong5", long.class));
            put("long6", HandlingUtils.getMethod(Domain64.class, "getLong6", long.class));
            put("long7", HandlingUtils.getMethod(Domain64.class, "getLong7", long.class));
            put("long8", HandlingUtils.getMethod(Domain64.class, "getLong8", long.class));
            put("long9", HandlingUtils.getMethod(Domain64.class, "getLong9", long.class));
            put("long10", HandlingUtils.getMethod(Domain64.class, "getLong10", long.class));
            put("long11", HandlingUtils.getMethod(Domain64.class, "getLong11", long.class));
            put("long12", HandlingUtils.getMethod(Domain64.class, "getLong12", long.class));
            put("long13", HandlingUtils.getMethod(Domain64.class, "getLong13", long.class));
            put("long14", HandlingUtils.getMethod(Domain64.class, "getLong14", long.class));
            put("long15", HandlingUtils.getMethod(Domain64.class, "getLong15", long.class));
            put("bool0", HandlingUtils.getMethod(Domain64.class, "isBool0", boolean.class));
            put("bool1", HandlingUtils.getMethod(Domain64.class, "isBool1", boolean.class));
            put("bool2", HandlingUtils.getMethod(Domain64.class, "isBool2", boolean.class));
            put("bool3", HandlingUtils.getMethod(Domain64.class, "isBool3", boolean.class));
            put("bool4", HandlingUtils.getMethod(Domain64.class, "isBool4", boolean.class));
            put("bool5", HandlingUtils.getMethod(Domain64.class, "isBool5", boolean.class));
            put("bool6", HandlingUtils.getMethod(Domain64.class, "isBool6", boolean.class));
            put("bool7", HandlingUtils.getMethod(Domain64.class, "isBool7", boolean.class));
            put("bool8", HandlingUtils.getMethod(Domain64.class, "isBool8", boolean.class));
            put("bool9", HandlingUtils.getMethod(Domain64.class, "isBool9", boolean.class));
            put("bool10", HandlingUtils.getMethod(Domain64.class, "isBool10", boolean.class));
            put("bool11", HandlingUtils.getMethod(Domain64.class, "isBool11", boolean.class));
            put("bool12", HandlingUtils.getMethod(Domain64.class, "isBool12", boolean.class));
            put("bool13", HandlingUtils.getMethod(Domain64.class, "isBool13", boolean.class));
            put("bool14", HandlingUtils.getMethod(Domain64.class, "isBool14", boolean.class));
            put("bool15", HandlingUtils.getMethod(Domain64.class, "isBool15", boolean.class));
            put("string0", HandlingUtils.getMethod(Domain64.class, "getString0", String.class));
            put("string1", HandlingUtils.getMethod(Domain64.class, "getString1", String.class));
            put("string2", HandlingUtils.getMethod(Domain64.class, "getString2", String.class));
            put("string3", HandlingUtils.getMethod(Domain64.class, "getString3", String.class));
            put("string4", HandlingUtils.getMethod(Domain64.class, "getString4", String.class));
            put("string5", HandlingUtils.getMethod(Domain64.class, "getString5", String.class));
            put("string6", HandlingUtils.getMethod(Domain64.class, "getString6", String.class));
            put("string7", HandlingUtils.getMethod(Domain64.class, "getString7", String.class));
            put("string8", HandlingUtils.getMethod(Domain64.class, "getString8", String.class));
            put("string9", HandlingUtils.getMethod(Domain64.class, "getString9", String.class));
            put("string10", HandlingUtils.getMethod(Domain64.class, "getString10", String.class));
            put("string11", HandlingUtils.getMethod(Domain64.class, "getString11", String.class));
            put("string12", HandlingUtils.getMethod(Domain64.class, "getString12", String.class));
            put("string13", HandlingUtils.getMethod(Domain64.class, "getString13", String.class));
            put("string14", HandlingUtils.getMethod(Domain64.class, "getString14", String.class));
            put("string15", HandlingUtils.getMethod(Domain64.class, "getString15", String.class));
        }};
        assert handlingMap64.size() == 64 : "Handling map size must be 64, not " + handlingMap64.size();
        DOMAIN64_HANDLING_SUPPLIER = () -> handlingMap64;
        Domain64GetterSupplier getterSupplier64 = new Domain64GetterSupplier();
        assert getterSupplier64.get().size() == 64 : "Getter supplier map size must be 64, not " + getterSupplier64.get().size();
        DOMAIN64_GENERATION_SUPPLIER = getterSupplier64;
//        128
        HashMap<String, Function<Domain128, Object>> map128 = new HashMap<String, Function<Domain128, Object>>(128) {{
            put("int0", Domain128::getInt0);
            put("int1", Domain128::getInt1);
            put("int2", Domain128::getInt2);
            put("int3", Domain128::getInt3);
            put("int4", Domain128::getInt4);
            put("int5", Domain128::getInt5);
            put("int6", Domain128::getInt6);
            put("int7", Domain128::getInt7);
            put("int8", Domain128::getInt8);
            put("int9", Domain128::getInt9);
            put("int10", Domain128::getInt10);
            put("int11", Domain128::getInt11);
            put("int12", Domain128::getInt12);
            put("int13", Domain128::getInt13);
            put("int14", Domain128::getInt14);
            put("int15", Domain128::getInt15);
            put("int16", Domain128::getInt16);
            put("int17", Domain128::getInt17);
            put("int18", Domain128::getInt18);
            put("int19", Domain128::getInt19);
            put("int20", Domain128::getInt20);
            put("int21", Domain128::getInt21);
            put("int22", Domain128::getInt22);
            put("int23", Domain128::getInt23);
            put("int24", Domain128::getInt24);
            put("int25", Domain128::getInt25);
            put("int26", Domain128::getInt26);
            put("int27", Domain128::getInt27);
            put("int28", Domain128::getInt28);
            put("int29", Domain128::getInt29);
            put("int30", Domain128::getInt30);
            put("int31", Domain128::getInt31);
            put("long0", Domain128::getLong0);
            put("long1", Domain128::getLong1);
            put("long2", Domain128::getLong2);
            put("long3", Domain128::getLong3);
            put("long4", Domain128::getLong4);
            put("long5", Domain128::getLong5);
            put("long6", Domain128::getLong6);
            put("long7", Domain128::getLong7);
            put("long8", Domain128::getLong8);
            put("long9", Domain128::getLong9);
            put("long10", Domain128::getLong10);
            put("long11", Domain128::getLong11);
            put("long12", Domain128::getLong12);
            put("long13", Domain128::getLong13);
            put("long14", Domain128::getLong14);
            put("long15", Domain128::getLong15);
            put("long16", Domain128::getLong16);
            put("long17", Domain128::getLong17);
            put("long18", Domain128::getLong18);
            put("long19", Domain128::getLong19);
            put("long20", Domain128::getLong20);
            put("long21", Domain128::getLong21);
            put("long22", Domain128::getLong22);
            put("long23", Domain128::getLong23);
            put("long24", Domain128::getLong24);
            put("long25", Domain128::getLong25);
            put("long26", Domain128::getLong26);
            put("long27", Domain128::getLong27);
            put("long28", Domain128::getLong28);
            put("long29", Domain128::getLong29);
            put("long30", Domain128::getLong30);
            put("long31", Domain128::getLong31);
            put("bool0", Domain128::isBool0);
            put("bool1", Domain128::isBool1);
            put("bool2", Domain128::isBool2);
            put("bool3", Domain128::isBool3);
            put("bool4", Domain128::isBool4);
            put("bool5", Domain128::isBool5);
            put("bool6", Domain128::isBool6);
            put("bool7", Domain128::isBool7);
            put("bool8", Domain128::isBool8);
            put("bool9", Domain128::isBool9);
            put("bool10", Domain128::isBool10);
            put("bool11", Domain128::isBool11);
            put("bool12", Domain128::isBool12);
            put("bool13", Domain128::isBool13);
            put("bool14", Domain128::isBool14);
            put("bool15", Domain128::isBool15);
            put("bool16", Domain128::isBool16);
            put("bool17", Domain128::isBool17);
            put("bool18", Domain128::isBool18);
            put("bool19", Domain128::isBool19);
            put("bool20", Domain128::isBool20);
            put("bool21", Domain128::isBool21);
            put("bool22", Domain128::isBool22);
            put("bool23", Domain128::isBool23);
            put("bool24", Domain128::isBool24);
            put("bool25", Domain128::isBool25);
            put("bool26", Domain128::isBool26);
            put("bool27", Domain128::isBool27);
            put("bool28", Domain128::isBool28);
            put("bool29", Domain128::isBool29);
            put("bool30", Domain128::isBool30);
            put("bool31", Domain128::isBool31);
            put("string0", Domain128::getString0);
            put("string1", Domain128::getString1);
            put("string2", Domain128::getString2);
            put("string3", Domain128::getString3);
            put("string4", Domain128::getString4);
            put("string5", Domain128::getString5);
            put("string6", Domain128::getString6);
            put("string7", Domain128::getString7);
            put("string8", Domain128::getString8);
            put("string9", Domain128::getString9);
            put("string10", Domain128::getString10);
            put("string11", Domain128::getString11);
            put("string12", Domain128::getString12);
            put("string13", Domain128::getString13);
            put("string14", Domain128::getString14);
            put("string15", Domain128::getString15);
            put("string16", Domain128::getString16);
            put("string17", Domain128::getString17);
            put("string18", Domain128::getString18);
            put("string19", Domain128::getString19);
            put("string20", Domain128::getString20);
            put("string21", Domain128::getString21);
            put("string22", Domain128::getString22);
            put("string23", Domain128::getString23);
            put("string24", Domain128::getString24);
            put("string25", Domain128::getString25);
            put("string26", Domain128::getString26);
            put("string27", Domain128::getString27);
            put("string28", Domain128::getString28);
            put("string29", Domain128::getString29);
            put("string30", Domain128::getString30);
            put("string31", Domain128::getString31);
        }};
        assert map128.size() == 128 : "Map size must be 128, not " + map128.size();
        DOMAIN128_MAP_SUPPLIER = () -> map128;
        HashMap<String, Function<Domain128, Object>> reflectionMap128 = new HashMap<String, Function<Domain128, Object>>(128) {{
            put("int0", ReflectionUtils.getMethod(Domain128.class, "getInt0"));
            put("int1", ReflectionUtils.getMethod(Domain128.class, "getInt1"));
            put("int2", ReflectionUtils.getMethod(Domain128.class, "getInt2"));
            put("int3", ReflectionUtils.getMethod(Domain128.class, "getInt3"));
            put("int4", ReflectionUtils.getMethod(Domain128.class, "getInt4"));
            put("int5", ReflectionUtils.getMethod(Domain128.class, "getInt5"));
            put("int6", ReflectionUtils.getMethod(Domain128.class, "getInt6"));
            put("int7", ReflectionUtils.getMethod(Domain128.class, "getInt7"));
            put("int8", ReflectionUtils.getMethod(Domain128.class, "getInt8"));
            put("int9", ReflectionUtils.getMethod(Domain128.class, "getInt9"));
            put("int10", ReflectionUtils.getMethod(Domain128.class, "getInt10"));
            put("int11", ReflectionUtils.getMethod(Domain128.class, "getInt11"));
            put("int12", ReflectionUtils.getMethod(Domain128.class, "getInt12"));
            put("int13", ReflectionUtils.getMethod(Domain128.class, "getInt13"));
            put("int14", ReflectionUtils.getMethod(Domain128.class, "getInt14"));
            put("int15", ReflectionUtils.getMethod(Domain128.class, "getInt15"));
            put("int16", ReflectionUtils.getMethod(Domain128.class, "getInt16"));
            put("int17", ReflectionUtils.getMethod(Domain128.class, "getInt17"));
            put("int18", ReflectionUtils.getMethod(Domain128.class, "getInt18"));
            put("int19", ReflectionUtils.getMethod(Domain128.class, "getInt19"));
            put("int20", ReflectionUtils.getMethod(Domain128.class, "getInt20"));
            put("int21", ReflectionUtils.getMethod(Domain128.class, "getInt21"));
            put("int22", ReflectionUtils.getMethod(Domain128.class, "getInt22"));
            put("int23", ReflectionUtils.getMethod(Domain128.class, "getInt23"));
            put("int24", ReflectionUtils.getMethod(Domain128.class, "getInt24"));
            put("int25", ReflectionUtils.getMethod(Domain128.class, "getInt25"));
            put("int26", ReflectionUtils.getMethod(Domain128.class, "getInt26"));
            put("int27", ReflectionUtils.getMethod(Domain128.class, "getInt27"));
            put("int28", ReflectionUtils.getMethod(Domain128.class, "getInt28"));
            put("int29", ReflectionUtils.getMethod(Domain128.class, "getInt29"));
            put("int30", ReflectionUtils.getMethod(Domain128.class, "getInt30"));
            put("int31", ReflectionUtils.getMethod(Domain128.class, "getInt31"));
            put("long0", ReflectionUtils.getMethod(Domain128.class, "getLong0"));
            put("long1", ReflectionUtils.getMethod(Domain128.class, "getLong1"));
            put("long2", ReflectionUtils.getMethod(Domain128.class, "getLong2"));
            put("long3", ReflectionUtils.getMethod(Domain128.class, "getLong3"));
            put("long4", ReflectionUtils.getMethod(Domain128.class, "getLong4"));
            put("long5", ReflectionUtils.getMethod(Domain128.class, "getLong5"));
            put("long6", ReflectionUtils.getMethod(Domain128.class, "getLong6"));
            put("long7", ReflectionUtils.getMethod(Domain128.class, "getLong7"));
            put("long8", ReflectionUtils.getMethod(Domain128.class, "getLong8"));
            put("long9", ReflectionUtils.getMethod(Domain128.class, "getLong9"));
            put("long10", ReflectionUtils.getMethod(Domain128.class, "getLong10"));
            put("long11", ReflectionUtils.getMethod(Domain128.class, "getLong11"));
            put("long12", ReflectionUtils.getMethod(Domain128.class, "getLong12"));
            put("long13", ReflectionUtils.getMethod(Domain128.class, "getLong13"));
            put("long14", ReflectionUtils.getMethod(Domain128.class, "getLong14"));
            put("long15", ReflectionUtils.getMethod(Domain128.class, "getLong15"));
            put("long16", ReflectionUtils.getMethod(Domain128.class, "getLong16"));
            put("long17", ReflectionUtils.getMethod(Domain128.class, "getLong17"));
            put("long18", ReflectionUtils.getMethod(Domain128.class, "getLong18"));
            put("long19", ReflectionUtils.getMethod(Domain128.class, "getLong19"));
            put("long20", ReflectionUtils.getMethod(Domain128.class, "getLong20"));
            put("long21", ReflectionUtils.getMethod(Domain128.class, "getLong21"));
            put("long22", ReflectionUtils.getMethod(Domain128.class, "getLong22"));
            put("long23", ReflectionUtils.getMethod(Domain128.class, "getLong23"));
            put("long24", ReflectionUtils.getMethod(Domain128.class, "getLong24"));
            put("long25", ReflectionUtils.getMethod(Domain128.class, "getLong25"));
            put("long26", ReflectionUtils.getMethod(Domain128.class, "getLong26"));
            put("long27", ReflectionUtils.getMethod(Domain128.class, "getLong27"));
            put("long28", ReflectionUtils.getMethod(Domain128.class, "getLong28"));
            put("long29", ReflectionUtils.getMethod(Domain128.class, "getLong29"));
            put("long30", ReflectionUtils.getMethod(Domain128.class, "getLong30"));
            put("long31", ReflectionUtils.getMethod(Domain128.class, "getLong31"));
            put("bool0", ReflectionUtils.getMethod(Domain128.class, "isBool0"));
            put("bool1", ReflectionUtils.getMethod(Domain128.class, "isBool1"));
            put("bool2", ReflectionUtils.getMethod(Domain128.class, "isBool2"));
            put("bool3", ReflectionUtils.getMethod(Domain128.class, "isBool3"));
            put("bool4", ReflectionUtils.getMethod(Domain128.class, "isBool4"));
            put("bool5", ReflectionUtils.getMethod(Domain128.class, "isBool5"));
            put("bool6", ReflectionUtils.getMethod(Domain128.class, "isBool6"));
            put("bool7", ReflectionUtils.getMethod(Domain128.class, "isBool7"));
            put("bool8", ReflectionUtils.getMethod(Domain128.class, "isBool8"));
            put("bool9", ReflectionUtils.getMethod(Domain128.class, "isBool9"));
            put("bool10", ReflectionUtils.getMethod(Domain128.class, "isBool10"));
            put("bool11", ReflectionUtils.getMethod(Domain128.class, "isBool11"));
            put("bool12", ReflectionUtils.getMethod(Domain128.class, "isBool12"));
            put("bool13", ReflectionUtils.getMethod(Domain128.class, "isBool13"));
            put("bool14", ReflectionUtils.getMethod(Domain128.class, "isBool14"));
            put("bool15", ReflectionUtils.getMethod(Domain128.class, "isBool15"));
            put("bool16", ReflectionUtils.getMethod(Domain128.class, "isBool16"));
            put("bool17", ReflectionUtils.getMethod(Domain128.class, "isBool17"));
            put("bool18", ReflectionUtils.getMethod(Domain128.class, "isBool18"));
            put("bool19", ReflectionUtils.getMethod(Domain128.class, "isBool19"));
            put("bool20", ReflectionUtils.getMethod(Domain128.class, "isBool20"));
            put("bool21", ReflectionUtils.getMethod(Domain128.class, "isBool21"));
            put("bool22", ReflectionUtils.getMethod(Domain128.class, "isBool22"));
            put("bool23", ReflectionUtils.getMethod(Domain128.class, "isBool23"));
            put("bool24", ReflectionUtils.getMethod(Domain128.class, "isBool24"));
            put("bool25", ReflectionUtils.getMethod(Domain128.class, "isBool25"));
            put("bool26", ReflectionUtils.getMethod(Domain128.class, "isBool26"));
            put("bool27", ReflectionUtils.getMethod(Domain128.class, "isBool27"));
            put("bool28", ReflectionUtils.getMethod(Domain128.class, "isBool28"));
            put("bool29", ReflectionUtils.getMethod(Domain128.class, "isBool29"));
            put("bool30", ReflectionUtils.getMethod(Domain128.class, "isBool30"));
            put("bool31", ReflectionUtils.getMethod(Domain128.class, "isBool31"));
            put("string0", ReflectionUtils.getMethod(Domain128.class, "getString0"));
            put("string1", ReflectionUtils.getMethod(Domain128.class, "getString1"));
            put("string2", ReflectionUtils.getMethod(Domain128.class, "getString2"));
            put("string3", ReflectionUtils.getMethod(Domain128.class, "getString3"));
            put("string4", ReflectionUtils.getMethod(Domain128.class, "getString4"));
            put("string5", ReflectionUtils.getMethod(Domain128.class, "getString5"));
            put("string6", ReflectionUtils.getMethod(Domain128.class, "getString6"));
            put("string7", ReflectionUtils.getMethod(Domain128.class, "getString7"));
            put("string8", ReflectionUtils.getMethod(Domain128.class, "getString8"));
            put("string9", ReflectionUtils.getMethod(Domain128.class, "getString9"));
            put("string10", ReflectionUtils.getMethod(Domain128.class, "getString10"));
            put("string11", ReflectionUtils.getMethod(Domain128.class, "getString11"));
            put("string12", ReflectionUtils.getMethod(Domain128.class, "getString12"));
            put("string13", ReflectionUtils.getMethod(Domain128.class, "getString13"));
            put("string14", ReflectionUtils.getMethod(Domain128.class, "getString14"));
            put("string15", ReflectionUtils.getMethod(Domain128.class, "getString15"));
            put("string16", ReflectionUtils.getMethod(Domain128.class, "getString16"));
            put("string17", ReflectionUtils.getMethod(Domain128.class, "getString17"));
            put("string18", ReflectionUtils.getMethod(Domain128.class, "getString18"));
            put("string19", ReflectionUtils.getMethod(Domain128.class, "getString19"));
            put("string20", ReflectionUtils.getMethod(Domain128.class, "getString20"));
            put("string21", ReflectionUtils.getMethod(Domain128.class, "getString21"));
            put("string22", ReflectionUtils.getMethod(Domain128.class, "getString22"));
            put("string23", ReflectionUtils.getMethod(Domain128.class, "getString23"));
            put("string24", ReflectionUtils.getMethod(Domain128.class, "getString24"));
            put("string25", ReflectionUtils.getMethod(Domain128.class, "getString25"));
            put("string26", ReflectionUtils.getMethod(Domain128.class, "getString26"));
            put("string27", ReflectionUtils.getMethod(Domain128.class, "getString27"));
            put("string28", ReflectionUtils.getMethod(Domain128.class, "getString28"));
            put("string29", ReflectionUtils.getMethod(Domain128.class, "getString29"));
            put("string30", ReflectionUtils.getMethod(Domain128.class, "getString30"));
            put("string31", ReflectionUtils.getMethod(Domain128.class, "getString31"));
        }};
        assert reflectionMap128.size() == 128 : "Reflection map size must be 128, not " + reflectionMap128.size();
        DOMAIN128_REFLECTION_SUPPLIER = () -> reflectionMap128;
        HashMap<String, Function<Domain128, Object>> handlingMap128 = new HashMap<String, Function<Domain128, Object>>(128) {{
            put("int0", HandlingUtils.getMethod(Domain128.class, "getInt0", int.class));
            put("int1", HandlingUtils.getMethod(Domain128.class, "getInt1", int.class));
            put("int2", HandlingUtils.getMethod(Domain128.class, "getInt2", int.class));
            put("int3", HandlingUtils.getMethod(Domain128.class, "getInt3", int.class));
            put("int4", HandlingUtils.getMethod(Domain128.class, "getInt4", int.class));
            put("int5", HandlingUtils.getMethod(Domain128.class, "getInt5", int.class));
            put("int6", HandlingUtils.getMethod(Domain128.class, "getInt6", int.class));
            put("int7", HandlingUtils.getMethod(Domain128.class, "getInt7", int.class));
            put("int8", HandlingUtils.getMethod(Domain128.class, "getInt8", int.class));
            put("int9", HandlingUtils.getMethod(Domain128.class, "getInt9", int.class));
            put("int10", HandlingUtils.getMethod(Domain128.class, "getInt10", int.class));
            put("int11", HandlingUtils.getMethod(Domain128.class, "getInt11", int.class));
            put("int12", HandlingUtils.getMethod(Domain128.class, "getInt12", int.class));
            put("int13", HandlingUtils.getMethod(Domain128.class, "getInt13", int.class));
            put("int14", HandlingUtils.getMethod(Domain128.class, "getInt14", int.class));
            put("int15", HandlingUtils.getMethod(Domain128.class, "getInt15", int.class));
            put("int16", HandlingUtils.getMethod(Domain128.class, "getInt16", int.class));
            put("int17", HandlingUtils.getMethod(Domain128.class, "getInt17", int.class));
            put("int18", HandlingUtils.getMethod(Domain128.class, "getInt18", int.class));
            put("int19", HandlingUtils.getMethod(Domain128.class, "getInt19", int.class));
            put("int20", HandlingUtils.getMethod(Domain128.class, "getInt20", int.class));
            put("int21", HandlingUtils.getMethod(Domain128.class, "getInt21", int.class));
            put("int22", HandlingUtils.getMethod(Domain128.class, "getInt22", int.class));
            put("int23", HandlingUtils.getMethod(Domain128.class, "getInt23", int.class));
            put("int24", HandlingUtils.getMethod(Domain128.class, "getInt24", int.class));
            put("int25", HandlingUtils.getMethod(Domain128.class, "getInt25", int.class));
            put("int26", HandlingUtils.getMethod(Domain128.class, "getInt26", int.class));
            put("int27", HandlingUtils.getMethod(Domain128.class, "getInt27", int.class));
            put("int28", HandlingUtils.getMethod(Domain128.class, "getInt28", int.class));
            put("int29", HandlingUtils.getMethod(Domain128.class, "getInt29", int.class));
            put("int30", HandlingUtils.getMethod(Domain128.class, "getInt30", int.class));
            put("int31", HandlingUtils.getMethod(Domain128.class, "getInt31", int.class));
            put("long0", HandlingUtils.getMethod(Domain128.class, "getLong0", long.class));
            put("long1", HandlingUtils.getMethod(Domain128.class, "getLong1", long.class));
            put("long2", HandlingUtils.getMethod(Domain128.class, "getLong2", long.class));
            put("long3", HandlingUtils.getMethod(Domain128.class, "getLong3", long.class));
            put("long4", HandlingUtils.getMethod(Domain128.class, "getLong4", long.class));
            put("long5", HandlingUtils.getMethod(Domain128.class, "getLong5", long.class));
            put("long6", HandlingUtils.getMethod(Domain128.class, "getLong6", long.class));
            put("long7", HandlingUtils.getMethod(Domain128.class, "getLong7", long.class));
            put("long8", HandlingUtils.getMethod(Domain128.class, "getLong8", long.class));
            put("long9", HandlingUtils.getMethod(Domain128.class, "getLong9", long.class));
            put("long10", HandlingUtils.getMethod(Domain128.class, "getLong10", long.class));
            put("long11", HandlingUtils.getMethod(Domain128.class, "getLong11", long.class));
            put("long12", HandlingUtils.getMethod(Domain128.class, "getLong12", long.class));
            put("long13", HandlingUtils.getMethod(Domain128.class, "getLong13", long.class));
            put("long14", HandlingUtils.getMethod(Domain128.class, "getLong14", long.class));
            put("long15", HandlingUtils.getMethod(Domain128.class, "getLong15", long.class));
            put("long16", HandlingUtils.getMethod(Domain128.class, "getLong16", long.class));
            put("long17", HandlingUtils.getMethod(Domain128.class, "getLong17", long.class));
            put("long18", HandlingUtils.getMethod(Domain128.class, "getLong18", long.class));
            put("long19", HandlingUtils.getMethod(Domain128.class, "getLong19", long.class));
            put("long20", HandlingUtils.getMethod(Domain128.class, "getLong20", long.class));
            put("long21", HandlingUtils.getMethod(Domain128.class, "getLong21", long.class));
            put("long22", HandlingUtils.getMethod(Domain128.class, "getLong22", long.class));
            put("long23", HandlingUtils.getMethod(Domain128.class, "getLong23", long.class));
            put("long24", HandlingUtils.getMethod(Domain128.class, "getLong24", long.class));
            put("long25", HandlingUtils.getMethod(Domain128.class, "getLong25", long.class));
            put("long26", HandlingUtils.getMethod(Domain128.class, "getLong26", long.class));
            put("long27", HandlingUtils.getMethod(Domain128.class, "getLong27", long.class));
            put("long28", HandlingUtils.getMethod(Domain128.class, "getLong28", long.class));
            put("long29", HandlingUtils.getMethod(Domain128.class, "getLong29", long.class));
            put("long30", HandlingUtils.getMethod(Domain128.class, "getLong30", long.class));
            put("long31", HandlingUtils.getMethod(Domain128.class, "getLong31", long.class));
            put("bool0", HandlingUtils.getMethod(Domain128.class, "isBool0", boolean.class));
            put("bool1", HandlingUtils.getMethod(Domain128.class, "isBool1", boolean.class));
            put("bool2", HandlingUtils.getMethod(Domain128.class, "isBool2", boolean.class));
            put("bool3", HandlingUtils.getMethod(Domain128.class, "isBool3", boolean.class));
            put("bool4", HandlingUtils.getMethod(Domain128.class, "isBool4", boolean.class));
            put("bool5", HandlingUtils.getMethod(Domain128.class, "isBool5", boolean.class));
            put("bool6", HandlingUtils.getMethod(Domain128.class, "isBool6", boolean.class));
            put("bool7", HandlingUtils.getMethod(Domain128.class, "isBool7", boolean.class));
            put("bool8", HandlingUtils.getMethod(Domain128.class, "isBool8", boolean.class));
            put("bool9", HandlingUtils.getMethod(Domain128.class, "isBool9", boolean.class));
            put("bool10", HandlingUtils.getMethod(Domain128.class, "isBool10", boolean.class));
            put("bool11", HandlingUtils.getMethod(Domain128.class, "isBool11", boolean.class));
            put("bool12", HandlingUtils.getMethod(Domain128.class, "isBool12", boolean.class));
            put("bool13", HandlingUtils.getMethod(Domain128.class, "isBool13", boolean.class));
            put("bool14", HandlingUtils.getMethod(Domain128.class, "isBool14", boolean.class));
            put("bool15", HandlingUtils.getMethod(Domain128.class, "isBool15", boolean.class));
            put("bool16", HandlingUtils.getMethod(Domain128.class, "isBool16", boolean.class));
            put("bool17", HandlingUtils.getMethod(Domain128.class, "isBool17", boolean.class));
            put("bool18", HandlingUtils.getMethod(Domain128.class, "isBool18", boolean.class));
            put("bool19", HandlingUtils.getMethod(Domain128.class, "isBool19", boolean.class));
            put("bool20", HandlingUtils.getMethod(Domain128.class, "isBool20", boolean.class));
            put("bool21", HandlingUtils.getMethod(Domain128.class, "isBool21", boolean.class));
            put("bool22", HandlingUtils.getMethod(Domain128.class, "isBool22", boolean.class));
            put("bool23", HandlingUtils.getMethod(Domain128.class, "isBool23", boolean.class));
            put("bool24", HandlingUtils.getMethod(Domain128.class, "isBool24", boolean.class));
            put("bool25", HandlingUtils.getMethod(Domain128.class, "isBool25", boolean.class));
            put("bool26", HandlingUtils.getMethod(Domain128.class, "isBool26", boolean.class));
            put("bool27", HandlingUtils.getMethod(Domain128.class, "isBool27", boolean.class));
            put("bool28", HandlingUtils.getMethod(Domain128.class, "isBool28", boolean.class));
            put("bool29", HandlingUtils.getMethod(Domain128.class, "isBool29", boolean.class));
            put("bool30", HandlingUtils.getMethod(Domain128.class, "isBool30", boolean.class));
            put("bool31", HandlingUtils.getMethod(Domain128.class, "isBool31", boolean.class));
            put("string0", HandlingUtils.getMethod(Domain128.class, "getString0", String.class));
            put("string1", HandlingUtils.getMethod(Domain128.class, "getString1", String.class));
            put("string2", HandlingUtils.getMethod(Domain128.class, "getString2", String.class));
            put("string3", HandlingUtils.getMethod(Domain128.class, "getString3", String.class));
            put("string4", HandlingUtils.getMethod(Domain128.class, "getString4", String.class));
            put("string5", HandlingUtils.getMethod(Domain128.class, "getString5", String.class));
            put("string6", HandlingUtils.getMethod(Domain128.class, "getString6", String.class));
            put("string7", HandlingUtils.getMethod(Domain128.class, "getString7", String.class));
            put("string8", HandlingUtils.getMethod(Domain128.class, "getString8", String.class));
            put("string9", HandlingUtils.getMethod(Domain128.class, "getString9", String.class));
            put("string10", HandlingUtils.getMethod(Domain128.class, "getString10", String.class));
            put("string11", HandlingUtils.getMethod(Domain128.class, "getString11", String.class));
            put("string12", HandlingUtils.getMethod(Domain128.class, "getString12", String.class));
            put("string13", HandlingUtils.getMethod(Domain128.class, "getString13", String.class));
            put("string14", HandlingUtils.getMethod(Domain128.class, "getString14", String.class));
            put("string15", HandlingUtils.getMethod(Domain128.class, "getString15", String.class));
            put("string16", HandlingUtils.getMethod(Domain128.class, "getString16", String.class));
            put("string17", HandlingUtils.getMethod(Domain128.class, "getString17", String.class));
            put("string18", HandlingUtils.getMethod(Domain128.class, "getString18", String.class));
            put("string19", HandlingUtils.getMethod(Domain128.class, "getString19", String.class));
            put("string20", HandlingUtils.getMethod(Domain128.class, "getString20", String.class));
            put("string21", HandlingUtils.getMethod(Domain128.class, "getString21", String.class));
            put("string22", HandlingUtils.getMethod(Domain128.class, "getString22", String.class));
            put("string23", HandlingUtils.getMethod(Domain128.class, "getString23", String.class));
            put("string24", HandlingUtils.getMethod(Domain128.class, "getString24", String.class));
            put("string25", HandlingUtils.getMethod(Domain128.class, "getString25", String.class));
            put("string26", HandlingUtils.getMethod(Domain128.class, "getString26", String.class));
            put("string27", HandlingUtils.getMethod(Domain128.class, "getString27", String.class));
            put("string28", HandlingUtils.getMethod(Domain128.class, "getString28", String.class));
            put("string29", HandlingUtils.getMethod(Domain128.class, "getString29", String.class));
            put("string30", HandlingUtils.getMethod(Domain128.class, "getString30", String.class));
            put("string31", HandlingUtils.getMethod(Domain128.class, "getString31", String.class));
        }};
        assert handlingMap128.size() == 128 : "Handling map size must be 128, not " + handlingMap128.size();
        DOMAIN128_HANDLING_SUPPLIER = () -> handlingMap128;
        CustomMatrixGettersMap<Domain128> customMap128 = new CustomMatrixGettersMap<>(map128);
        DOMAIN128_CUSTOM_MAP_SUPPLIER = () -> customMap128;
        Domain128GetterSupplier getterSupplier128 = new Domain128GetterSupplier();
        assert getterSupplier128.get().size() == 128 : "Getter supplier map size must be 128, not " + getterSupplier128.get().size();
        DOMAIN128_GENERATION_SUPPLIER = getterSupplier128;
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
