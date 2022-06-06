import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;

public class FizzBuzz {

    private static final int FIZZ = -1;
    private static final int BUZZ = -2;
    private static final int FIZZ_BUZZ = -3;
    private final IntVector fizzVector = IntVector.zero(SPECIES).add(FIZZ);
    private final IntVector buzzVector = IntVector.zero(SPECIES).add(BUZZ);
    private final IntVector fizzBuzzVector = fizzVector.add(buzzVector);

    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    public int[] simdFizzBuzz(int[] inputs) {

        int[] result = new int[inputs.length];
        int offset = 0;
        int upperBound = SPECIES.loopBound(inputs.length);

        for (; offset < upperBound; offset += SPECIES.length()) {
            IntVector inputPart = IntVector.fromArray(SPECIES, inputs, offset);
            IntVector resultPart = inputPart
                    .blend(fizzVector, moduloMask(inputPart, 3))
                    .blend(buzzVector, moduloMask(inputPart, 5))
                    .blend(fizzBuzzVector, moduloMask(inputPart, 15));
            resultPart.intoArray(result, offset);
        }
        return result;
    }

    private VectorMask<Integer> moduloMask(IntVector inputVector, int factor) {
        return inputVector.div(factor).mul(factor).compare(VectorOperators.EQ, inputVector);
    }

    public int[] scalarFizzBuzz(int[] values) {
        return Arrays.stream(values).map(result -> {
            if (result % 3 == 0 && result % 5 == 0) {
                result = FIZZ_BUZZ;
            } else if (result % 3 == 0) {
                result = FIZZ;
            } else if (result % 5 == 0) {
                result = BUZZ;
            }
            return result;
        }).toArray();
    }


}
