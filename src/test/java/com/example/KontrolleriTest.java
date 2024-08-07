package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.example.controller.Kontrolleri;
import com.example.dataAccessObjects.KategoriaDao;
import com.example.dataAccessObjects.KayttajaDao;
import com.example.dataAccessObjects.KuluDao;
import com.example.model.Kategoria;
import com.example.model.Kayttaja;
import com.example.model.Kulu;
import com.example.view.IGUI;

public class KontrolleriTest {

    private Kontrolleri kontrolleri;
    private IGUI gui;
    private KategoriaDao kategoriaDao;
    private KayttajaDao kayttajaDao;
    private KuluDao kuluDao;

    @Before
    public void setUp() {
        gui = mock(IGUI.class);
        kategoriaDao = mock(KategoriaDao.class);
        kayttajaDao = mock(KayttajaDao.class);
        kuluDao = mock(KuluDao.class);
        kontrolleri = new Kontrolleri(gui);
        kontrolleri.setKategoriaDao(kategoriaDao);
        kontrolleri.setKayttajaDao(kayttajaDao);
        kontrolleri.setKuluDao(kuluDao);
    }
    
    @Test
    public void testGetKategorianimet() {
    	String omistaja = "testOmistaja";
    	List<Kategoria> kategoriaObjektit = new ArrayList<>();
    	kategoriaObjektit.add(new Kategoria("testi1", "testi1 omistaja"));
    	kategoriaObjektit.add(new Kategoria("testi2", omistaja));
    	when(kategoriaDao.haeKategoriaLista()).thenReturn(kategoriaObjektit);
    	
    	List<String> result = kontrolleri.getKategorianimet(omistaja);
    	
    	List<String> expected = new ArrayList<>();
    	expected.add("testi2");
    	assertEquals(expected, result);
    }
    
    @Test
    public void testGetKategoria() {
        kontrolleri.getKategoria("kategoria1", "omistaja1");

        verify(kategoriaDao).haeKategoriaLista();
    }
    
    @Test
    public void testLisaaKategoria() {
        String nimi = "Ruoka";
        String omistaja = "Matti";
        Kategoria kategoria = new Kategoria(nimi, omistaja);
        when(kategoriaDao.lisaaKategoria(kategoria)).thenReturn(true);
        kontrolleri.lisaaKategoria(nimi, omistaja);
        verify(kategoriaDao).lisaaKategoria(kategoria);
    }
    
    @Test
    public void testPoistaKategoria() {
    	Kayttaja kayttaja = new Kayttaja("Matti", 500);
        String nimi = "Ruoka";
        Kategoria kategoria = new Kategoria(nimi, kayttaja.getNimimerkki());
        when(kategoriaDao.lisaaKategoria(kategoria)).thenReturn(true);
        when(kategoriaDao.poistaKategoria(kategoria.getKategoriaID())).thenReturn(true);
        kontrolleri.lisaaKategoria(nimi, kayttaja.getNimimerkki());
        boolean poistettiin = kontrolleri.poistaKategoria(kategoria.getKategoriaID(), kayttaja);
        assertTrue(poistettiin);
        verify(kategoriaDao).lisaaKategoria(kategoria);
        verify(kategoriaDao).poistaKategoria(kategoria.getKategoriaID());
    }
    
    @Test
    public void muokkaaKategoriaa() {
    	Kategoria kategoria = new Kategoria("Ruoka", "Matti");
    	
        when(kategoriaDao.muutaKategoria(kategoria.getKategoriaID(), "Juomat")).thenReturn(true);
        boolean success = kontrolleri.muokkaaKategoriaa(kategoria.getKategoriaID(), "Juomat");
        assertTrue(success);
        verify(kategoriaDao).muutaKategoria(kategoria.getKategoriaID(), "Juomat");
    }
    
    @Test
    public void testGetKategoriat() {
    	List<Kategoria> kategoriat = new ArrayList<>();
        Kayttaja kayttaja = new Kayttaja("Matti", 500);
        kategoriat.add(new Kategoria("Ruoka", kayttaja.getNimimerkki()));
        kategoriat.add(new Kategoria("Vaatteet", kayttaja.getNimimerkki()));
        
        when(kategoriaDao.haeKategoriaLista()).thenReturn(kategoriat);
        
        List<Kategoria> palautetutKategoriat = kontrolleri.getKategoriat(kayttaja.getNimimerkki());
        
        assertEquals(kategoriat, palautetutKategoriat);
        verify(kategoriaDao).haeKategoriaLista();
    }
    
    @Test
    public void testLisaaKulu() {
        String nimi = "Ruokaostokset";
        double hinta = 20.0;
        LocalDate paivamaara = LocalDate.now();
        Kategoria kategoria = new Kategoria("Ruoka", "Matti");
        Kayttaja kayttaja = new Kayttaja("Matti", 500.0);
        String kuvaus = "Ostettu kaupasta";
        Kulu kulu = new Kulu(nimi, hinta, paivamaara, kategoria, kayttaja, kuvaus);
        when(kuluDao.lisaaKulu(kulu)).thenReturn(true);
        kontrolleri.lisaaKulu(nimi, hinta, paivamaara, kategoria, kayttaja, kuvaus);
        verify(kuluDao).lisaaKulu(kulu);
    }
    
    @Test
    public void testPoistaKulu() {
    	Kayttaja kayttaja = new Kayttaja("Matti", 500);
    	Kategoria kategoria = new Kategoria("Ruoka", kayttaja.getNimimerkki());
        Kulu kulu = new Kulu("Ostos", 10.0, LocalDate.now(), kategoria, kayttaja, "Ruokaostos");
        
        when(kuluDao.poistaKulu(kulu.getKuluID())).thenReturn(true);
        
        boolean poistettiin = kontrolleri.poistaKulu(kulu.getKuluID());
        
        assertTrue(poistettiin);
        verify(kuluDao).poistaKulu(kulu.getKuluID());
    }
    
    @Test
    public void testGetKulut() {
        List<Kulu> kulut = new ArrayList<>();
        Kayttaja kayttaja = new Kayttaja("Matti", 500);
        Kategoria kategoria = new Kategoria("Ruoka", kayttaja.getNimimerkki());
        kulut.add(new Kulu("Pulla", 2.0, LocalDate.now(), kategoria, kayttaja, "Ruokaostos"));
        kulut.add(new Kulu("Banaani", 1.0, LocalDate.now(), kategoria, kayttaja, "Ruokaostos"));
        
        when(kuluDao.haeKulut(kayttaja.getKayttajaID())).thenReturn(kulut);
        
        List<Kulu> palautetutKulut = kontrolleri.getKulut(kayttaja.getKayttajaID());
        
        assertEquals(kulut, palautetutKulut);
        verify(kuluDao).haeKulut(kayttaja.getKayttajaID());
    }
   
	@Test
	public void testGetKulu() {
		Kulu expectedKulu = new Kulu();
		expectedKulu.setKuluID(1);
		expectedKulu.setSumma(10.0);
		
		when(kuluDao.haeKulu(1)).thenReturn(expectedKulu);
		
		Kulu actualKulu = kontrolleri.getKulu(1);
		
		assertEquals(expectedKulu, actualKulu);
	}
    
    @Test
    public void testPaivitaKulu() {
        Kayttaja kayttaja = new Kayttaja("Matti", 500);
        Kategoria kategoria = new Kategoria("Ruoka", kayttaja.getNimimerkki());
        Kulu kulu = new Kulu("Pulla", 2.0, LocalDate.now(), kategoria, kayttaja, "Ruokaostos");
        
        when(kuluDao.muutaKulu(kulu.getKuluID(), 1.5, "Pulla", "Ruokaostos")).thenReturn(true);
        
        boolean paivitettiin = kontrolleri.muokkaaKulua(kulu.getKuluID(), 1.5, "Pulla", "Ruokaostos");
        
        assertTrue(paivitettiin);
        verify(kuluDao).muutaKulu(kulu.getKuluID(), 1.5, "Pulla", "Ruokaostos");
    }

    
    @Test
    public void testMuutaKulunKategoria() {
        Kayttaja kayttaja = new Kayttaja("Matti", 500);
        Kategoria kategoria = new Kategoria("Juoma", kayttaja.getNimimerkki());
        Kulu kulu = new Kulu("Limu", 2.0, LocalDate.now(), kategoria, kayttaja, "Ruokaostos");

        when(kuluDao.muutaKulunKategoria(kulu.getKuluID(), kategoria)).thenReturn(true);
        
        boolean paivitettiin = kontrolleri.muutaKulunKategoria(kulu.getKuluID(), kategoria);
        
        assertTrue(paivitettiin);
        verify(kuluDao).muutaKulunKategoria(kulu.getKuluID(), kategoria);
    }
        
    @Test
    public void testPaivitaBudjetti() {
        int kayttajaID = 1;
        double budjetti = 500.0;
        when(kayttajaDao.paivitaBudjetti(kayttajaID, budjetti)).thenReturn(true);
        kontrolleri.paivitaBudjetti(kayttajaID, budjetti);
        verify(kayttajaDao).paivitaBudjetti(kayttajaID, budjetti);
    }
    
    @Test
    public void testGetKayttajat() {
        List<Kayttaja> kayttajaObjektit = Arrays.asList(
            new Kayttaja("user1", 1000),
            new Kayttaja("user2", 800)
        );
      
        when(kayttajaDao.haeKayttajaLista()).thenReturn(kayttajaObjektit);
        
        List<String> kayttajaNimet = kontrolleri.getKayttajat();
        
        List<String> expected = Arrays.asList("user1", "user2");
        assertEquals(expected, kayttajaNimet);
    }
    
    @Test
    public void testGetKayttaja() {
        int testKayttajaId = 1;
        Kayttaja testKayttaja = new Kayttaja("Testikayttaja", 100.0);
        testKayttaja.setKayttajaID(testKayttajaId);

        when(kayttajaDao.haeKayttajat(testKayttajaId)).thenReturn(testKayttaja);

        Kayttaja resultKayttaja = kontrolleri.getKayttaja(testKayttajaId);

        Assert.assertEquals(testKayttaja, resultKayttaja);
    }
    
    @Test
    public void testLisaaKayttaja() {
        Kayttaja kayttaja = new Kayttaja("Testi", 1000.0);
        
        doNothing().when(kayttajaDao).lisaaKayttaja(kayttaja);
        
        kontrolleri.lisaaKayttaja(kayttaja.getNimimerkki(), kayttaja.getMaksimibudjetti());
        
        verify(kayttajaDao).lisaaKayttaja(kayttaja);
    }
    
    @Test
    public void testPoistaKayttajanTiedot() {
      int kayttajaid = 1;
      when(kayttajaDao.poistaKayttajanTiedot(kayttajaid)).thenReturn(true);
      kontrolleri.poistaKayttajanTiedot(kayttajaid);
      verify(kayttajaDao).poistaKayttajanTiedot(kayttajaid);
    }
}