package Cryptography.Tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Stream Cipher Tests")
public class StreamCipherTests {

    public static byte[] Message = {0x54, 0x65, 0x73, 0x74, 0x20, 0x4d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65};
    public static byte[] Password = {0x50, 0x61, 0x73, 0x73, 0x77, 0x6f, 0x72, 0x64};
    public static byte[] EncodedMessage = {0x04, 0x04, 0x00, 0x07, 0x57, 0x22, 0x17, 0x17, 0x23, 0x00, 0x14, 0x16 };


    @Test
    @DisplayName("Encode Test")
    public void EncodeTest() {
        Assertions.assertArrayEquals(EncodedMessage, Cryptography.StreamCipher.StreamCipherHandler.Encode(Message, Password));
    }

    @Test
    @DisplayName("Decode Test")
    public void DecodeTest() {
        Assertions.assertArrayEquals(Message, Cryptography.StreamCipher.StreamCipherHandler.Encode(EncodedMessage, Password));
    }


}
