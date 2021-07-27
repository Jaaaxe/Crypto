package Cryptography.HammingCode;

public class CorrectionResult {

    // This class will be returned by the Corrector class.
    // It will contain the relevant information from the correction process
    public String OriginalString = "";
    public String CorrectedString = "";
    public int CorrectionPosition = -1;
    public boolean CorrectionMade = false;
}
