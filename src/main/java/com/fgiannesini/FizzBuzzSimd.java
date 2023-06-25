package com.fgiannesini;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FizzBuzzSimd {

    private static final int FIZZ = -1;
    private static final int BUZZ = -2;
    private static final int FIZZ_BUZZ = -3;

    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    public int[] simdSequentialFizzBuzz(int[] inputs) {
        var containerStream = intStream(inputs)
                .mapToObj(offset -> {
                    IntVector intVector1 = IntVector.fromArray(SPECIES, inputs, offset);
                    IntVector intVector = fizzBuzz(intVector1);
                    return new Container(offset, intVector);
                });
        return toArray(containerStream, inputs.length);
    }

    public int[] simdParallelFizzBuzz(int[] inputs) {
        var containerStream = intStream(inputs)
                .parallel()
                .mapToObj(offset -> new Container(offset, IntVector.fromArray(SPECIES, inputs, offset)))
                .map(container -> new Container(container.offset(), fizzBuzz(container.intVector())));
        return toArray(containerStream, inputs.length);
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

    private int[] toArray(Stream<Container> containerStream, int length) {
        return containerStream.collect(() -> new int[length],
                (tab, container) -> container.intVector().intoArray(tab, container.offset()),
                (tab1, tab2) -> {
                    int index = indexOfFirstZero(tab1);
                    System.arraycopy(tab2, index, tab1, index, tab1.length - index);
                });
    }

    private int indexOfFirstZero(int[] tab) {
        for (int i = tab.length - 1; i >= 0; i--) {
            if (tab[i] != 0) {
                return i + 1;
            }
        }
        return 0;
    }

    private IntStream intStream(int[] inputs) {
        return IntStream.iterate(0, n -> n < SPECIES.loopBound(inputs.length), n -> n + SPECIES.length());
    }
}
