import Cryptography.PasswordAttacks.*;
import Cryptography.Steganograpgy.TextStega;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class UIController implements Initializable, DebugListener, AttackResultListener {

    /////////////////
    // Credit Card //
    /////////////////
    @FXML
    private TextField CreditCardVerify_CCNumber;
    @FXML
    void CreditCardVerify_VerifyOnClick(MouseEvent event) {
        var ccnumber = CreditCardVerify_CCNumber.getText();

        if (ccnumber.matches("^[a-zA-Z]*$")){
            ShowMessageAlert("Credit card number is expected to be...a number","Credit Card Verifier","WARNING");
            return;
        } else if (ccnumber.length() < 16 || ccnumber.length() > 19) {
            ShowMessageAlert("Credit card number is expected to be greater than 16 digits and less than 19 digits","Credit Card Verifier","WARNING");
            return;
        }
        if (Cryptography.CreditCard.Verifier.VerifyCreditCard(ccnumber)) {
            ShowMessageAlert("The credit card number provided is valid","Credit Card Verifier","INFORMATION");
        } else {
            ShowMessageAlert("The credit card number provided is invalid","Credit Card Verifier","INFORMATION");
        }
    }
    @FXML
    void CreditCardVerify_Refresh(MouseEvent event) {
        CreditCardVerify_CCNumber.setText("");
    }

    //////////////////
    // Hamming Code //
    //////////////////
    @FXML
    private TextField HammingCode_InputBinary;
    @FXML
    private CheckBox HammingCode_UseOddParity;

    @FXML
    void HammingCode_FindErrorOnClick(MouseEvent event) {

        if (HammingCode_InputBinary.getText().matches("^[a-zA-Z23456789]*$")){
            ShowMessageAlert("Binary string expected","It's Hamming Time","WARNING");
            return;
        } else if (HammingCode_InputBinary.getText().length() != 7) {
            ShowMessageAlert("The bit array length must be 7 bits","It's Hamming Time","WARNING");
            return;
        }

        var solve_result = Cryptography.HammingCode.Corrector.Correct(HammingCode_InputBinary.getText(), HammingCode_UseOddParity.isSelected(), true);
        if (solve_result.CorrectionMade) {
            ShowMessageAlert("Error found at position "+Integer.toString(solve_result.CorrectionPosition + 1)+("\nCorrected String: "+solve_result.CorrectedString),"Hamming Code Corrector","WARNING");

        } else {
            ShowMessageAlert("No Errors","Hamming Code Corrector","INFORMATION");

        }

    }
    @FXML
    void Hamming_Refresh(MouseEvent event) {
        HammingCode_InputBinary.setText("");
    }

    /////////////////////
    // Password Attack //
    /////////////////////
    Cryptography.PasswordAttacks.BruteForceAttack PasswordAttacks_BruteForceAttack = new BruteForceAttack();
    Cryptography.PasswordAttacks.DictionaryAttack PasswordAttacks_DictionaryAttack = new DictionaryAttack();
    Cryptography.PasswordAttacks.Server PasswordAttacks_Server;

    @FXML
    private ProgressIndicator PasswordAttacks_Progress;
    @FXML
    private TextArea PasswordAttacks_ServerOutput;
    @FXML
    private TextArea PasswordAttacks_ClientOutput;
    @FXML
    private TextField PasswordAttacks_ServerPassword;
    @FXML
    private TextField PasswordAttacks_ClientTargetIP;
    @FXML
    private CheckBox PasswordAttacks_BruteForceUppercase;
    @FXML
    private CheckBox PasswordAttacks_BruteForceLowercase;
    @FXML
    private CheckBox PasswordAttacks_BruteForceSpecialChars;
    @FXML
    private CheckBox PasswordAttacks_BruteForceNumbers;
    @FXML
    private TextField PasswordAttacks_DictionaryFile;
    @FXML
    private Button PasswordAttacks_OpenDictionaryFileButton;
    @FXML
    private Button PasswordAttacks_AttackButton;
    @FXML
    private  Button PasswordAttacks_ServerButton;
    @FXML
    private ComboBox<Attacker> PasswordAttacks_AttackMode;
    @FXML
    private TextField PasswordAttacks_ServerPort;
    @FXML
    private TextField PasswordAttacks_ClientTargetPort;
    @FXML
    private TextField PasswordAttacks_BruteForcePasswordLengthPasswordAttacks_BruteForcePasswordLength;
    @FXML
    void PasswordAttacks_AttackModeChanged(KeyEvent event) {

    }

    @FXML
    void PasswordAttacks_DictionaryFileOpenClicked(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Dictionary File");
        var file = chooser.showOpenDialog(PasswordAttacks_DictionaryFile.getScene().getWindow());
        if (file != null) {
            this.PasswordAttacks_DictionaryAttack.ClearDictionary();
            this.PasswordAttacks_DictionaryAttack.AddDictionaryFile(file);
        }
    }



    @FXML
    void PasswordAttacks_BruteForceConfigChange(InputMethodEvent event) {
        PasswordAttacks_BruteForceAttack.ConfigureAttack(
                Integer.parseInt(PasswordAttacks_BruteForcePasswordLengthPasswordAttacks_BruteForcePasswordLength.getText()),
                PasswordAttacks_BruteForceUppercase.isSelected(),
                PasswordAttacks_BruteForceLowercase.isSelected(),
                PasswordAttacks_BruteForceSpecialChars.isSelected(),
                PasswordAttacks_BruteForceNumbers.isSelected()
        );

    }

    @FXML
    void PasswordAttacks_BruteForceConfigChangeIncl(MouseEvent event) {
        PasswordAttacks_BruteForceAttack.ConfigureAttack(
                Integer.parseInt(PasswordAttacks_BruteForcePasswordLengthPasswordAttacks_BruteForcePasswordLength.getText()),
                PasswordAttacks_BruteForceUppercase.isSelected(),
                PasswordAttacks_BruteForceLowercase.isSelected(),
                PasswordAttacks_BruteForceSpecialChars.isSelected(),
                PasswordAttacks_BruteForceNumbers.isSelected()
        );

    }


    @FXML
    void PasswordAttacks_StartAttack(MouseEvent event) {
        PasswordAttacks_AttackMode.setDisable(true);
        PasswordAttacks_AttackButton.setText("Stop Attack");
        var attack = PasswordAttacks_AttackMode.getSelectionModel().selectedItemProperty().getValue();
        attack.SetTargetPort(5002);
        attack.SetTargetIP("127.0.0.1");
        attack.StartAttack();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SyncProgress();
            }
        }).start();
    }

    @FXML
    void PasswordAttacks_StartServer(MouseEvent event) {

        if (this.PasswordAttacks_Server == null) {
            this.StartServer();
        } else {
            if  (this.PasswordAttacks_Server.ServerRunning) {
                this.PasswordAttacks_Server.StopServer();
                this.PasswordAttacks_Server = null;
            } else {
                this.PasswordAttacks_Server.StartServer();
            }
        }
        if (this.PasswordAttacks_Server != null) {
            PasswordAttacks_ServerButton.setText(this.PasswordAttacks_Server.ServerRunning ? "Stop Server" : "Start Server");
        } else {
            PasswordAttacks_ServerButton.setText("Start Server");
        }

    }

    private void StartServer() {
        PasswordAttacks_ServerOutput.clear();
        System.out.println(PasswordAttacks_ServerPort.getText());
        this.PasswordAttacks_Server = new Server(Integer.parseInt(PasswordAttacks_ServerPort.getText()), PasswordAttacks_ServerPassword.getText());
        this.PasswordAttacks_Server.SubscribeToDebug(this);
        this.PasswordAttacks_Server.StartServer();
    }

    @Override
    public void OnDebug(Object sender, String s) {
        if (sender == this.PasswordAttacks_BruteForceAttack || sender == this.PasswordAttacks_DictionaryAttack) {
            PasswordAttacks_ClientOutput.setText(
                    PasswordAttacks_ClientOutput.getText() + s
            );
        } else {
            PasswordAttacks_ServerOutput.setText(
                    PasswordAttacks_ServerOutput.getText() + s
            );
        }
    }

    @Override
    public void AttackCompleted(Attacker a, AttackResult r) {
        PasswordAttacks_AttackMode.setDisable(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PasswordAttacks_AttackButton.setText("Start Attack");
            }
        });

        var message = "";
        if (r.AttackSuccessful) {
            message = "Password cracking successful! Password : " + r.CrackedPassword + "\nAttempts : " + r.Attempts;
        } else {
            message = "Password cracking failed.\nAttempts : " + r.Attempts;
        }
        ShowMessageAlert("Password Cracking","Hackerman","INFORMATION");
    }

    private void SyncProgress() {
        try {
            PasswordAttacks_Progress.setProgress(0);
            var attack = PasswordAttacks_AttackMode.getSelectionModel().selectedItemProperty().getValue();
            System.out.println(attack.IsAttackRunning());
            while (attack.IsAttackRunning()) {
                PasswordAttacks_Progress.setProgress(attack.GetProgress());
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ProgressChange(int current_taskIndex, int total_tasks) {
        // Yeah, we are not going to use this...
    }
    //endregion
    //region Stream Cipher
    @FXML
    private TextArea StreamCipher_HexOut;
    @FXML
    private TextArea StreamCipher_PlainTextIn;
    @FXML
    private TextField StreamCipher_PasswordField;
    @FXML
    private Button StreamCipher_RunProcessButton;
    @FXML
    private Button StreamCipher_ConvertHexToPlainButton;
    @FXML
    private Button StreamCipher_ConvertPlainToHexButton;
    @FXML
    void StreamCipher_HexToPlainClicked(MouseEvent event) {
        StreamCipher_PlainTextIn.setText(
                DotNetExtensions.FormatConversions.ConvertHexToPlain(
                        StreamCipher_HexOut.getText()
                )
        );
    }

    @FXML
    void StreamCipher_PlainToHexClicked(MouseEvent event) {
        StreamCipher_HexOut.setText(
                DotNetExtensions.FormatConversions.ConvertPlainToHex(
                        StreamCipher_PlainTextIn.getText()
                )
        );
    }

    @FXML
    void StreamCipher_RunProcess(MouseEvent event) {
        StreamCipher_HexOut.setText(
            new String(
                org.apache.commons.codec.binary.Hex.encodeHex(
                    Cryptography.StreamCipher.StreamCipherHandler.Encode(
                            DotNetExtensions.FormatConversions.ConvertHexToBytes(
                                StreamCipher_HexOut.getText()
                        ),
                        StreamCipher_PasswordField.getText().getBytes(StandardCharsets.UTF_8)
                    )
                )
            )
        );
    }

    ///////////////////////////////////
    // Steganography + Stream Cipher //
    ///////////////////////////////////
    @FXML
    private TextArea TextStega_Original;

    @FXML
    private TextArea TextStega_Encoded;

    @FXML
    private TextArea TextStega_Secret;

    @FXML
    private TextField SteganographyCiperPassword;

    @FXML
    private CheckBox TextStegaEnableEncryption;

    @FXML
    void TextStega_DecodeClicked(MouseEvent event) {

        try {
            if (TextStegaEnableEncryption.isSelected()) {
                TextStega_Secret.setText(
                        Cryptography.Steganograpgy.TextStega.Decode(
                                TextStega_Encoded.getText(),
                                SteganographyCiperPassword.getText()
                        )
                );
            } else {
                TextStega_Secret.setText(
                        Cryptography.Steganograpgy.TextStega.Decode(TextStega_Encoded.getText())
                );
            }
        } catch (Exception e) {
            System.out.println("Oh noes");
            e.printStackTrace();
        }
    }

    @FXML
    void TextStega_EncodeClicked(MouseEvent event) {
        var max_char_count = Cryptography.Steganograpgy.TextStega.CountPossibleCharacters(TextStega_Original.getText());
        if (!Cryptography.Steganograpgy.TextStega.CheckIfEncodeable(
                TextStega_Original.getText(),
                TextStega_Secret.getText()
        )){
            ShowMessageAlert("This message can only encode a secret message of " + max_char_count + " character(s). Please add more spaces.","Stream Cipher + Steganography","WARNING");
            return;
        }
        try {
            if (TextStegaEnableEncryption.isSelected()) {
                TextStega_Encoded.setText(
                        Cryptography.Steganograpgy.TextStega.Encode(
                                TextStega_Original.getText(),
                                TextStega_Secret.getText(),
                                SteganographyCiperPassword.getText()
                        ));

            } else {
                TextStega_Encoded.setText(
                        Cryptography.Steganograpgy.TextStega.Encode(
                                TextStega_Original.getText(),
                                TextStega_Secret.getText()
                        ));

            }
            TextStega_Original.setText("");
            TextStega_Secret.setText("");
            SteganographyCiperPassword.setText("");
        } catch (Exception e) {
            System.out.println("Oh noes");
            e.printStackTrace();
        }
    }

    @FXML
    void TextStega_Refresh(MouseEvent event){
        TextStega_Original.setText("");
        TextStega_Encoded.setText("");
        TextStega_Secret.setText("");
        SteganographyCiperPassword.setText("");
    }
    @FXML Label chars;
    @FXML
    void typetest(KeyEvent event) {
        chars.textProperty().bind(TextStega_Secret.textProperty()
                .length()
                .asString("Char: %d / "+Integer.toString(Cryptography.Steganograpgy.TextStega.CountPossibleCharacters(TextStega_Original.getText()))));
    }

    /////////////
    // General //
    /////////////
    @FXML
    private void ShowMessageAlert(String message, String title, String type) {
        Alert alert = new Alert(Alert.AlertType.valueOf(type), message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Attacker> Modes =
                FXCollections.observableArrayList(
                        PasswordAttacks_BruteForceAttack,
                        PasswordAttacks_DictionaryAttack
                );


        PasswordAttacks_AttackMode.setItems(Modes);
        PasswordAttacks_DictionaryAttack.SubscribeToDebug(this);
        PasswordAttacks_BruteForceAttack.SubscribeToDebug(this);
        PasswordAttacks_DictionaryAttack.SubscribeToCompletion(this);
        PasswordAttacks_BruteForceAttack.SubscribeToCompletion(this);
        TextStega_Original.setWrapText(true);
        TextStega_Encoded.setWrapText(true);
        TextStega_Secret.setWrapText(true);
        SteganographyCiperPassword.visibleProperty().bind(TextStegaEnableEncryption.selectedProperty());

        System.out.println("App Loaded");


    }
}
