import Cryptography.PasswordAttacks.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.net.URL;
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
    private ProgressBar Traversed;
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
    private Slider PasswordAttacks_BruteForcePasswordLength;

    @FXML
    private Label PasswordLength;

    @FXML
    private Label AttackTitle;

    @FXML
    private void PasswordAttacks_AttackModeChanged(ActionEvent event) {

        Attacker type = PasswordAttacks_AttackMode.getValue();

        if(type.toString().equals("Bruteforce Attack")){
            AttackTitle.setText("Bruteforce Setup");
            PasswordLength.setVisible(true);
            PasswordAttacks_BruteForcePasswordLength.setVisible(true);
            PasswordAttacks_BruteForceUppercase.setVisible(true);
            PasswordAttacks_BruteForceSpecialChars.setVisible(true);
            PasswordAttacks_BruteForceLowercase.setVisible(true);
            PasswordAttacks_BruteForceNumbers.setVisible(true);
            PasswordAttacks_DictionaryFile.setVisible(false);
            PasswordAttacks_OpenDictionaryFileButton.setVisible(false);
        } else if(type.toString().equals("Dictionary Attack")){
            AttackTitle.setText("Dictionary Setup");
            PasswordLength.setVisible(false);
            PasswordAttacks_BruteForcePasswordLength.setVisible(false);
            PasswordAttacks_BruteForceUppercase.setVisible(false);
            PasswordAttacks_BruteForceSpecialChars.setVisible(false);
            PasswordAttacks_BruteForceLowercase.setVisible(false);
            PasswordAttacks_BruteForceNumbers.setVisible(false);
            PasswordAttacks_DictionaryFile.setVisible(true);
            PasswordAttacks_OpenDictionaryFileButton.setVisible(true);
        }

    }

    @FXML
    void PasswordAttacks_DictionaryFileOpenClicked(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Dictionary File");
        var file = chooser.showOpenDialog(PasswordAttacks_DictionaryFile.getScene().getWindow());
        System.out.println(file);
        if (file != null) {
            PasswordAttacks_DictionaryFile.setText(file.toString());
            this.PasswordAttacks_DictionaryAttack.ClearDictionary();
            this.PasswordAttacks_DictionaryAttack.AddDictionaryFile(file);
        }
    }



    @FXML
    void PasswordAttacks_BruteForceConfigChange(InputMethodEvent event) {
        int passlen = (int) PasswordAttacks_BruteForcePasswordLength.getValue();
        PasswordAttacks_BruteForceAttack.ConfigureAttack(
                passlen,
                PasswordAttacks_BruteForceUppercase.isSelected(),
                PasswordAttacks_BruteForceLowercase.isSelected(),
                PasswordAttacks_BruteForceSpecialChars.isSelected(),
                PasswordAttacks_BruteForceNumbers.isSelected()
        );

    }

    @FXML
    void PasswordAttacks_BruteForceConfigChangeIncl(MouseEvent event) {
        int passlen = (int) PasswordAttacks_BruteForcePasswordLength.getValue();
        PasswordAttacks_BruteForceAttack.ConfigureAttack(
                passlen,
                PasswordAttacks_BruteForceUppercase.isSelected(),
                PasswordAttacks_BruteForceLowercase.isSelected(),
                PasswordAttacks_BruteForceSpecialChars.isSelected(),
                PasswordAttacks_BruteForceNumbers.isSelected()
        );

    }

    @FXML
    void PasswordAttacks_StartAttack(MouseEvent event) {

        PasswordAttacks_ClientOutput.clear();
        Traversed.setVisible(true);

        var attack = PasswordAttacks_AttackMode.getSelectionModel().selectedItemProperty().getValue();

        if(PasswordAttackValidityCheck(attack)){
            PasswordAttacks_AttackButton.setText("Stop Attack");
            attack.SetTargetPort(Integer.parseInt(PasswordAttacks_ClientTargetPort.getText()));
            attack.SetTargetIP(PasswordAttacks_ClientTargetIP.getText());
            attack.StartAttack();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SyncProgress();
                }
            }).start();
        } else {
            System.out.println("Validity check failed");
        }

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
        PasswordAttacks_AttackButton.setDisable(false);
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


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                PasswordAttacks_AttackButton.setText("Start Attack");
                long sec = r.Duration;
                long minute = sec/60;

                if(sec<60){
                    PasswordAttacks_ClientOutput.setText(
                            PasswordAttacks_ClientOutput.getText() + ("Time elapsed: "+Long.toString(sec)+"s\n")
                    );
                } else {
                    PasswordAttacks_ClientOutput.setText(
                            PasswordAttacks_ClientOutput.getText() + ("Time elapsed: "+Long.toString(minute)+"m\n")
                    );
                }

                Traversed.setVisible(false);

            }
        });
    }

    private void SyncProgress() {
        try {
            Traversed.setProgress(0);
            var attack = PasswordAttacks_AttackMode.getSelectionModel().selectedItemProperty().getValue();
            while (attack.IsAttackRunning()) {
                Traversed.setProgress(attack.GetProgress());
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.out.println("Sync Progress Error");
        }
    }


    private Boolean PasswordAttackValidityCheck(Attacker checkAttack){

        Boolean Validity = false;

        if(!PasswordAttacks_ServerPort.getText().isEmpty() || !PasswordAttacks_ServerPassword.getText().isEmpty() ||
        !PasswordAttacks_ClientTargetIP.getText().isEmpty() || !PasswordAttacks_ClientTargetPort.getText().isEmpty()){

            if(checkAttack.toString().equals("Bruteforce Attack") && PasswordAttacks_BruteForceUppercase.isSelected() ||
                    PasswordAttacks_BruteForceSpecialChars.isSelected() || PasswordAttacks_BruteForceLowercase.isSelected()
                    || PasswordAttacks_BruteForceNumbers.isSelected()){

                Validity = true;

            } else if (checkAttack.toString().equals("Dictionary Attack") && !PasswordAttacks_DictionaryFile.getText().isEmpty()){

                Validity = true;

            } else {
                ShowMessageAlert("Please fill all fields","Password Attacks","WARNING");
            }
        } else {
            ShowMessageAlert("Please fill all fields","Password Attacks","WARNING");
        }

        return Validity;
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
    private Label chars;


    @FXML
    void TextStega_DecodeClicked(MouseEvent event) {

        try {
            if (TextStegaEnableEncryption.isSelected()) {
                TextStega_Secret.setText(
                        Cryptography.Steganography.TextStega.Decode(
                                TextStega_Encoded.getText(),
                                SteganographyCiperPassword.getText()
                        )
                );
            } else {
                TextStega_Secret.setText(
                        Cryptography.Steganography.TextStega.Decode(TextStega_Encoded.getText())
                );
            }
        } catch (Exception e) {
            System.out.println("Decode Error");
        }
    }

    @FXML
    void TextStega_EncodeClicked(MouseEvent event) {

        var max_char_count = Cryptography.Steganography.TextStega.CountPossibleCharacters(TextStega_Original.getText());
        int val = Cryptography.Steganography.TextStega.CountPossibleCharacters(TextStega_Original.getText())-1;
        if(val>0){
            if (!Cryptography.Steganography.TextStega.CheckIfEncodeable(
                    TextStega_Original.getText(),
                    TextStega_Secret.getText()
            )){
                ShowMessageAlert("This message can only encode a secret message of " + max_char_count + " character(s). Please add more spaces.","Stream Cipher + Steganography","WARNING");
                return;
            }

        } else {
            ShowMessageAlert("Woops, you forgot to write the messages","Stream Cipher + Steganography","WARNING");
        }

        try {


            if (TextStegaEnableEncryption.isSelected()) {
                TextStega_Encoded.setText(
                        Cryptography.Steganography.TextStega.Encode(
                                TextStega_Original.getText(),
                                TextStega_Secret.getText(),
                                SteganographyCiperPassword.getText()
                        ));

            } else {
                TextStega_Encoded.setText(
                        Cryptography.Steganography.TextStega.Encode(
                                TextStega_Original.getText(),
                                TextStega_Secret.getText()
                        ));

            }
            TextStega_Original.setText("");
            TextStega_Secret.setText("");
            SteganographyCiperPassword.setText("");
            chars.setVisible(false);
        } catch (Exception e) {
            System.out.println("Oh noes");
        }
    }

    @FXML
    void CharCount(KeyEvent event) {
        chars.setVisible(true);
        int val = 0;
        val = Cryptography.Steganography.TextStega.CountPossibleCharacters(TextStega_Original.getText())-1;
        chars.textProperty().bind(TextStega_Secret.textProperty()
                    .length()
                    .asString("Char: %d / "+Integer.toString(val)));
    }

    @FXML
    void TextStega_Refresh(MouseEvent event){
        TextStega_Original.setText("");
        TextStega_Encoded.setText("");
        TextStega_Secret.setText("");
        SteganographyCiperPassword.setText("");
        chars.setVisible(false);

    }


    /////////////
    // General //
    /////////////
    @FXML
    private void ShowMessageAlert(String message, String title, String type) {
       try {
           Alert alert = new Alert(Alert.AlertType.valueOf(type), message);
           alert.setTitle(title);
           alert.setHeaderText(null);
           alert.showAndWait();
       } catch (Exception e){
           System.out.println("Java doesn't like threads");
       }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Attacker> Modes =
                FXCollections.observableArrayList(
                        PasswordAttacks_BruteForceAttack,
                        PasswordAttacks_DictionaryAttack
                );


        PasswordAttacks_AttackMode.setItems(Modes);
        PasswordAttacks_AttackMode.getSelectionModel().selectFirst();

        PasswordAttacks_DictionaryFile.setVisible(false);
        PasswordAttacks_OpenDictionaryFileButton.setVisible(false);
        PasswordAttacks_AttackButton.setDisable(true);

        PasswordAttacks_DictionaryAttack.SubscribeToDebug(this);
        PasswordAttacks_BruteForceAttack.SubscribeToDebug(this);
        PasswordAttacks_DictionaryAttack.SubscribeToCompletion(this);
        PasswordAttacks_BruteForceAttack.SubscribeToCompletion(this);
        TextStega_Original.setWrapText(true);
        TextStega_Encoded.setWrapText(true);
        TextStega_Secret.setWrapText(true);
        SteganographyCiperPassword.visibleProperty().bind(TextStegaEnableEncryption.selectedProperty());
        Traversed.setVisible(false);

        System.out.println("App Loaded");






    }
}
