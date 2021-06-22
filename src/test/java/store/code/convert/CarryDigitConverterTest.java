package store.code.convert;

import org.junit.Test;

public class CarryDigitConverterTest {
    private static char[] thirtySix1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static char[] thirtySix = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static char[] hex1 = "0123456789ABCDEF".toCharArray();
    private static char[] hex = "0123456789abcdef".toCharArray();
    private static char[] decimal = "0123456789".toCharArray();

    @Test
    public void test1() {
        System.out.println(new CarryDigitConverter(thirtySix, decimal).convert("iu93wa"));
        System.out.println(new CarryDigitConverter(decimal, thirtySix).convert("1314520"));
        System.out.println(new CarryDigitConverter(decimal, hex).convert("1314520"));
    }

}
