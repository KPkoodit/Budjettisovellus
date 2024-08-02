package com.example.kayttajanHallinta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.model.Kayttaja;

/**
 * This class is responsible for handling the user profiles and their changes.
 * @autor hannemsalmi, willeKoodaus, Katanpe, MinaSofi
 */
public class KayttajanHallinta {
    private static KayttajanHallinta INSTANCE = null;
    private Kayttaja kirjautunutKayttaja = null;
    private final Path externalFilePath = Paths.get("edellisenKayttajanID.txt");

    private KayttajanHallinta() {
    }

    /**
     * Returns the instance
     * @return KayttajanHallinta instance
     */
    public static KayttajanHallinta getInstance() {
        if (INSTANCE == null)
            INSTANCE = new KayttajanHallinta();

        return INSTANCE;
    }

    public Kayttaja getKirjautunutKayttaja() {
        return kirjautunutKayttaja;
    }

    public void setKirjautunutKayttaja(Kayttaja kirjautunutKayttaja) {
        this.kirjautunutKayttaja = kirjautunutKayttaja;
    }

    /**
     * Writes the ID of the current user into a separate file.
     * @param kayttajaID The ID being written to the separate file.
     */
    public void kirjoitaKayttajaID(int kayttajaID) {
        try {
            // Write the user ID to the external file
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(externalFilePath)))) {
                writer.write(Integer.toString(kayttajaID));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the ID of the previous user from a separate file.
     * @return The ID which have been read from the file.
     */
    public int lueKayttajaID() {
        int kayttajaID = 0;
        if (Files.exists(externalFilePath)) {
            // Read the ID from the external file if it exists
            try (BufferedReader reader = Files.newBufferedReader(externalFilePath)) {
                kayttajaID = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return kayttajaID;
    }
}