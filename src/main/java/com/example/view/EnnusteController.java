package com.example.view;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.example.kayttajanHallinta.KayttajanHallinta;
import com.example.model.Kulu;

/**
 * EnnusteController implements a controller class for Ennuste.fxml.
 * @author hannemsalmi, willeKoodaus, Katanpe, MinaSofi
 */
public class EnnusteController implements ViewController{
	@FXML
	private AnchorPane ap;
	@FXML
	private BorderPane bp;
	@FXML
	private HBox sisalto;
	
	@FXML
	private VBox chartBox;
	@FXML
	private LineChart<String, Number> chart;
	@FXML
	private CategoryAxis vaakaAkseli;
	@FXML
	private NumberAxis pystyAkseli;
	@FXML
	private Label info;
	
	private ViewHandler vh;
	private KayttajanHallinta kayttajanhallinta;
	private List<Kulu> kulut;

	private final String bundleFinnish = "com/example/Bundle_Finnish";
	private final String bundleEnglish = "com/example/Bundle_English";
	
	/**
	 * Initiates EnnusteController when it is opened.
	 * @param ViewHandler The class which controls the view changes and functions.
	 */
	@Override
	public void init(ViewHandler viewHandler) {
		vh = viewHandler;
		if(!(vh.getKieli())) {
			asetaKieli();
		}
		kayttajanhallinta = vh.getKayttajanhallinta();
		kulut = vh.getKontrolleri().getKulut(kayttajanhallinta.lueKayttajaID());
		luoKuluGraph();
	}
	
	/**
	 * A method for changing the language of the graphical user interface.
	 */
	public void asetaKieli() {
		ResourceBundle english = ResourceBundle.getBundle(bundleEnglish);
		chart.setTitle(english.getString("ennusteOtsikko"));
		info.setText(english.getString("info"));
	}
	
	/**
	 * A method which creates a graph of the expenses during the ongoing month.
	 */
	public void luoKuluGraph() {
		ResourceBundle english = ResourceBundle.getBundle(bundleEnglish);
		ResourceBundle finnish = ResourceBundle.getBundle(bundleFinnish);
	
		//päivittää kulut
		kulut = vh.getKontrolleri().getKulut(kayttajanhallinta.lueKayttajaID());
		
		// muuttuja, jolla pidetään kirjaa kulujen kasvusta
		int kulutSumma = 0;
		int lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).getDayOfMonth();
		// luodaan series ennusteen datalle
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		if(vh.getKieli()) {
			series.setName(finnish.getString("kumuloituma") + LocalDate.now().getMonth().getValue() + "." + Integer.toString((LocalDate.now().getYear())));
		} else {
			series.setName(english.getString("kumuloituma") + LocalDate.now().getMonth().getValue() + "." + Integer.toString((LocalDate.now().getYear())));
		}
		
		int currentMonth = LocalDate.now().getMonthValue();
		
		// lajittele kulut päivämäärän mukaan
		Collections.sort(kulut, new Comparator<Kulu>() {
			public int compare(Kulu k1, Kulu k2) {
			    return k1.getPaivamaara().compareTo(k2.getPaivamaara());
			}
		});
		
		// alusta kaksi taulukkoa pitämään sisällään chartin x- ja y-arvot
		double[] xValues = new double[kulut.size()];
		double[] yValues = new double[kulut.size()];
		
		// muuttuja datapointtien määrän seuraamiseksi
		int count = 0;
		
		// käy läpi kulut-lista
		int prevDay = -1;
		int prevCount = -1;

		// Iterate through the expenses
		for (int i = 0; i < kulut.size(); i++) {
		    // Current expense's date
		    LocalDate date = kulut.get(i).getPaivamaara();

		    // Add data to x- and y-arrays if the expense is in the current month
		    if (date.getMonthValue() == currentMonth) {
		        int dayOfMonth = date.getDayOfMonth();

		        if (dayOfMonth == prevDay) {
		            // If the current day is the same as the previous day,
		            // update the existing data point instead of creating a new one
		            kulutSumma += kulut.get(i).getSumma();
		            yValues[prevCount] = kulutSumma;
		        } else {
		            // If the current day is different, create a new data point
		            xValues[count] = dayOfMonth;
		            kulutSumma += kulut.get(i).getSumma();
		            yValues[count] = kulutSumma;
		            prevDay = dayOfMonth;
		            prevCount = count;
		            count++;
		        }
		    }
		}
			  
		// luodaan ennusteregressio loppukuun kuluille
		SimpleRegression regression = new SimpleRegression();
		
		// lisää x ja y arvot regressiomalliin jokaiselle datapisteelle
		for (int i = 0; i < xValues.length; i++) {
		    regression.addData(xValues[i], yValues[i]);
		}
		
		//Luodaan datapointit, jotka näytetään taulukon x- ja y-akseleilla 
		for (int i = 1; i <= lastDayOfMonth; i++) {
			
		    boolean found = false;
		    // käy läpi kaikki kulut tiettynä kuukautena
		    for (int j = 0; j < count; j++) {
		        // tiettynä päivänä on kulu
		        if ((int) xValues[j] == i) {
		            String dayOfMonth = String.format("%02d.", (int) xValues[j]);
		            series.getData().add(new XYChart.Data<>(dayOfMonth, yValues[j]));
		            found = true;
		            break;
		        }
		    }
		    // ei kuluja tiettynä päivänä
		    if (!found) {
		        String dayOfMonth = String.format("%02d.", i);
		        series.getData().add(new XYChart.Data<>(dayOfMonth, 0));
		    }
		}
		
		// lisää series taulukkoon
		chart.getData().add(series);
		
		// laske regressio
		double slope = regression.getSlope();
		double intercept = regression.getIntercept();
		
		// luo uusi series extrapolated datalle
		XYChart.Series<String, Number> extrapolationSeries = new XYChart.Series<>();
		if(vh.getKieli()) {
			extrapolationSeries.setName(finnish.getString("ennusteLoppukuu"));
		} else {
			extrapolationSeries.setName(english.getString("ennusteLoppukuu"));
		}
		
		// lisää extrapolated datapisteet seriesiin käyttäen slopea ja muokkaa regressio-viivaa niin että trendi näkyy selkeästi tulevaisuudessa
		for (int i = 1; i <= lastDayOfMonth; i++) {
		    String dayOfMonth = String.format("%02d.", i);
		    extrapolationSeries.getData().add(new XYChart.Data<>(dayOfMonth, slope * i + intercept));
		}
		
		// lisää extrapolated series taulukkoon
		chart.getData().add(extrapolationSeries);
			
		// uusi series näyttää max budjetin arvon
		double maxBudget = kayttajanhallinta.getKirjautunutKayttaja().getMaksimibudjetti();
		XYChart.Series<String, Number> maxBudgetSeries = new XYChart.Series<>();
		if(vh.getKieli()) {
			maxBudgetSeries.setName(finnish.getString("maxBudjetti"));
		} else {
			maxBudgetSeries.setName(english.getString("maxBudjetti"));
		}
		maxBudgetSeries.getData().add(new XYChart.Data<>(String.format("%02d.", 1), maxBudget));
		maxBudgetSeries.getData().add(new XYChart.Data<>(String.format("%02d.", lastDayOfMonth), maxBudget));
		//lisää max budjetti taulukkoon
		chart.getData().add(maxBudgetSeries);		
	}
}
