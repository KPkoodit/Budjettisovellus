package com.example.view;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.example.controller.IKontrolleri;
import com.example.kayttajanHallinta.KayttajanHallinta;

/**
 * EtusivuController implements a controller class for Etusivu.fxml.
 * @author hannemsalmi, willeKoodaus, Katanpe, MinaSofi
 */
public class EtusivuController implements ViewController{
	@FXML
	private AnchorPane ap;
	@FXML
	private BorderPane bp;
	@FXML
	private HBox sisalto;
	@FXML
	private VBox tervetuloa;
	@FXML
	private Label tervetuloaLabel;
	@FXML
	private VBox profiili;
	@FXML
	private Label profiiliLabel;
	@FXML
	private ComboBox<String> profiiliBox;
	@FXML
	private VBox uusiProfiili;
	@FXML
	private Label uusiProfiiliLabel;
	@FXML
	private Button uusiProfiiliButton;
	
	@FXML
	private VBox ostoslista;
	@FXML
	private Label ostoslistaLabel;
	@FXML
	private VBox shoppingListVBox;
	
	@FXML
	private VBox muistilista;
	@FXML
	private Label muistilistaLabel;
	@FXML
	private VBox reminderListVBox;
	
	private ViewHandler vh;
	private IKontrolleri kontrolleri;
	
	KayttajanHallinta kayttajanhallinta;

	private final String bundleFinnish = "com/example/Bundle_Finnish";
	private final String bundleEnglish = "com/example/Bundle_English";
	
	/**
	 * Initiates AsetuksetController when it is opened.
	 * @param ViewHandler The class which controls the view changes and functions.
	 */
	@Override
	public void init(ViewHandler viewHandler) {
		vh = viewHandler;
		if(!(vh.getKieli())) {
			asetaKieli();
		}
		
		kontrolleri = vh.getKontrolleri();
		kayttajanhallinta = vh.getKayttajanhallinta();
		kayttajanhallinta.setKirjautunutKayttaja(kontrolleri.getKayttaja(kayttajanhallinta.lueKayttajaID()));
		paivitaTervehdys();
		initProfiiliBox();
	}
	
	/**
	 * A method for changing the language of the graphical user interface.
	 */
	public void asetaKieli() {
		ResourceBundle english = ResourceBundle.getBundle(bundleEnglish);
		profiiliLabel.setText(english.getString("profiiliValinta"));
		uusiProfiiliLabel.setText(english.getString("luoProfiiliTeksti"));
		uusiProfiiliButton.setText(english.getString("luoProfiili"));
	}
	
	/**
	 * A method for selecting the active profile.
	 */
	public void valitseKayttaja() {
		int valittuKayttaja = profiiliBox.getSelectionModel().getSelectedIndex() +1;
        kayttajanhallinta.kirjoitaKayttajaID(valittuKayttaja);
        kayttajanhallinta.setKirjautunutKayttaja(kontrolleri.getKayttaja(valittuKayttaja));
        System.out.println("Logging in user: " + valittuKayttaja);
        paivitaTervehdys();
        vh.paivitaSisalto();
	}
	
	/**
	 * A method used for creating a new user profile.
	 */
	public void luoUusiKayttaja() {
		vh.luoUusiKayttaja();
		vh.paivitaKayttaja();
		paivitaTervehdys();
	}
	
	/**
	 * A method which updates the welcome message based on the profile active right now.
	 */
	public void paivitaTervehdys() {
		ResourceBundle english = ResourceBundle.getBundle(bundleEnglish);
		ResourceBundle finnish = ResourceBundle.getBundle(bundleFinnish);
		if(vh.getKieli()) {
			tervetuloaLabel.setText(finnish.getString("tervetuloa") + kayttajanhallinta.getKirjautunutKayttaja().getNimimerkki());
		} else {
			tervetuloaLabel.setText(english.getString("tervetuloa") + kayttajanhallinta.getKirjautunutKayttaja().getNimimerkki());
		}
		profiiliBox.getSelectionModel().select(kayttajanhallinta.getKirjautunutKayttaja().getNimimerkki());
	}
	
	/**
	 * Initiates the data in user profile combobox.
	 */
	private void initProfiiliBox() {
		profiiliBox.getItems().addAll(kontrolleri.getKayttajat());
	}
}
