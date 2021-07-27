package Cryptography.StreamCipher;

import org.apache.commons.codec.DecoderException;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamCipherHandler {

    // This the function that we'll be using to encode and
    // decode the messages
    public static byte[] Encode(byte[] data, byte[] passphrase) {

        // create a new byte array that will store the processed
        // bytes and create a variable to store the index of the
        // current byte of the password we're using
        var processed_bytes = new byte[data.length];
        var passphrase_index = 0;

        // Loop over the message add the XOR result of the current
        // byte and the current password byte to the processed byte
        // array
        for (int i = 0; i < data.length; i++) {
            processed_bytes[i] = (byte)(data[i] ^ passphrase[passphrase_index]);
            passphrase_index++;

            // if the password index has become larger than the
            // length of the message, then reset it
            if (passphrase_index >= passphrase.length) {
                passphrase_index = 0;
            }
        }

        // return the processed bytes
        return processed_bytes;
    }



}
