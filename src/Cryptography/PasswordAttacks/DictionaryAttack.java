package Cryptography.PasswordAttacks;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.logging.SocketHandler;

public class DictionaryAttack implements Attacker {

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

    public DictionaryAttack() {
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
        while (!AttackHalted && !PasswordFound && index < Chunk.size()) {

            try {
                var csocket = new Socket(this.TargetIP, this.TargetPort);
                var input_stream = csocket.getInputStream();
                var output_stream = csocket.getOutputStream();
                var input_stream_reader = new BufferedReader(new InputStreamReader(input_stream));
                var output_stream_writer = new PrintWriter(output_stream, true);

//                System.out.println("[DICTIONARY] Connected...");
//                ReadStream(input_stream);
                input_stream_reader.readLine();
                input_stream_reader.readLine();

//                System.out.println("[DICTIONARY] Trying " + Chunk.get(index));
                output_stream_writer.println(Chunk.get(index) + '\n');
                var password_repsonse = input_stream_reader.readLine();

//                System.out.println("[DICTIONARY] >> Response : " + password_repsonse);
                this.Progress = ((float)(index + 1) / (float)this.Dictionary.size());
                if (password_repsonse.contains("Access Granted")) {
                    Debug("Password found : " + Chunk.get(index) + "\n");
                    System.out.println("Password Found! : " + Chunk.get(index));
                    this.MatchedPassword = Chunk.get(index);
                    this.PasswordFound = true;
                    this.AttackHalted = true;
                    this.IsAttackRunning = false;

                    var att_res = new AttackResult();
                    att_res.Attempts = index + 1;
                    att_res.AttackSuccessful = true;
                    att_res.CrackedPassword = Chunk.get(index);
                    att_res.Duration = -1;

                    NotifyResult(att_res);
                    return;
                }

//                System.out.println(String.format("[DICTIONARY] Progress : %f", this.Progress));
                input_stream.close();
                output_stream.close();
                csocket.close();
                index++;
                Attempts++;
            } catch (Exception e) {
                e.printStackTrace();
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
    public void ClearDictionary() {
        this.Dictionary.clear();
    }

    public int AddDictionaryFile(File file) {
        try {
            var reader = new Scanner(file);
            var count = 0;
            while (reader.hasNextLine()) {
                this.Dictionary.add(reader.nextLine());
                count++;
            }

            Debug(String.format("%d passwords added to dictionary\n", count));
            return count;
        } catch (Exception e) {
            return -1;
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

    public void SetProgress(int current, int total) {
        for (var d : AttackResultSubscribers) {
            d.ProgressChange(current, total);
        }
    }

    //endregion

    @Override
    public String toString() {
        return "Dictionary Attack";
    }
}
