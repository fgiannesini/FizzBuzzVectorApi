package com.fgiannesini;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.random.RandomGenerator;

public class Bench {

    private int[] data() {
        return RandomGenerator.getDefault().ints(8_000_000, 0, Integer.MAX_VALUE).toArray();
    }

    @Benchmark
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    @Fork(value = 1)
    public void should_measure_fizz_buzz_scalar(Blackhole bh) {
        FizzBuzz fizzBuzz = new FizzBuzz();
        bh.consume(fizzBuzz.scalarFizzBuzz(data()));
    }

    @Benchmark
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    @Fork(value = 1)
    public void should_measure_fizz_buzz_parallel_scalar(Blackhole bh) {
        FizzBuzz fizzBuzz = new FizzBuzz();
        bh.consume(fizzBuzz.scalarParallelFizzBuzz(data()));
    }

    @Benchmark
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    @Fork(value = 1)
    public void should_measure_fizz_buzz_simd(Blackhole bh) {
        FizzBuzz fizzBuzz = new FizzBuzz();
        bh.consume(fizzBuzz.simdFizzBuzz(data()));
    }
}
