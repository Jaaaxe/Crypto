package Cryptography.HammingCode;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class Corrector {
    public static CorrectionResult Correct(String BitArray_o, boolean UseOddParity, boolean UWEMode) {

        var BitArray = BitArray_o;
        // If we're gonna use the UWE mode we first have to reverse the array
        if (UWEMode) {
            BitArray = new StringBuilder(BitArray).reverse().toString();
        }

        // Create a variable to store the addresses of the bits with the
        // the value one and create another variable that will store the
        // correction result
        var Addresses = new ArrayList<int[]>();
        var CorrectionResult = new CorrectionResult();
        CorrectionResult.OriginalString = BitArray;

        for (int i = 0; i < BitArray.length(); i++) {
            if (BitArray.toCharArray()[i] == '1') {

                // Convert the current address to binary and
                // add padding to the left side for 3 places
                // so that it'll always have 3 digits
                var address = Integer.toBinaryString(i + 1);
                address = StringUtils.leftPad(address, 3, "0");

                // Create an array with 3 ints and place each bit
                // of the address into its respective position
                // and finally add it to the address array
                var address_array = new int[3];
                for (int x = 0; x < 3; x++) {
                    address_array[x] = Character.getNumericValue(address.toCharArray()[x]);
                }
                Addresses.add(address_array);
            }
        }

        // create a new variable to store the totals of the
        // address and add them up
        var ResultAddress = new int[3];
        for (var ad : Addresses) {
            ResultAddress[0] += ad[0];
            ResultAddress[1] += ad[1];
            ResultAddress[2] += ad[2];
        }

        // Calculate the modulus of each bit of address
        ResultAddress[0] = ResultAddress[0] % 2 == (UseOddParity ? 0 : 1) ? 0 : 1;
        ResultAddress[1] = ResultAddress[1] % 2 == (UseOddParity ? 0 : 1) ? 0 : 1;
        ResultAddress[2] = ResultAddress[2] % 2 == (UseOddParity ? 0 : 1) ? 0 : 1;

        // Parse that modulus result as a binary
        // and subtract 1 from it. (Because we're
        // working with arrays and array count from
        // zero
        var pos = Integer.parseInt("0" + DotNetExtensions.Array.CombineIntsToString(ResultAddress), 2) - 1;

        // If we are UWE mode subtract the position from 6 because
        // it counts backwards... or something
        if (UWEMode) {
            pos = (6 - pos);
        }

        pos -= 1;

        // Set the corrected bit array and set the correction
        // flag if the position is -1 and if we're doing UWE
        // method check if its 7
        var corrected_array = Reverse(BitArray_o);

        CorrectionResult.CorrectionMade = pos != -1;
        if (CorrectionResult.CorrectionMade) {
            corrected_array = FlipStringBit(corrected_array, pos);
        }

        // Set the corrected result and the correction position on
        // the result variable and return it
        CorrectionResult.CorrectedString = Reverse(corrected_array);
        CorrectionResult.CorrectionPosition = pos;

        return CorrectionResult;
    }

    private static String Reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    // This will accept a binary string and flip the big at the provided
    // index
    private static String FlipStringBit(String string, int position) {
        var return_obj = string.toCharArray();
        return_obj[position] = return_obj[position] == '0' ? '1' : '0';
        return DotNetExtensions.Array.CombineCharsToString(return_obj);
    }
}