package DotNetExtensions;

import java.util.ArrayList;

public class Array {


    /**
     * This function will accept an integer array and reverse it
     * @param data Array to reverse
     * @return Reversed array
     */
    public static int[] Reverse(int[] data) {
        for(int i = 0; i < data.length / 2; i++)  {
             int temp = data[i];
             data[i] = data[data.length - i - 1];
             data[data.length - i - 1] = temp;
         }
         return data;
     }

    /**
     * Accepts an int array and returns the total of the array
     * @param data Array to add up
     * @return Total of the array
     */
    public static int GetTotalOf(int[] data) {
        var total = 0;
        for (var i : data) {
            total += i;
        }
        return total;
    }

    /**
     * Accepts an integer list and returns the total of it
     * @param data List to add up
     * @return Total of the list
     */
     public static int GetTotalOf(ArrayList<Integer> data) {
         var total = 0;
         for (var i : data) {
             total += i;
         }
         return total;
     }

    /**
     * This function will accept an array of integers and
     * return a string of it
     * @param ints Integers to join
     * @return Joined string
     */
    public static String CombineIntsToString(int[] ints) {
         var ret = "";
         for (var i : ints) {
             ret += Integer.toString(i);
         }
         return ret;
    }

    /**
     * This function will accept an array of chars and join them
     * @param chars Characters to join
     * @return Joined string
     */
    public static String CombineCharsToString(char[] chars) {
         var ret = "";
         for (var c : chars) {
            ret += Character.toString(c);
         }
         return ret;
    }

    public static byte[] AddElementToArray(byte[] ba, byte b) {
        var newArray = new byte[ba.length + 1];
        for (int i = 0; i < ba.length; i++) {
            newArray[i] = ba[i];
        }
        newArray[newArray.length - 1] = b;
        return newArray;
    }

    /**
     * This function will accept an array of booleans
     * and convert it into an array of bytes
     * @param input Bits to convert
     * @return Byte array
     */
    public static byte[] BitsToBytes(Boolean[] input) {
        byte[] toReturn = new byte[input.length / 8];
        for (int entry = 0; entry < toReturn.length; entry++) {
            for (int bit = 0; bit < 8; bit++) {
                if (input[entry * 8 + bit]) {
                    toReturn[entry] |= (128 >> bit);
                }
            }
        }
        return toReturn;
    }

    /**
     * This function will accept an array of bytes and return
     * a list of bits
     * @param input Bytes to convert
     * @return Converted bits
     */
    public static Boolean[] BytesToBits(byte[] input) {
        var out = new ArrayList<Boolean>();
        for (byte x : input) {
            out.add((x & 0x80) != 0);
            out.add((x & 0x40) != 0);
            out.add((x & 0x20) != 0);
            out.add((x & 0x10) != 0);
            out.add((x & 0x08) != 0);
            out.add((x & 0x04) != 0);
            out.add((x & 0x02) != 0);
            out.add((x & 0x01) != 0);
        }
        return out.toArray(new Boolean[0]);
    }

    public static byte[] ConvertListToArray(ArrayList<Byte> bl) {
        var ba = new byte[bl.size()];
        for (int i = 0; i < bl.size(); i++) {
            ba[i] = bl.get(i);
        }
        return ba;
    }

}
