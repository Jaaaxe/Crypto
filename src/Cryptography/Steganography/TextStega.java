package Cryptography.Steganography;

import Cryptography.StreamCipher.StreamCipherHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TextStega {

    // We must have definitions of what the ones and zero bits are
    public static char OneBitFlag = (char)0xa0; // Non-Breaking Space (NBSP)
    public static char ZeroBitFlag = (char)0x20; // Space character
    public static char TerminationFlag = (char)0x03; // End of Transmission Flag

    // Overloading constructor for when the password is not provided
    public static String Decode(String message) {
        return Decode(message, "");
    }

    public static String Decode(String message, String Password) {
        // Create a new boolean character list and
        // loop through the message
        var Bits = new ArrayList<Boolean>();
        for (var c : message.toCharArray()) {
            if (c == OneBitFlag) { // if the character is an NBSP add a one
                Bits.add(true);
                continue;
            }

            if (c == ZeroBitFlag) { // if the character is a regular space add a zero
                Bits.add(false);
            }
        }
        // Convert the bits into an ASCII encoded string
        Boolean[] BitArray = Bits.toArray(new Boolean[0]);
        var ByteArray = DotNetExtensions.Array.BitsToBytes(BitArray);


        if (!Password.isBlank()) {
            ByteArray = StreamCipherHandler.Encode(ByteArray, Password.getBytes(StandardCharsets.US_ASCII));
        }


        var ConvertedBytes = new ArrayList<Byte>();

        for (var b : ByteArray) {
            if (b == (byte)TerminationFlag) break;
            ConvertedBytes.add(b);
        }

        var ConvertByteArray = DotNetExtensions.Array.ConvertListToArray(ConvertedBytes);


        return new String(ConvertByteArray, StandardCharsets.US_ASCII);
    }

    // Overloading constructor for when a password is not provided
    public static String Encode(String medium_message, String hidden_message) {
        return Encode(medium_message, hidden_message, "");
    }

    public static String Encode(String medium_message, String hidden_message, String Password) {

        // Create an array to store the bits of the hidden message
        // and convert the medium message into a character array and
        // store it in a variable
        var hidden_msg_bits = DotNetExtensions.Array.BytesToBits(
                DotNetExtensions.Array.AddElementToArray(
                        hidden_message.getBytes(StandardCharsets.US_ASCII),
                        (byte)TerminationFlag)
        );
        var medium_msg_chars = medium_message.toCharArray();

        // Encrypt the data
        if (!Password.isBlank()) {
            hidden_msg_bits = DotNetExtensions.Array.BytesToBits(StreamCipherHandler.Encode(
                    DotNetExtensions.Array.AddElementToArray(hidden_message.getBytes(StandardCharsets.US_ASCII), (byte)TerminationFlag),
                    Password.getBytes(StandardCharsets.US_ASCII))
            );
        }

        // initialize the variables that will store the current indexes
        var index = 0;
        var bit_index = 0;

        // Loop through all the bits of the hidden message
        while (bit_index < hidden_msg_bits.length) {

            // Go through all the characters of the medium
            // message until a regular space is found
            while (medium_msg_chars[index] != ZeroBitFlag) {
                index++;
            }

            // if the current hidden bit is one, replace the
            // character with the NBSP and if its a zero, leave
            // it as it is
            if (hidden_msg_bits[bit_index]) {
                medium_msg_chars[index] = OneBitFlag;
            }

            // Increment the indexers
            bit_index++;
            index++;
        }

        // convert the character array into a string and return it
        var string = new String(medium_msg_chars);

        return string;

    }

    // Check to ensure there are enough spaces to accommodate characters
    public static int CountPossibleCharacters(String medium_message) {
        var count = 0;
        for (var c : medium_message.toCharArray()) {
            if (c == ' ') {
                count++;
            }
        }

        return (int)(count / 8);
    }

    // Verification to make sure the secret + flag can be added to message
    public static boolean CheckIfEncodeable(String medium_message, String secret) {
        return secret.length() < CountPossibleCharacters(medium_message);
    }
}
