package org.example;


import org.example.model.Polaznik;
import org.example.model.ProgramObrazovanja;
import org.example.model.Upis;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();


        System.out.println("Odaberite opciju:");
        System.out.println("1 - unesi novog polaznika");
        System.out.println("2 - unesi novi program obrazovanja");
        System.out.println("3 - upiši polaznika na program obrazovanja");
        System.out.println("4 - prebaci polaznika iz jednog u drugi program obrazovanja");
        System.out.println("5 - ispisi ime, prezime polaznika, naziv programa obrazovanja, broj CSVET bodova");
        System.out.println("6 - kraj");


        System.out.println("Uspješno ste spojeni na bazu podataka!");
        Scanner scanner = new Scanner(System.in);
        int unos;
        Transaction transaction = null;
        do {
            unos = scanner.nextInt();
            scanner.nextLine();

            switch (unos) {
                case 1:

                    try{
                        transaction = session.beginTransaction();
                    // Unos novog polaznika
                    System.out.print("Unesite ime polaznika: ");
                    String ime = scanner.nextLine();
                    System.out.print("Unesite prezime polaznika: ");
                    String prezime = scanner.nextLine();

                   Polaznik polaznik = new Polaznik();
                    polaznik.setIme(ime);
                    polaznik.setPrezime(prezime);

                    session.save(polaznik);
                        transaction.commit();
                        System.out.println("Polaznik dodan.");
                    } catch (Exception e){
                        System.out.println("Greška prilikom dodavanja polaznika: " + e.getMessage());
                    }
                    break;

                case 2:

                    try{
                        transaction = session.beginTransaction();
                    // Unos novog programa obrazovanja
                    System.out.print("Unesite naziv programa: ");
                    String nazivPrograma = scanner.nextLine();
                    System.out.print("Unesite broj CSVET bodova: ");
                    int bodovi = scanner.nextInt();

                    ProgramObrazovanja program = new ProgramObrazovanja();
                    program.setNaziv(nazivPrograma);
                        program.setCSvet(bodovi);
                    session.save(program);
                        transaction.commit();
                        System.out.println("Program obrazovanja uspješno dodan.");
            } catch (Exception e){
                System.out.println("Greška prilikom dodavanja programa: " + e.getMessage());
            }
                    break;

                case 3:


                    try {
                        transaction = session.beginTransaction();

                        // Unos podataka
                        System.out.print("Unesite ID polaznika: ");
                        int idPolaznik = scanner.nextInt();
                        System.out.print("Unesite ID programa: ");
                        int idProgram = scanner.nextInt();
                        scanner.nextLine();

                        // Dohvati entitete po ID-ju
                        Polaznik p = session.get(Polaznik.class, idPolaznik);
                        ProgramObrazovanja po = session.get(ProgramObrazovanja.class, idProgram);



                        // Kreiraj novi upis
                        Upis upis = new Upis();
                        upis.setPolaznik(p);
                        upis.setProgramObrazovanja(po);

                        // Spremi u bazu
                        session.save(upis);
                        transaction.commit();
                        System.out.println("Entitet uspješno dodan");
                        System.out.println("Polaznik uspješno upisan na program.");

                    } catch (Exception e) {
                        System.out.println("Greška prilikom upisa: " + e.getMessage());
                    }
                    break;



                case 4:
                    System.out.print("Unesite ID upisa koji želite ažurirati: ");
                    int upisId = scanner.nextInt();

                    System.out.print("Unesite novi ID programa: ");
                    int noviProgramId = scanner.nextInt();
                    scanner.nextLine();



                    try {
                        transaction = session.beginTransaction();

                        // Dohvati postojeći upis
                        Upis upis = session.get(Upis.class, upisId);
                        if (upis == null) {
                            System.out.println("Upis s ID " + upisId + " ne postoji.");
                            break;
                        }

                        // Dohvati novi program obrazovanja
                        ProgramObrazovanja noviProgram = session.get(ProgramObrazovanja.class, noviProgramId);
                        if (noviProgram == null) {
                            System.out.println("Program s ID " + noviProgramId + " ne postoji.");
                            break;
                        }

                        // Ažuriraj upis
                        upis.setProgramObrazovanja(noviProgram);
                        session.update(upis);

                        transaction.commit();
                        System.out.println("Polaznik je uspješno prebačen u novi program.");

                    } catch (Exception e) {
                        if (transaction != null) transaction.rollback();
                        System.out.println("Greška prilikom prebacivanja polaznika: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.println("Polaznici i programi:");
                    System.out.printf("%-20s %-20s %-30s %-10s\n", "Ime", "Prezime", "Program", "CSVET");
                    System.out.println("-------------------------------------------------------------------------------");

                    try {
                        // Dohvati sve upise
                        List<Upis> upisi = session.createQuery("FROM Upis", Upis.class).list();

                        for (Upis upis : upisi) {
                            String imePrint = upis.getPolaznik().getIme();
                            String prezimePrint = upis.getPolaznik().getPrezime();
                            String nazivPrograma = upis.getProgramObrazovanja().getNaziv();
                            int csvet = upis.getProgramObrazovanja().getCSvet();

                            System.out.printf("%-20s %-20s %-30s %-10d\n", imePrint, prezimePrint, nazivPrograma, csvet);
                        }
                    } catch (Exception e) {
                        System.out.println("Greška prilikom dohvaćanja podataka: " + e.getMessage());
                    }

                    break;

                case 6:
                    System.out.println("Kraj programa.");
                    break;

                default:
                    System.out.println("Nepoznata opcija. Molimo pokušajte ponovo.");
            }
        } while (unos != 6);

        session.close();
        sessionFactory.close();
        scanner.close();
    }
}




