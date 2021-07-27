import java.io.File;

public class TestRunTime {
    public static void main(String[] args) throws InterruptedException {

        var dictattack = new Cryptography.PasswordAttacks.DictionaryAttack();
        dictattack.SetTargetIP("127.0.0.1");
        dictattack.SetTargetPort(5002);


        dictattack.AddDictionaryFile(new File("D:\\GitHub\\CryptographyFinalYear\\fasttrack.txt"));
        dictattack.StartAttack();


        while (dictattack.IsAttackRunning) {
            Thread.sleep(100);
        }
    }
}
