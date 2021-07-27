package Cryptography.Tests;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.UnsupportedEncodingException;

@DisplayName("Steganography Tests")
public class SteganographyTests {

    public static String PlainMessage = "Hello world, this is a sentence that has a hidden message encoded within it. It can be read by processing the spaces between each word. Some spaces will use NBSP instead of normal spaces.";
    public static String EncodedMessage = "Hello world,\u00A0this is\u00A0a sentence\u00A0that has a hidden\u00A0message encoded within it.\u00A0It can\u00A0be read\u00A0by processing\u00A0the spaces between\u00A0each\u00A0word. Some\u00A0spaces will\u00A0use NBSP\u00A0instead of normal spaces.";

    @Test
    @DisplayName("Read Test")
    public void ReadTest() throws UnsupportedEncodingException {
        Assert.assertEquals("TEST", Cryptography.Steganography.TextStega.Decode(EncodedMessage));
    }

    @Test
    @DisplayName("Write Test")
    public void WriteTest() throws UnsupportedEncodingException {
        Assert.assertEquals(EncodedMessage, Cryptography.Steganography.TextStega.Encode(PlainMessage, "TEST"));
    }

//    @Test
//    @DisplayName("Read Test \\w Password")
//    public void ReadTestPassword() throws UnsupportedEncodingException {
//        Assert.assertEquals("TEST", Cryptography.Steganography.TextStega.Decode(EncodedMessage, "Password"));
//    }
//
//    @Test
//    @DisplayName("Write Test \\w Password")
//    public void WriteTestPassword() throws UnsupportedEncodingException {
//        Assert.assertEquals(EncodedMessage, Cryptography.Steganography.TextStega.Encode(PlainMessage, "TEST", "Password"));
//    }
}
