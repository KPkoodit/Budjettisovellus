package com.example.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.dataAccessObjects.KategoriaDao;
import com.example.dataAccessObjects.KayttajaDao;
import com.example.dataAccessObjects.KuluDao;
import com.example.model.Kategoria;
import com.example.model.Kayttaja;
import com.example.model.Kulu;
import com.example.view.IGUI;

public class Kontrolleri implements IKontrolleri {
	private KategoriaDao kategoriaDao = new KategoriaDao();
	private KayttajaDao kayttajaDao = new KayttajaDao();
	private KuluDao kuluDao = new KuluDao();
	private Kategoria kategoria;
	private Kayttaja kayttaja;
	private Kulu kulu;
	
	public Kontrolleri(IGUI gui) {
	}
	
	public List<Kulu> getKulut(int kayttajaid) {
		return kuluDao.haeKulut(kayttajaid);
	}
	
	public Kulu getKulu(int kuluId) {
		return kuluDao.haeKulu(kuluId);
	}

	@Override
	public void lisaaKategoria(String nimi, String omistaja) {
		kategoria = new Kategoria(nimi, omistaja);
		System.out.println(kategoria.getNimi());
		kategoriaDao.lisaaKategoria(kategoria);
	}
	
	@Override
	public void paivitaBudjetti(int kayttajaID, double budjetti) {
		kayttajaDao.paivitaBudjetti(kayttajaID, budjetti);
	}

	@Override
	public void lisaaKulu(String nimi, double hinta, LocalDate paivamaara, Kategoria kategoria, Kayttaja kayttaja, String kuvaus) {
		kulu = new Kulu(nimi, hinta, paivamaara, kategoria, kayttaja, kuvaus);
		kuluDao.lisaaKulu(kulu);
	}
	
	public Kayttaja getKayttaja(int kayttajaid) {
		return kayttajaDao.haeKayttajat(kayttajaid);
	}
	

	public void poistaKayttajanTiedot(int kayttajaid) {
		kayttajaDao.poistaKayttajanTiedot(kayttajaid);	
	}
	
	public void lisaaKayttaja(String nimi, double budjetti) {
		kayttaja = new Kayttaja(nimi, budjetti);
		System.out.println(kayttaja);
		kayttajaDao.lisaaKayttaja(kayttaja);
	}
	
	public List<String> getKayttajat(){
		List<String> kayttajaNimet = new ArrayList<String>();
		List<Kayttaja> kayttajaObjektit = kayttajaDao.haeKayttajaLista();
		for(Kayttaja k : kayttajaObjektit) {
			kayttajaNimet.add(k.getNimimerkki());
		}
		return kayttajaNimet;
	}

	public int getKayttajaID(String kayttajanimi){
		int etsittyID = -1;
		List<Kayttaja> kayttajaObjektit = kayttajaDao.haeKayttajaLista();
		for(Kayttaja k : kayttajaObjektit) {
			if(k.getNimimerkki().equals(kayttajanimi)) {
				etsittyID = k.getKayttajaID();
			}
		}
		return etsittyID;
	}
	
	public List<String> getKategorianimet(String omistaja) {
		List<String> kategoriaNimet = new ArrayList<String>();
		List<Kategoria> kategoriaObjektit = kategoriaDao.haeKategoriaLista();
		for(Kategoria k : kategoriaObjektit) {
			if(k.getOmistaja().equals(omistaja)) {
				kategoriaNimet.add(k.getNimi());
			}
		}
		return kategoriaNimet;
	}
	
	public List<Kategoria> getKategoriat(String omistaja) {
		List<Kategoria> kategoriat = new ArrayList<Kategoria>();
		List<Kategoria> kaikkikategoriat = kategoriaDao.haeKategoriaLista();
		for(Kategoria k : kaikkikategoriat) {
			if(k.getOmistaja().equals(omistaja)) {
				kategoriat.add(k);
			}
		}
		return kategoriat;
	}
	
	public Kategoria getKategoria(String nimi, String omistaja) {
		List<Kategoria> kategoriaObjektit2 = kategoriaDao.haeKategoriaLista();
		Kategoria etsitty = null;
		for(Kategoria k : kategoriaObjektit2) {
			if(k.getNimi().equals(nimi) && k.getOmistaja().equals(omistaja)) {
				etsitty = k;
			}
		}
		return etsitty;
	}
	
	public boolean muokkaaKulua(int id, Double summa, String nimi, String kuvaus) {
		kuluDao.muutaKulu(id, summa, nimi, kuvaus);
		return true;
	}
	
	public boolean muokkaaKategoriaa(int id, String nimi) {
		kategoriaDao.muutaKategoria(id, nimi);
		return true;
	}
	
	public boolean muutaKulunKategoria(int kuluId, Kategoria uusiKategoria) {
		kuluDao.muutaKulunKategoria(kuluId, uusiKategoria);
		return true;
	}
	
	public boolean poistaKulu(int id) {
		kuluDao.poistaKulu(id);
		return true;
	}
	
	public boolean poistaKategoria(int id, Kayttaja kayttaja) {
		Kategoria poistettava = kategoriaDao.haeKategoriat(id);
		Kategoria yleinen = null;
		List<Kulu> kulut = kuluDao.haeKulut(kayttaja.getKayttajaID());
		List<Kategoria> kategoriat = kategoriaDao.haeKategoriaLista();
		
		for(Kategoria kategoria : kategoriat) {
			if(kategoria.getNimi().equals("Yleinen")) {
				yleinen = kategoria;
			}
		}
				
		for(Kulu kulu : kulut) {
			if(kulu.getKategoria().equals(poistettava)) {
				kuluDao.muutaKulunKategoria(kulu.getKuluID(), yleinen);
			}
		}
		
		kategoriaDao.poistaKategoria(id);
		return true;
	}

	public void setKategoriaDao(KategoriaDao kategoriaDao) {
		this.kategoriaDao = kategoriaDao;
	}
	
	public void setKayttajaDao(KayttajaDao kayttajaDao) {
		this.kayttajaDao = kayttajaDao;
	}
	
	public void setKuluDao(KuluDao kuluDao) {
		this.kuluDao = kuluDao;
	}
}