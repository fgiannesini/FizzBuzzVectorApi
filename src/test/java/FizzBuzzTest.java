import com.fgiannesini.FizzBuzz;
import org.junit.jupiter.api.Test;

import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FizzBuzzTest {

    @Test
    void should_check_fizz_buzz_scalar() {
        FizzBuzz fizzBuzz = new FizzBuzz();
        int[] results = fizzBuzz.scalarFizzBuzz(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        assertArrayEquals(new int[]{-3, 1, 2, -1, 4, -2, -1, 7, 8, -1, -2, 11, -1, 13, 14, -3}, results);
    }

    @Test
    void should_check_fizz_buzz_simd() {
        FizzBuzz fizzBuzz = new FizzBuzz();
        int[] results = fizzBuzz.simdFizzBuzz(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        assertArrayEquals(new int[]{-3, 1, 2, -1, 4, -2, -1, 7, 8, -1, -2, 11, -1, 13, 14, -3}, results);
    }

    @Test
    void should_check_random_fizz_buzz() {
        FizzBuzz fizzBuzz = new FizzBuzz();
        int[] input = RandomGenerator.getDefault().ints(8, 0, Integer.MAX_VALUE).toArray();
        assertArrayEquals(fizzBuzz.scalarFizzBuzz(input), fizzBuzz.simdFizzBuzz(input));
    }
}