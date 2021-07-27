package Cryptography.PasswordAttacks;

public interface Attacker {
    void StartAttack();
    void StopAttack();
    void SetTargetIP(String ip);
    void SetTargetPort(int port);
    boolean IsAttackRunning();
    float GetProgress();

    void SubscribeToDebug(DebugListener d);
    void SubscribeToCompletion(AttackResultListener a);
}
