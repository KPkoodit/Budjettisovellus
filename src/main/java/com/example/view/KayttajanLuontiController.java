package com.example.view;

import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.kayttajanHallinta.KayttajanHallinta;

/**
 * KayttajanLuontiController implements a controller class for KayttajanLuonti.fxml.
 * @author hannemsalmi, willeKoodaus, Katanpe, MinaSofi
 */
public class KayttajanLuontiController implements ViewController {

	@FXML
	private AnchorPane ap;
	@FXML
	private BorderPane bp;
	@FXML
	private VBox sisalto;
	
	@FXML
	private VBox kayttaja;
	@FXML
	private Label kayttajaLabel;
	@FXML
	private TextField kayttajaField;
	
	@FXML
	private VBox budjetti;
	@FXML
	private Label budjettiLabel;
	@FXML
	private TextField budjettiField;
	
	@FXML
	private VBox tallenna;
	@FXML
	private Button tallennaButton;
	
	private ViewHandler vh;
	private KayttajanHallinta kayttajanhallinta;

	private final String bundleEnglish = "com/example/Bundle_English";
	
	/**
	 * Initiates KayttajanLuontiController when it is opened.
	 * @param ViewHandler The class which controls the view changes and functions.
	 */
	@Override
	public void init(ViewHandler viewHandler) {
		vh = viewHandler;
		if(!(vh.getKieli())) {
			asetaKieli();
		}
		kayttajanhallinta = vh.getKayttajanhallinta();
	}
	
	/**
	 * A method for changing the language of the graphical user interface.
	 */
	public void asetaKieli() {
		ResourceBundle english = ResourceBundle.getBundle(bundleEnglish);
		kayttajaLabel.setText(english.getString("kayttaja"));
		budjettiLabel.setText(english.getString("kuukausiBudjetti"));
		tallennaButton.setText(english.getString("luoKayttaja"));
	}
	
	/**
	 * A method used for creating a new user profile.
	 */
	public void luoUusiKayttaja() {
		String username = kayttajaField.getText();
        double budjetti = Double.parseDouble(budjettiField.getText());
        if (!username.isEmpty()) {
        	vh.getKontrolleri().lisaaKayttaja(username, budjetti);
            vh.getKontrolleri().lisaaKategoria("Yleinen", username);
            
            int haluttuId = -1;
            List<String> kayttajat = vh.getKontrolleri().getKayttajat();
            for(int i = 0; i < kayttajat.size(); i++) {
            	if(kayttajat.get(i).equals(username)){
            		haluttuId = (i+1);
            	}           	
            }
            
            kayttajanhallinta.kirjoitaKayttajaID(haluttuId);
            System.out.println(haluttuId);
            kayttajanhallinta.setKirjautunutKayttaja(vh.getKontrolleri().getKayttaja(kayttajanhallinta.lueKayttajaID()));
            Stage stage = vh.getKayttajaStage();
            stage.close();
        }
        vh.naytaEtusivu();
	}
}
