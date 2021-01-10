package it.unibas.mediapesata.persistenza;

import it.unibas.mediapesata.modello.Esame;
import it.unibas.mediapesata.modello.Studente;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOStudente {
    
    private static final Logger logger = LoggerFactory.getLogger(DAOStudente.class.getName());

    /* ******************************************
     *               Salvataggio
     * ****************************************** */

    private DAOStudente(){}

    public static void salva(Studente studente, String nomeFile) throws DAOException {
        PrintWriter flusso = null;
        try {
            FileWriter fileWriter = new FileWriter(nomeFile);
            flusso = new PrintWriter(fileWriter);
            flusso.println("Studente:");
            flusso.println(studente.toSaveString());
            flusso.println("--------------------------------------");
            salvaEsami(studente, flusso);
        } catch (IOException ioe) {
            logger.error(ioe.toString());
            throw new DAOException(ioe);
        } finally {
            if (flusso != null) {
                flusso.close();
            }
        }
    }
    
    private static void salvaEsami(Studente studente, PrintWriter flusso) {
        for (int i = 0; i < studente.getNumeroEsami(); i++) {
            flusso.println(studente.getEsame(i).toSaveString());            
        }
    }

    /* ******************************************
     *               Caricamento
     * ****************************************** */

    public static Studente carica(String nomeFile) throws DAOException {
        Studente studente = null;
        BufferedReader flusso = null;
        try {
            FileReader fileReader = new FileReader(nomeFile);
            flusso = new BufferedReader(fileReader);
            studente = estraiDatiStudente(flusso);
            caricaEsami(studente, flusso);
        } catch (Exception e) {
            logger.error(e.toString());
            throw new DAOException(e);
        } finally {
            try {
                if (flusso != null) {
                    flusso.close();
                }
            } catch (IOException ioe) {}
        }
        return studente;
    }
        
    private static Studente estraiDatiStudente(BufferedReader flusso) 
                   throws IOException {
        flusso.readLine();
        String lineaStudente = flusso.readLine();
        StringTokenizer tokenizer = new StringTokenizer(lineaStudente, "|");
        String cognome = tokenizer.nextToken().trim();
        String nome = tokenizer.nextToken().trim();
        int matricola = Integer.parseInt(tokenizer.nextToken().trim());
        Studente studente = new Studente(nome, cognome, matricola);
        if (logger.isDebugEnabled()) logger.debug("Dati studente: " + studente);
        return studente;
    }
    
    private static void caricaEsami(Studente studente, BufferedReader flusso) throws IOException {
        flusso.readLine();
        String lineaEsame;
        while ((lineaEsame = flusso.readLine()) != null) {
            studente.addEsame(estraiDatiEsame(lineaEsame, flusso));
        }
        if (logger.isDebugEnabled()) logger.debug("Caricato studente: " + studente);
    }
    
    private static Esame estraiDatiEsame(String lineaEsame, BufferedReader flusso) {
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(lineaEsame, "|");
        String insegnamento = tokenizer.nextToken().trim();
        int crediti = Integer.parseInt(tokenizer.nextToken().trim());
        int voto = Integer.parseInt(tokenizer.nextToken().trim());
        boolean lode = Boolean.valueOf(tokenizer.nextToken().trim());
        Esame esame = new Esame(insegnamento, voto, lode, crediti);
        if (logger.isDebugEnabled()) logger.debug("Estratto esame: " + esame);
        return esame;
    }
    
}
