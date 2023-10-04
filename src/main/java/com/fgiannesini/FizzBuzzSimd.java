package com.fgiannesini;

import jdk.incubator.vector.*;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FizzBuzzSimd {

    private static final int FIZZ = -1;
    private static final int BUZZ = -2;
    private static final int FIZZ_BUZZ = -3;

    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    public int[] simdSequentialFizzBuzz(int[] inputs) {
        var containerStream = intStream(inputs)
                .mapToObj(offset -> new Container(offset, IntVector.fromArray(SPECIES, inputs, offset)))
                .map(container -> new Container(container.offset(), fizzBuzz(container.intVector())));
        return toArray(containerStream);
    }

    public int[] simdParallelFizzBuzz(int[] inputs) {
        var containerStream = intStream(inputs)
                .parallel()
                .mapToObj(offset -> new Container(offset, IntVector.fromArray(SPECIES, inputs, offset)))
                .map(container -> new Container(container.offset(), fizzBuzz(container.intVector())));
        return toArray(containerStream);
    }

    private IntVector fizzBuzz(IntVector inputPart) {
        VectorMask<Integer> threeMask = moduloMask(inputPart, 3);
        VectorMask<Integer> fiveMask = moduloMask(inputPart, 5);
        return inputPart
                .blend(FIZZ, threeMask)
                .blend(BUZZ, fiveMask)
                .blend(FIZZ_BUZZ, threeMask.and(fiveMask));
    }

    private VectorMask<Integer> moduloMask(IntVector inputVector, int factor) {
        return inputVector.div(factor).mul(factor).eq(inputVector);
    }

    private int[] toArray(Stream<Container> containerStream) {
        return containerStream.map(container -> container.intVector().toArray())
                .flatMapToInt(IntStream::of)
                .toArray();
    }

    private IntStream intStream(int[] inputs) {
        return IntStream.iterate(0, n -> n < SPECIES.loopBound(inputs.length), n -> n + SPECIES.length());
    }
}
