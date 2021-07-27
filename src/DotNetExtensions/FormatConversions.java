package DotNetExtensions;

import org.apache.commons.codec.DecoderException;

import java.nio.charset.StandardCharsets;

public class FormatConversions {

    /**
     * This function will convert plain text to hex
     * @param s String to convert
     * @return Hex string
     */
    public static String ConvertPlainToHex(String s) {
        var bytes = s.getBytes(StandardCharsets.UTF_8);
        return new String(org.apache.commons.codec.binary.Hex.encodeHex(bytes));
    }

    /**
     * This function will convert hex to plain text
     * @param s Hex data
     * @return Plain Text
     */
    public static String ConvertHexToPlain(String s) {
        try {
            return new String(org.apache.commons.codec.binary.Hex.decodeHex(s));
        } catch (DecoderException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This function will convert a hex string to a byte array
     * @param s Hex data
     * @return Byte array
     */
    public static byte[] ConvertHexToBytes(String s) {
        try {
            return org.apache.commons.codec.binary.Hex.decodeHex(s);
        } catch (DecoderException e) {
            e.printStackTrace();
            return new byte[] {};
        }
    }
}
