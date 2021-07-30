package Cryptography.PasswordAttacks;

public interface AttackResultListener {
    void AttackCompleted(Attacker a, AttackResult r);
}
