import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.util.Arrays.fill;

public class KaratsubaMortalTriploCarpado {
    
    public static void main(String[] args) {
        System.out.println(kmultstr3(args[0], args[1]));
    }

    /**
     * Deve dividir os números em três partes, usando dois shifts e1 e e2.
     * <p>
     * a ∗ b = (a1 ∗ 10e1 + a2 ∗ 10e2 + a3) ∗ (b1 ∗ 10e1 + b2 ∗ 10e2 + b3) (1)
     * O algoritmo deve apenas traduzir a expressão (1) acima sem a otimização que existia no algoritmo original de Karatsuba.
     * <p>
     * Considerações:
     * - A multiplicação é recursiva, feita usando as partes de cada número e reconstruindo o resultado original.
     * - Os números devem ser passados como argumentos pela linha de comando.
     * - Os números devem ser tratados sempre como Strings.
     * - As operações aritméticas só devem ser feitas quando tivermos apenas um dígito
     * - É preciso implementar operações de soma de inteiros para Strings
     * - Não podem ser usados inteiros longos nem nada parecido
     *
     * @param a
     * @param b
     * @return
     */

    /**
     * @param a
     * @param b
     * @return
     */

    public static String kmultstr3(String a, String b) {
        if (anyZero(a, b)) return "0";
        if (onlyOneDigit(a, b)) return valueOf(parseInt(a) * parseInt(b));

        while (isNotSameSize(a, b)) {
            a = padWithZeros(a, b);
            b = padWithZeros(b, a);
        }

        var length = a.length();

        String a1, a2, a3, b1, b2, b3;
        if (length < 3) {
            a1 = "0"; a2 = a.substring(0, 1); a3 = a.substring(1);
            b1 = "0"; b2 = b.substring(0, 1); b3 = b.substring(1);
        } else {
            var end1 = length / 3 + length % 3;
            var end2 = end1 + length / 3;
            a1 = a.substring(0, end1); a2 = a.substring(end1, end2); a3 = a.substring(end2);
            b1 = b.substring(0, end1); b2 = b.substring(end1, end2); b3 = b.substring(end2);
        }

        var a1b1 = kmultstr3(a1, b1);
        var a1b2 = kmultstr3(a1, b2);
        var a1b3 = kmultstr3(a1, b3);

        var a2b1 = kmultstr3(a2, b1);
        var a2b2 = kmultstr3(a2, b2);
        var a2b3 = kmultstr3(a2, b3);

        var a3b1 = kmultstr3(a3, b1);
        var a3b2 = kmultstr3(a3, b2);
        var a3b3 = kmultstr3(a3, b3);

        var s1 = shift(a2.length() + a3.length());
        var s2 = shift(a3.length());

        return removeNonSignificantZeros(Stream.of(
                a1b1.concat(s1).concat(s1), a1b2.concat(s1).concat(s2), a1b3.concat(s1),
                a2b1.concat(s1).concat(s2), a2b2.concat(s2).concat(s2), a2b3.concat(s2),
                a3b1.concat(s1), a3b2.concat(s2), a3b3)
                .reduce("", KaratsubaMortalTriploCarpado::sum));
    }

    private static String sum(String a, String b) {
        if (bothZero(a, b)) return "0";

        while (isNotSameSize(a, b)) {
            a = padWithZeros(a, b);
            b = padWithZeros(b, a);
        }

        var length = a.length();

        var carry = 0;
        var result = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            var x = parseInt(valueOf(a.charAt(i)));
            var y = parseInt(valueOf(b.charAt(i)));

            var sum = Integer.toString(x + y + carry);
            carry = 0;

            if (sum.length() > 1) {
                carry = 1;
                sum = valueOf(sum.charAt(1));
            }

            result.insert(0, sum);
        }

        if (carry == 1) {
            result.insert(0, carry);
        }

        return result.toString();
    }

    private static String shift(int length) {
        var shift = new char[length];
        fill(shift, '0');
        return new String(shift);
    }

    private static String padWithZeros(String a, String b) {
        if (b.length() > a.length()) {
            a = "0".concat(a);
        }
        return a;
    }

    private static boolean isNotSameSize(String a, String b) {
        return a.length() != b.length();
    }

    private static boolean onlyOneDigit(String a, String b) {
        return a.length() == 1 && b.length() == 1;
    }

    private static boolean anyZero(String a, String b) {
        return isZero(a) || isZero(b);
    }

    private static boolean bothZero(String a, String b) {
        return isZero(a) && isZero(b);
    }

    private static boolean isZero(String x) {
        return removeNonSignificantZeros(x).isBlank();
    }

    private static String removeNonSignificantZeros(String x) {
        return x.replaceAll("^0+", "");
    }
}
