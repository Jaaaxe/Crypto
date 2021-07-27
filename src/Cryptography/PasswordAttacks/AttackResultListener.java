package Cryptography.PasswordAttacks;

public interface AttackResultListener {
    void AttackCompleted(Attacker a, AttackResult r);
    void ProgressChange(int current_taskIndex, int total_tasks);
}
