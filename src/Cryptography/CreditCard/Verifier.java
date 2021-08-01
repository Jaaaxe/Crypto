package Cryptography.CreditCard;

import DotNetExtensions.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Verifier {
    public static boolean VerifyCreditCard(String original) {

        // Convert the credit card number into a character array
        // and init an arraylist of integers
        var character_array = original.toCharArray();
        var integer_array = new ArrayList<Integer>();

        // Convert the character array that contains the credit
        // card numbers into an integer array
        for (char c : character_array) {
            integer_array.add(Character.getNumericValue(c));
        }

        // Reverse it so it'll be easier for us to process it
        Collections.reverse(integer_array);

        // Isolate the check digit and remaining digits
        var check_digit = integer_array.get(0);
        var remaining_digits = integer_array.subList(1, integer_array.size());

        // Define two sets for odd and even sets...
        // and do the luhn method (https://datagenetics.com/blog/july42013/index.html)
        ArrayList<Integer> even_set = new ArrayList<Integer>();
        ArrayList<Integer> odd_set = new ArrayList<Integer>();

        for (int i = 0; i < remaining_digits.size(); i++) {
            if ((i % 2) == 0) {
                even_set.add(SplitAndAdd(2 * remaining_digits.get(i)));
            } else {
                odd_set.add(SplitAndAdd(remaining_digits.get(i)));
            }
        }

        // Add up the totals and
        // calculate the check digit characters
        var total = Array.GetTotalOf(even_set) + Array.GetTotalOf(odd_set);
        var calculated_check_char_array = Integer.toString(total).toCharArray();
        var calculated_check = Character.getNumericValue(calculated_check_char_array[calculated_check_char_array.length -1]);

        // Reduce the check digit amount
        if (calculated_check != 0) calculated_check = 10 - calculated_check;

        // return the boolean
        return Objects.equals(calculated_check, check_digit);
    }

    // this function will accept a number and if it has more than
    // two digits it will split them up and add them together
    private static int SplitAndAdd(int i) {
        var digits = Integer.toString(i);
        if (digits.length() == 1) return i;
        var total = 0;
        for (var c : Integer.toString(i).toCharArray()) {
            total += Character.getNumericValue(c);
        }
        return total;
    }
}
