package Cryptography.Tests;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Credit Card Tests")
public class CreditCardTests {

    @Test
    @DisplayName("American Express")
    public void CreditCardValidation_AmericanExpress() {
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("346693136146318"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("375769244699077"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("376933120917610"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("373649909502919"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("341184846760544"));
    }

    @Test
    @DisplayName("Discover")
    public void CreditCardValidation_Discover() {
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("6011957384130493"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("6011542069585284"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("6011637451910155"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("6011317693965099"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("6011418008237271"));
    }

    @Test
    @DisplayName("Master Card")
    public void CreditCardValidation_MasterCard() {
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("5153209623561280"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("5597361951824113"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("5293895123917450"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("5369946737993533"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("5529781471256897"));
    }

    @Test
    @DisplayName("Visa")
    public void CreditCardValidation_Visa() {
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4556240692556972"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4307310575731208"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4556980923408077"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4716428995621109"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4916195379632642"));
    }

    @Test
    @DisplayName("Visa (13 Digits)")
    public void CreditCardValidation_Visa13Digit() {
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4916985301236"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4539674694859"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4539532670372"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4539189396644"));
        Assert.assertTrue(Cryptography.CreditCard.Verifier.VerifyCreditCard("4716893593969"));
    }

}
