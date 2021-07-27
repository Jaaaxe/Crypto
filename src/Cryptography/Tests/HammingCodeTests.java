package Cryptography.Tests;


import Cryptography.HammingCode.Corrector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Hamming Code Tests")
public class HammingCodeTests {

//    @Test
//    public void HammingCode_Encode() {
//        Assert.fail("Not implemented");
//    }
//
//    @Test
//    public void HammingCode_Validate() {
//        Assert.fail("Not implemented");
//    }
//
//    @Test
//    public void HammingCode_Encode_UWE() {
//        Assert.fail("Not implemented");
//    }

    @Test
    @DisplayName("Hamming code detection (UWE)")
    public void HammingCode_Detection_UWE() {
        Assertions.assertFalse(Corrector.Correct("1010010", false, true).CorrectionMade);
        Assertions.assertFalse(Corrector.Correct("1111111", false, true).CorrectionMade);
        Assertions.assertFalse(Corrector.Correct("1100110", false, true).CorrectionMade);
        Assertions.assertFalse(Corrector.Correct("0101010", false, true).CorrectionMade);
    }

    @Test
    @DisplayName("Hamming code correction (UWE)")
    public void HammingCode_Correction_UWE() {
        Assertions.assertEquals(1, Corrector.Correct( "1110010", false, true).CorrectionPosition);
        Assertions.assertEquals(1, Corrector.Correct( "1000001", false, true).CorrectionPosition);
        Assertions.assertEquals(2, Corrector.Correct( "1110110", false, true).CorrectionPosition);
        Assertions.assertEquals(3, Corrector.Correct( "1011101", false, true).CorrectionPosition);
    }


    @Test
    @DisplayName("Hamming code detection")
    public void HammingCode_Detection() {
        Assertions.assertFalse(Corrector.Correct("0011001", false, false).CorrectionMade);
        Assertions.assertFalse(Corrector.Correct("0100101", false, false).CorrectionMade);
        Assertions.assertFalse(Corrector.Correct("0101010", false, false).CorrectionMade);
        Assertions.assertFalse(Corrector.Correct("1011010", false, false).CorrectionMade);
    }

    @Test
    @DisplayName("Hamming code correction")
    public void HammingCode_Correction() {
        Assertions.assertEquals(0, Corrector.Correct( "1011001", false, false).CorrectionPosition);
        Assertions.assertEquals(1, Corrector.Correct( "0000101", false, false).CorrectionPosition);
        Assertions.assertEquals(2, Corrector.Correct( "0111010", false, false).CorrectionPosition);
        Assertions.assertEquals(3, Corrector.Correct( "1010010", false, false).CorrectionPosition);
    }
}
