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

package dev.alexengrig.metter.benchmark;

import dev.alexengrig.metter.benchmark.domain.Domain10;
import dev.alexengrig.metter.benchmark.domain.Domain100;
import dev.alexengrig.metter.benchmark.domain.Domain100GetterSupplier;
import dev.alexengrig.metter.benchmark.domain.Domain10GetterSupplier;
import dev.alexengrig.metter.benchmark.domain.Domain50;
import dev.alexengrig.metter.benchmark.domain.Domain500;
import dev.alexengrig.metter.benchmark.domain.Domain500GetterSupplier;
import dev.alexengrig.metter.benchmark.domain.Domain50GetterSupplier;
import dev.alexengrig.metter.benchmark.util.ReflectionSupplier;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@SuppressWarnings("DuplicatedCode")
public class GetterSupplierBenchmark {
    static final Map<String, Function<Domain10, Object>> REFLECTION_GETTERS_10;
    static final Map<String, Function<Domain10, Object>> GENERATION_GETTERS_10;
    static final Map<String, Function<Domain50, Object>> REFLECTION_GETTERS_50;
    static final Map<String, Function<Domain50, Object>> GENERATION_GETTERS_50;
    static final Map<String, Function<Domain100, Object>> REFLECTION_GETTERS_100;
    static final Map<String, Function<Domain100, Object>> GENERATION_GETTERS_100;
    static final Map<String, Function<Domain500, Object>> REFLECTION_GETTERS_500;
    static final Map<String, Function<Domain500, Object>> GENERATION_GETTERS_500;

    static {
        REFLECTION_GETTERS_10 = requireSize(10, ReflectionSupplier.createGetterMapSupplier(Domain10.class).get());
        GENERATION_GETTERS_10 = requireSize(10, new Domain10GetterSupplier().get());
        REFLECTION_GETTERS_50 = requireSize(50, ReflectionSupplier.createGetterMapSupplier(Domain50.class).get());
        GENERATION_GETTERS_50 = requireSize(50, new Domain50GetterSupplier().get());
        REFLECTION_GETTERS_100 = requireSize(100, ReflectionSupplier.createGetterMapSupplier(Domain100.class).get());
        GENERATION_GETTERS_100 = requireSize(100, new Domain100GetterSupplier().get());
        REFLECTION_GETTERS_500 = requireSize(500, ReflectionSupplier.createGetterMapSupplier(Domain500.class).get());
        GENERATION_GETTERS_500 = requireSize(500, new Domain500GetterSupplier().get());
    }

    static <K, V> Map<K, V> requireSize(int size, Map<K, V> map) {
        if (map.size() != size) throw new IllegalArgumentException("Size must be " + size);
        return map;
    }

    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder().include(GetterSupplierBenchmark.class.getSimpleName()).build()).run();
    }

    @Benchmark
    public Domain10 domain10ByReflection() {
        Domain10 domain = new Domain10();
        for (String key : REFLECTION_GETTERS_10.keySet()) {
            Function<Domain10, Object> getter = REFLECTION_GETTERS_10.get(key);
            Object ignore = getter.apply(domain);
        }
        return domain;
    }

    @Benchmark
    public Domain50 domain50ByReflection() {
        Domain50 domain = new Domain50();
        for (String key : REFLECTION_GETTERS_50.keySet()) {
            Function<Domain50, Object> getter = REFLECTION_GETTERS_50.get(key);
            Object ignore = getter.apply(domain);
        }
        return domain;
    }

    @Benchmark
    public Domain100 domain100ByReflection() {
        Domain100 domain = new Domain100();
        for (String key : REFLECTION_GETTERS_100.keySet()) {
            Function<Domain100, Object> getter = REFLECTION_GETTERS_100.get(key);
            Object ignore = getter.apply(domain);
        }
        return domain;
    }

    @Benchmark
    public Domain500 domain500ByReflection() {
        Domain500 domain = new Domain500();
        for (String key : REFLECTION_GETTERS_500.keySet()) {
            Function<Domain500, Object> getter = REFLECTION_GETTERS_500.get(key);
            Object ignore = getter.apply(domain);
        }
        return domain;
    }

    @Benchmark
    public Domain10 domain10ByGeneration() {
        Domain10 domain = new Domain10();
        for (String key : GENERATION_GETTERS_10.keySet()) {
            Function<Domain10, Object> getter = GENERATION_GETTERS_10.get(key);
            Object ignore = getter.apply(domain);
        }
        return domain;
    }

    @Benchmark
    public Domain50 domain50ByGeneration() {
        Domain50 domain = new Domain50();
        for (String key : GENERATION_GETTERS_50.keySet()) {
            Function<Domain50, Object> getter = GENERATION_GETTERS_50.get(key);
            Object ignore = getter.apply(domain);
        }
        return domain;
    }

    @Benchmark
    public Domain100 domain100ByGeneration() {
        Domain100 domain = new Domain100();
        for (String key : GENERATION_GETTERS_100.keySet()) {
            Function<Domain100, Object> getter = GENERATION_GETTERS_100.get(key);
            Object ignore = getter.apply(domain);
        }
        return domain;
    }

    @Benchmark
    public Domain500 domain500ByGeneration() {
        Domain500 domain = new Domain500();
        for (String key : GENERATION_GETTERS_500.keySet()) {
            Function<Domain500, Object> getter = GENERATION_GETTERS_500.get(key);
            Object ignore = getter.apply(domain);
        }
        return domain;
    }

    @Benchmark
    public Domain10 domain10ByManually() {
        Domain10 domain = new Domain10();
        Object int0 = domain.getInt0();
        Object int1 = domain.getInt1();
        Object int2 = domain.getInt2();
        Object int3 = domain.getInt3();
        Object int4 = domain.getInt4();
        Object string5 = domain.getString5();
        Object string6 = domain.getString6();
        Object string7 = domain.getString7();
        Object string8 = domain.getString8();
        Object string9 = domain.getString9();
        return domain;
    }

    @Benchmark
    public Domain50 domain50ByManually() {
        Domain50 domain = new Domain50();
        Object int0 = domain.getInt0();
        Object int1 = domain.getInt1();
        Object int2 = domain.getInt2();
        Object int3 = domain.getInt3();
        Object int4 = domain.getInt4();
        Object int5 = domain.getInt5();
        Object int6 = domain.getInt6();
        Object int7 = domain.getInt7();
        Object int8 = domain.getInt8();
        Object int9 = domain.getInt9();
        Object int10 = domain.getInt10();
        Object int11 = domain.getInt11();
        Object int12 = domain.getInt12();
        Object int13 = domain.getInt13();
        Object int14 = domain.getInt14();
        Object int15 = domain.getInt15();
        Object int16 = domain.getInt16();
        Object int17 = domain.getInt17();
        Object int18 = domain.getInt18();
        Object int19 = domain.getInt19();
        Object int20 = domain.getInt20();
        Object int21 = domain.getInt21();
        Object int22 = domain.getInt22();
        Object int23 = domain.getInt23();
        Object int24 = domain.getInt24();
        Object string25 = domain.getString25();
        Object string26 = domain.getString26();
        Object string27 = domain.getString27();
        Object string28 = domain.getString28();
        Object string29 = domain.getString29();
        Object string30 = domain.getString30();
        Object string31 = domain.getString31();
        Object string32 = domain.getString32();
        Object string33 = domain.getString33();
        Object string34 = domain.getString34();
        Object string35 = domain.getString35();
        Object string36 = domain.getString36();
        Object string37 = domain.getString37();
        Object string38 = domain.getString38();
        Object string39 = domain.getString39();
        Object string40 = domain.getString40();
        Object string41 = domain.getString41();
        Object string42 = domain.getString42();
        Object string43 = domain.getString43();
        Object string44 = domain.getString44();
        Object string45 = domain.getString45();
        Object string46 = domain.getString46();
        Object string47 = domain.getString47();
        Object string48 = domain.getString48();
        Object string49 = domain.getString49();
        return domain;
    }

    @Benchmark
    public Domain100 domain100ByManually() {
        Domain100 domain = new Domain100();
        Object int0 = domain.getInt0();
        Object int1 = domain.getInt1();
        Object int2 = domain.getInt2();
        Object int3 = domain.getInt3();
        Object int4 = domain.getInt4();
        Object int5 = domain.getInt5();
        Object int6 = domain.getInt6();
        Object int7 = domain.getInt7();
        Object int8 = domain.getInt8();
        Object int9 = domain.getInt9();
        Object int10 = domain.getInt10();
        Object int11 = domain.getInt11();
        Object int12 = domain.getInt12();
        Object int13 = domain.getInt13();
        Object int14 = domain.getInt14();
        Object int15 = domain.getInt15();
        Object int16 = domain.getInt16();
        Object int17 = domain.getInt17();
        Object int18 = domain.getInt18();
        Object int19 = domain.getInt19();
        Object int20 = domain.getInt20();
        Object int21 = domain.getInt21();
        Object int22 = domain.getInt22();
        Object int23 = domain.getInt23();
        Object int24 = domain.getInt24();
        Object int25 = domain.getInt25();
        Object int26 = domain.getInt26();
        Object int27 = domain.getInt27();
        Object int28 = domain.getInt28();
        Object int29 = domain.getInt29();
        Object int30 = domain.getInt30();
        Object int31 = domain.getInt31();
        Object int32 = domain.getInt32();
        Object int33 = domain.getInt33();
        Object int34 = domain.getInt34();
        Object int35 = domain.getInt35();
        Object int36 = domain.getInt36();
        Object int37 = domain.getInt37();
        Object int38 = domain.getInt38();
        Object int39 = domain.getInt39();
        Object int40 = domain.getInt40();
        Object int41 = domain.getInt41();
        Object int42 = domain.getInt42();
        Object int43 = domain.getInt43();
        Object int44 = domain.getInt44();
        Object int45 = domain.getInt45();
        Object int46 = domain.getInt46();
        Object int47 = domain.getInt47();
        Object int48 = domain.getInt48();
        Object int49 = domain.getInt49();
        Object string50 = domain.getString50();
        Object string51 = domain.getString51();
        Object string52 = domain.getString52();
        Object string53 = domain.getString53();
        Object string54 = domain.getString54();
        Object string55 = domain.getString55();
        Object string56 = domain.getString56();
        Object string57 = domain.getString57();
        Object string58 = domain.getString58();
        Object string59 = domain.getString59();
        Object string60 = domain.getString60();
        Object string61 = domain.getString61();
        Object string62 = domain.getString62();
        Object string63 = domain.getString63();
        Object string64 = domain.getString64();
        Object string65 = domain.getString65();
        Object string66 = domain.getString66();
        Object string67 = domain.getString67();
        Object string68 = domain.getString68();
        Object string69 = domain.getString69();
        Object string70 = domain.getString70();
        Object string71 = domain.getString71();
        Object string72 = domain.getString72();
        Object string73 = domain.getString73();
        Object string74 = domain.getString74();
        Object string75 = domain.getString75();
        Object string76 = domain.getString76();
        Object string77 = domain.getString77();
        Object string78 = domain.getString78();
        Object string79 = domain.getString79();
        Object string80 = domain.getString80();
        Object string81 = domain.getString81();
        Object string82 = domain.getString82();
        Object string83 = domain.getString83();
        Object string84 = domain.getString84();
        Object string85 = domain.getString85();
        Object string86 = domain.getString86();
        Object string87 = domain.getString87();
        Object string88 = domain.getString88();
        Object string89 = domain.getString89();
        Object string90 = domain.getString90();
        Object string91 = domain.getString91();
        Object string92 = domain.getString92();
        Object string93 = domain.getString93();
        Object string94 = domain.getString94();
        Object string95 = domain.getString95();
        Object string96 = domain.getString96();
        Object string97 = domain.getString97();
        Object string98 = domain.getString98();
        Object string99 = domain.getString99();
        return domain;
    }

    @Benchmark
    public Domain500 domain500ByManually() {
        Domain500 domain = new Domain500();
        Object int0 = domain.getInt0();
        Object int1 = domain.getInt1();
        Object int2 = domain.getInt2();
        Object int3 = domain.getInt3();
        Object int4 = domain.getInt4();
        Object int5 = domain.getInt5();
        Object int6 = domain.getInt6();
        Object int7 = domain.getInt7();
        Object int8 = domain.getInt8();
        Object int9 = domain.getInt9();
        Object int10 = domain.getInt10();
        Object int11 = domain.getInt11();
        Object int12 = domain.getInt12();
        Object int13 = domain.getInt13();
        Object int14 = domain.getInt14();
        Object int15 = domain.getInt15();
        Object int16 = domain.getInt16();
        Object int17 = domain.getInt17();
        Object int18 = domain.getInt18();
        Object int19 = domain.getInt19();
        Object int20 = domain.getInt20();
        Object int21 = domain.getInt21();
        Object int22 = domain.getInt22();
        Object int23 = domain.getInt23();
        Object int24 = domain.getInt24();
        Object int25 = domain.getInt25();
        Object int26 = domain.getInt26();
        Object int27 = domain.getInt27();
        Object int28 = domain.getInt28();
        Object int29 = domain.getInt29();
        Object int30 = domain.getInt30();
        Object int31 = domain.getInt31();
        Object int32 = domain.getInt32();
        Object int33 = domain.getInt33();
        Object int34 = domain.getInt34();
        Object int35 = domain.getInt35();
        Object int36 = domain.getInt36();
        Object int37 = domain.getInt37();
        Object int38 = domain.getInt38();
        Object int39 = domain.getInt39();
        Object int40 = domain.getInt40();
        Object int41 = domain.getInt41();
        Object int42 = domain.getInt42();
        Object int43 = domain.getInt43();
        Object int44 = domain.getInt44();
        Object int45 = domain.getInt45();
        Object int46 = domain.getInt46();
        Object int47 = domain.getInt47();
        Object int48 = domain.getInt48();
        Object int49 = domain.getInt49();
        Object int50 = domain.getInt50();
        Object int51 = domain.getInt51();
        Object int52 = domain.getInt52();
        Object int53 = domain.getInt53();
        Object int54 = domain.getInt54();
        Object int55 = domain.getInt55();
        Object int56 = domain.getInt56();
        Object int57 = domain.getInt57();
        Object int58 = domain.getInt58();
        Object int59 = domain.getInt59();
        Object int60 = domain.getInt60();
        Object int61 = domain.getInt61();
        Object int62 = domain.getInt62();
        Object int63 = domain.getInt63();
        Object int64 = domain.getInt64();
        Object int65 = domain.getInt65();
        Object int66 = domain.getInt66();
        Object int67 = domain.getInt67();
        Object int68 = domain.getInt68();
        Object int69 = domain.getInt69();
        Object int70 = domain.getInt70();
        Object int71 = domain.getInt71();
        Object int72 = domain.getInt72();
        Object int73 = domain.getInt73();
        Object int74 = domain.getInt74();
        Object int75 = domain.getInt75();
        Object int76 = domain.getInt76();
        Object int77 = domain.getInt77();
        Object int78 = domain.getInt78();
        Object int79 = domain.getInt79();
        Object int80 = domain.getInt80();
        Object int81 = domain.getInt81();
        Object int82 = domain.getInt82();
        Object int83 = domain.getInt83();
        Object int84 = domain.getInt84();
        Object int85 = domain.getInt85();
        Object int86 = domain.getInt86();
        Object int87 = domain.getInt87();
        Object int88 = domain.getInt88();
        Object int89 = domain.getInt89();
        Object int90 = domain.getInt90();
        Object int91 = domain.getInt91();
        Object int92 = domain.getInt92();
        Object int93 = domain.getInt93();
        Object int94 = domain.getInt94();
        Object int95 = domain.getInt95();
        Object int96 = domain.getInt96();
        Object int97 = domain.getInt97();
        Object int98 = domain.getInt98();
        Object int99 = domain.getInt99();
        Object int100 = domain.getInt100();
        Object int101 = domain.getInt101();
        Object int102 = domain.getInt102();
        Object int103 = domain.getInt103();
        Object int104 = domain.getInt104();
        Object int105 = domain.getInt105();
        Object int106 = domain.getInt106();
        Object int107 = domain.getInt107();
        Object int108 = domain.getInt108();
        Object int109 = domain.getInt109();
        Object int110 = domain.getInt110();
        Object int111 = domain.getInt111();
        Object int112 = domain.getInt112();
        Object int113 = domain.getInt113();
        Object int114 = domain.getInt114();
        Object int115 = domain.getInt115();
        Object int116 = domain.getInt116();
        Object int117 = domain.getInt117();
        Object int118 = domain.getInt118();
        Object int119 = domain.getInt119();
        Object int120 = domain.getInt120();
        Object int121 = domain.getInt121();
        Object int122 = domain.getInt122();
        Object int123 = domain.getInt123();
        Object int124 = domain.getInt124();
        Object int125 = domain.getInt125();
        Object int126 = domain.getInt126();
        Object int127 = domain.getInt127();
        Object int128 = domain.getInt128();
        Object int129 = domain.getInt129();
        Object int130 = domain.getInt130();
        Object int131 = domain.getInt131();
        Object int132 = domain.getInt132();
        Object int133 = domain.getInt133();
        Object int134 = domain.getInt134();
        Object int135 = domain.getInt135();
        Object int136 = domain.getInt136();
        Object int137 = domain.getInt137();
        Object int138 = domain.getInt138();
        Object int139 = domain.getInt139();
        Object int140 = domain.getInt140();
        Object int141 = domain.getInt141();
        Object int142 = domain.getInt142();
        Object int143 = domain.getInt143();
        Object int144 = domain.getInt144();
        Object int145 = domain.getInt145();
        Object int146 = domain.getInt146();
        Object int147 = domain.getInt147();
        Object int148 = domain.getInt148();
        Object int149 = domain.getInt149();
        Object int150 = domain.getInt150();
        Object int151 = domain.getInt151();
        Object int152 = domain.getInt152();
        Object int153 = domain.getInt153();
        Object int154 = domain.getInt154();
        Object int155 = domain.getInt155();
        Object int156 = domain.getInt156();
        Object int157 = domain.getInt157();
        Object int158 = domain.getInt158();
        Object int159 = domain.getInt159();
        Object int160 = domain.getInt160();
        Object int161 = domain.getInt161();
        Object int162 = domain.getInt162();
        Object int163 = domain.getInt163();
        Object int164 = domain.getInt164();
        Object int165 = domain.getInt165();
        Object int166 = domain.getInt166();
        Object int167 = domain.getInt167();
        Object int168 = domain.getInt168();
        Object int169 = domain.getInt169();
        Object int170 = domain.getInt170();
        Object int171 = domain.getInt171();
        Object int172 = domain.getInt172();
        Object int173 = domain.getInt173();
        Object int174 = domain.getInt174();
        Object int175 = domain.getInt175();
        Object int176 = domain.getInt176();
        Object int177 = domain.getInt177();
        Object int178 = domain.getInt178();
        Object int179 = domain.getInt179();
        Object int180 = domain.getInt180();
        Object int181 = domain.getInt181();
        Object int182 = domain.getInt182();
        Object int183 = domain.getInt183();
        Object int184 = domain.getInt184();
        Object int185 = domain.getInt185();
        Object int186 = domain.getInt186();
        Object int187 = domain.getInt187();
        Object int188 = domain.getInt188();
        Object int189 = domain.getInt189();
        Object int190 = domain.getInt190();
        Object int191 = domain.getInt191();
        Object int192 = domain.getInt192();
        Object int193 = domain.getInt193();
        Object int194 = domain.getInt194();
        Object int195 = domain.getInt195();
        Object int196 = domain.getInt196();
        Object int197 = domain.getInt197();
        Object int198 = domain.getInt198();
        Object int199 = domain.getInt199();
        Object int200 = domain.getInt200();
        Object int201 = domain.getInt201();
        Object int202 = domain.getInt202();
        Object int203 = domain.getInt203();
        Object int204 = domain.getInt204();
        Object int205 = domain.getInt205();
        Object int206 = domain.getInt206();
        Object int207 = domain.getInt207();
        Object int208 = domain.getInt208();
        Object int209 = domain.getInt209();
        Object int210 = domain.getInt210();
        Object int211 = domain.getInt211();
        Object int212 = domain.getInt212();
        Object int213 = domain.getInt213();
        Object int214 = domain.getInt214();
        Object int215 = domain.getInt215();
        Object int216 = domain.getInt216();
        Object int217 = domain.getInt217();
        Object int218 = domain.getInt218();
        Object int219 = domain.getInt219();
        Object int220 = domain.getInt220();
        Object int221 = domain.getInt221();
        Object int222 = domain.getInt222();
        Object int223 = domain.getInt223();
        Object int224 = domain.getInt224();
        Object int225 = domain.getInt225();
        Object int226 = domain.getInt226();
        Object int227 = domain.getInt227();
        Object int228 = domain.getInt228();
        Object int229 = domain.getInt229();
        Object int230 = domain.getInt230();
        Object int231 = domain.getInt231();
        Object int232 = domain.getInt232();
        Object int233 = domain.getInt233();
        Object int234 = domain.getInt234();
        Object int235 = domain.getInt235();
        Object int236 = domain.getInt236();
        Object int237 = domain.getInt237();
        Object int238 = domain.getInt238();
        Object int239 = domain.getInt239();
        Object int240 = domain.getInt240();
        Object int241 = domain.getInt241();
        Object int242 = domain.getInt242();
        Object int243 = domain.getInt243();
        Object int244 = domain.getInt244();
        Object int245 = domain.getInt245();
        Object int246 = domain.getInt246();
        Object int247 = domain.getInt247();
        Object int248 = domain.getInt248();
        Object int249 = domain.getInt249();
        Object string250 = domain.getString250();
        Object string251 = domain.getString251();
        Object string252 = domain.getString252();
        Object string253 = domain.getString253();
        Object string254 = domain.getString254();
        Object string255 = domain.getString255();
        Object string256 = domain.getString256();
        Object string257 = domain.getString257();
        Object string258 = domain.getString258();
        Object string259 = domain.getString259();
        Object string260 = domain.getString260();
        Object string261 = domain.getString261();
        Object string262 = domain.getString262();
        Object string263 = domain.getString263();
        Object string264 = domain.getString264();
        Object string265 = domain.getString265();
        Object string266 = domain.getString266();
        Object string267 = domain.getString267();
        Object string268 = domain.getString268();
        Object string269 = domain.getString269();
        Object string270 = domain.getString270();
        Object string271 = domain.getString271();
        Object string272 = domain.getString272();
        Object string273 = domain.getString273();
        Object string274 = domain.getString274();
        Object string275 = domain.getString275();
        Object string276 = domain.getString276();
        Object string277 = domain.getString277();
        Object string278 = domain.getString278();
        Object string279 = domain.getString279();
        Object string280 = domain.getString280();
        Object string281 = domain.getString281();
        Object string282 = domain.getString282();
        Object string283 = domain.getString283();
        Object string284 = domain.getString284();
        Object string285 = domain.getString285();
        Object string286 = domain.getString286();
        Object string287 = domain.getString287();
        Object string288 = domain.getString288();
        Object string289 = domain.getString289();
        Object string290 = domain.getString290();
        Object string291 = domain.getString291();
        Object string292 = domain.getString292();
        Object string293 = domain.getString293();
        Object string294 = domain.getString294();
        Object string295 = domain.getString295();
        Object string296 = domain.getString296();
        Object string297 = domain.getString297();
        Object string298 = domain.getString298();
        Object string299 = domain.getString299();
        Object string300 = domain.getString300();
        Object string301 = domain.getString301();
        Object string302 = domain.getString302();
        Object string303 = domain.getString303();
        Object string304 = domain.getString304();
        Object string305 = domain.getString305();
        Object string306 = domain.getString306();
        Object string307 = domain.getString307();
        Object string308 = domain.getString308();
        Object string309 = domain.getString309();
        Object string310 = domain.getString310();
        Object string311 = domain.getString311();
        Object string312 = domain.getString312();
        Object string313 = domain.getString313();
        Object string314 = domain.getString314();
        Object string315 = domain.getString315();
        Object string316 = domain.getString316();
        Object string317 = domain.getString317();
        Object string318 = domain.getString318();
        Object string319 = domain.getString319();
        Object string320 = domain.getString320();
        Object string321 = domain.getString321();
        Object string322 = domain.getString322();
        Object string323 = domain.getString323();
        Object string324 = domain.getString324();
        Object string325 = domain.getString325();
        Object string326 = domain.getString326();
        Object string327 = domain.getString327();
        Object string328 = domain.getString328();
        Object string329 = domain.getString329();
        Object string330 = domain.getString330();
        Object string331 = domain.getString331();
        Object string332 = domain.getString332();
        Object string333 = domain.getString333();
        Object string334 = domain.getString334();
        Object string335 = domain.getString335();
        Object string336 = domain.getString336();
        Object string337 = domain.getString337();
        Object string338 = domain.getString338();
        Object string339 = domain.getString339();
        Object string340 = domain.getString340();
        Object string341 = domain.getString341();
        Object string342 = domain.getString342();
        Object string343 = domain.getString343();
        Object string344 = domain.getString344();
        Object string345 = domain.getString345();
        Object string346 = domain.getString346();
        Object string347 = domain.getString347();
        Object string348 = domain.getString348();
        Object string349 = domain.getString349();
        Object string350 = domain.getString350();
        Object string351 = domain.getString351();
        Object string352 = domain.getString352();
        Object string353 = domain.getString353();
        Object string354 = domain.getString354();
        Object string355 = domain.getString355();
        Object string356 = domain.getString356();
        Object string357 = domain.getString357();
        Object string358 = domain.getString358();
        Object string359 = domain.getString359();
        Object string360 = domain.getString360();
        Object string361 = domain.getString361();
        Object string362 = domain.getString362();
        Object string363 = domain.getString363();
        Object string364 = domain.getString364();
        Object string365 = domain.getString365();
        Object string366 = domain.getString366();
        Object string367 = domain.getString367();
        Object string368 = domain.getString368();
        Object string369 = domain.getString369();
        Object string370 = domain.getString370();
        Object string371 = domain.getString371();
        Object string372 = domain.getString372();
        Object string373 = domain.getString373();
        Object string374 = domain.getString374();
        Object string375 = domain.getString375();
        Object string376 = domain.getString376();
        Object string377 = domain.getString377();
        Object string378 = domain.getString378();
        Object string379 = domain.getString379();
        Object string380 = domain.getString380();
        Object string381 = domain.getString381();
        Object string382 = domain.getString382();
        Object string383 = domain.getString383();
        Object string384 = domain.getString384();
        Object string385 = domain.getString385();
        Object string386 = domain.getString386();
        Object string387 = domain.getString387();
        Object string388 = domain.getString388();
        Object string389 = domain.getString389();
        Object string390 = domain.getString390();
        Object string391 = domain.getString391();
        Object string392 = domain.getString392();
        Object string393 = domain.getString393();
        Object string394 = domain.getString394();
        Object string395 = domain.getString395();
        Object string396 = domain.getString396();
        Object string397 = domain.getString397();
        Object string398 = domain.getString398();
        Object string399 = domain.getString399();
        Object string400 = domain.getString400();
        Object string401 = domain.getString401();
        Object string402 = domain.getString402();
        Object string403 = domain.getString403();
        Object string404 = domain.getString404();
        Object string405 = domain.getString405();
        Object string406 = domain.getString406();
        Object string407 = domain.getString407();
        Object string408 = domain.getString408();
        Object string409 = domain.getString409();
        Object string410 = domain.getString410();
        Object string411 = domain.getString411();
        Object string412 = domain.getString412();
        Object string413 = domain.getString413();
        Object string414 = domain.getString414();
        Object string415 = domain.getString415();
        Object string416 = domain.getString416();
        Object string417 = domain.getString417();
        Object string418 = domain.getString418();
        Object string419 = domain.getString419();
        Object string420 = domain.getString420();
        Object string421 = domain.getString421();
        Object string422 = domain.getString422();
        Object string423 = domain.getString423();
        Object string424 = domain.getString424();
        Object string425 = domain.getString425();
        Object string426 = domain.getString426();
        Object string427 = domain.getString427();
        Object string428 = domain.getString428();
        Object string429 = domain.getString429();
        Object string430 = domain.getString430();
        Object string431 = domain.getString431();
        Object string432 = domain.getString432();
        Object string433 = domain.getString433();
        Object string434 = domain.getString434();
        Object string435 = domain.getString435();
        Object string436 = domain.getString436();
        Object string437 = domain.getString437();
        Object string438 = domain.getString438();
        Object string439 = domain.getString439();
        Object string440 = domain.getString440();
        Object string441 = domain.getString441();
        Object string442 = domain.getString442();
        Object string443 = domain.getString443();
        Object string444 = domain.getString444();
        Object string445 = domain.getString445();
        Object string446 = domain.getString446();
        Object string447 = domain.getString447();
        Object string448 = domain.getString448();
        Object string449 = domain.getString449();
        Object string450 = domain.getString450();
        Object string451 = domain.getString451();
        Object string452 = domain.getString452();
        Object string453 = domain.getString453();
        Object string454 = domain.getString454();
        Object string455 = domain.getString455();
        Object string456 = domain.getString456();
        Object string457 = domain.getString457();
        Object string458 = domain.getString458();
        Object string459 = domain.getString459();
        Object string460 = domain.getString460();
        Object string461 = domain.getString461();
        Object string462 = domain.getString462();
        Object string463 = domain.getString463();
        Object string464 = domain.getString464();
        Object string465 = domain.getString465();
        Object string466 = domain.getString466();
        Object string467 = domain.getString467();
        Object string468 = domain.getString468();
        Object string469 = domain.getString469();
        Object string470 = domain.getString470();
        Object string471 = domain.getString471();
        Object string472 = domain.getString472();
        Object string473 = domain.getString473();
        Object string474 = domain.getString474();
        Object string475 = domain.getString475();
        Object string476 = domain.getString476();
        Object string477 = domain.getString477();
        Object string478 = domain.getString478();
        Object string479 = domain.getString479();
        Object string480 = domain.getString480();
        Object string481 = domain.getString481();
        Object string482 = domain.getString482();
        Object string483 = domain.getString483();
        Object string484 = domain.getString484();
        Object string485 = domain.getString485();
        Object string486 = domain.getString486();
        Object string487 = domain.getString487();
        Object string488 = domain.getString488();
        Object string489 = domain.getString489();
        Object string490 = domain.getString490();
        Object string491 = domain.getString491();
        Object string492 = domain.getString492();
        Object string493 = domain.getString493();
        Object string494 = domain.getString494();
        Object string495 = domain.getString495();
        Object string496 = domain.getString496();
        Object string497 = domain.getString497();
        Object string498 = domain.getString498();
        Object string499 = domain.getString499();
        return domain;
    }
}
