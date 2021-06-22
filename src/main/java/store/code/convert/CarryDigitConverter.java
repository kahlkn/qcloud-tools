package store.code.convert;

import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.ONE;
import static artoria.common.Constants.ZERO;
import static java.util.Collections.unmodifiableMap;

public class CarryDigitConverter {
    private Map<Character, Integer> sourceNotationsMap;
    private char[] targetNotations;
    private int sourceCarryDigit;
    private int targetCarryDigit;

    public CarryDigitConverter(char[] sourceNotations, char[] targetNotations) {
        this.sourceNotationsMap = new HashMap<Character, Integer>();
        this.targetNotations = targetNotations;
        this.sourceCarryDigit = sourceNotations.length;
        this.targetCarryDigit = targetNotations.length;
        for (int i = 0; i < sourceCarryDigit; i++) {
            sourceNotationsMap.put(sourceNotations[i], i);
        }
        this.sourceNotationsMap = unmodifiableMap(sourceNotationsMap);
    }

    public String convert(String source) {
        int length = source.length();
        long number = ZERO; int count = ZERO;
        for (int i = length - ONE; i >= ZERO; i--) {
            char charAt = source.charAt(i);
            Integer integer = sourceNotationsMap.get(charAt);
            long pow = Double.valueOf(Math.pow(sourceCarryDigit, count)).longValue();
            number = number + integer * pow;
            count++;
        }
        //System.out.println(">>>>" + number);

        StringBuilder builder = new StringBuilder();
        long quotient;
        int remainder;
        do {
            quotient = number/targetCarryDigit;
            remainder = (int) (number % targetCarryDigit);
            builder.insert(ZERO, targetNotations[remainder]);
            number=quotient;
        } while (quotient > ZERO);

        /*
        *    a10b
        *    11*16^0 + 0 *
        *
        *    1121
        *
        *    1*10^0 + 2*10^1 + 1*10^2
        *
        *
        *    1121
        *    / 10
        *    112   -    1
        *    11    -    2
        *
        *
        * */

        return builder.toString();
    }

}
