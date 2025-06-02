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
                    try {
                        transaction = session.beginTransaction();

                        // Unos novog polaznika
                        System.out.print("Unesite ime polaznika: ");
                        String ime = scanner.nextLine().trim();
                        System.out.print("Unesite prezime polaznika: ");
                        String prezime = scanner.nextLine().trim();
                        if (ime.isEmpty()||prezime.isEmpty()) {
                            System.out.println("Greška: ime  ili prezime ne smije biti prazno!");
                            return;
                        }


                        Polaznik polaznik = new Polaznik();
                        polaznik.setIme(ime);
                        polaznik.setPrezime(prezime);

                        session.save(polaznik);
                        transaction.commit();
                        System.out.println("Polaznik dodan.");

                    } catch (Exception e) {
                        if (transaction != null) transaction.rollback();
                        System.out.println("Greška prilikom dodavanja polaznika: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        transaction = session.beginTransaction();

                        // Unos novog programa obrazovanja
                        System.out.print("Unesite naziv programa: ");
                        String nazivPrograma = scanner.nextLine().trim();
                        System.out.print("Unesite broj CSVET bodova: ");
                        String bodoviInput = scanner.nextLine().trim();
                        if (nazivPrograma.isEmpty()||bodoviInput.isEmpty()) {
                            System.out.println("Greška: Naziv programa  ili broj CSVET bodova  ne smije biti prazan");
                            return;
                        }
                        int bodovi;
                        try {
                            bodovi = Integer.parseInt(bodoviInput);
                            if (bodovi <= 0) {
                                System.out.println("Greška: Broj CSVET bodova mora biti veći od nule!");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Greška: Broj CSVET bodova mora biti cijeli broj!");
                            return;
                        }

                        ProgramObrazovanja program = new ProgramObrazovanja();
                        program.setNaziv(nazivPrograma);
                        program.setCSvet(bodovi);

                        session.save(program);
                        transaction.commit();
                        System.out.println("Program obrazovanja uspješno dodan.");

                    } catch (Exception e) {
                        if (transaction != null) transaction.rollback();
                        System.out.println("Greška prilikom dodavanja programa: " + e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        transaction = session.beginTransaction();

                        // Unos podataka
                        System.out.print("Unesite ID polaznika: ");
                        String idPolaznikInput = scanner.nextLine();
                        if (idPolaznikInput.isEmpty()) {
                            System.out.println("Greška: ID polaznika ne može biti prazan!");
                            return;
                        }
                           int idPolaznik;
                        try {
                             idPolaznik = Integer.parseInt(idPolaznikInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Greška: ID polaznika mora biti broj!");
                            return;
                        }


                        System.out.print("Unesite ID programa: ");
                        String idProgramInput = scanner.nextLine();
                        if (idProgramInput.isEmpty()) {
                            System.out.println("Greška: ID programa ne može biti prazan!");
                            return;
                        }

                        int idProgram;
                        try {
                            idProgram = Integer.parseInt(idProgramInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Greška: ID programa mora biti broj!");
                            return;
                        }

                        // Dohvati entitete po ID-ju
                        Polaznik p = session.get(Polaznik.class, idPolaznik);
                        ProgramObrazovanja po = session.get(ProgramObrazovanja.class, idProgram);
                        if (p == null||po==null) {
                            System.out.println("Polaznik ili program pod tim ID ne postoji ");
                            return;
                        }

                        // Kreiraj novi upis
                        Upis upis = new Upis();
                        upis.setPolaznik(p);
                        upis.setProgramObrazovanja(po);

                        // Spremi u bazu
                        session.save(upis);
                        transaction.commit();
                        System.out.println("Polaznik uspješno upisan na program.");

                    } catch (Exception e) {
                        System.out.println("Greška prilikom upisa: " + e.getMessage());
                    }
                    break;



                case 4:
                    System.out.print("Unesite ID upisa koji želite ažurirati: ");
                    String upisIdInput = scanner.nextLine();
                    if (upisIdInput.isEmpty()) {
                        System.out.println("Greška: ID upisa ne može biti prazan!");
                        return;
                    }
                    int upisId;
                    try {
                        upisId = Integer.parseInt(upisIdInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Greška: ID upisa mora biti broj!");
                        return;
                    }

                    System.out.print("Unesite novi ID programa: ");
                    String noviProgramIdInput = scanner.nextLine();
                    if (noviProgramIdInput.isEmpty()) {
                        System.out.println("Greška: ID programa ne može biti prazan!");
                        return;
                    }
                    int noviProgramId;
                    try {
                        noviProgramId = Integer.parseInt(noviProgramIdInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Greška: ID programa mora biti broj!");
                        return;
                    }

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

                    System.out.print("Unesite ID programa obrazovanja: ");
                    String programIdInput = scanner.nextLine();
                    if (programIdInput.isEmpty()) {
                        System.out.println("Greška: ID programa ne može biti prazan!");
                        break;
                    }
                    int programId;
                    try {
                        programId = Integer.parseInt(programIdInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Greška: ID programa mora biti broj!");
                        break;
                    }

                    System.out.println("Polaznici na programu:");
                    System.out.printf("%-20s %-20s %-30s %-10s\n", "Ime", "Prezime", "Program", "CSVET");
                    System.out.println("-------------------------------------------------------------------------------");
                    try {
                        // dohvati upis
                        List<Upis> upisi = session.createQuery(
                                        "FROM Upis u WHERE u.programObrazovanja.programObrazovanjaID = :programId",
                                        Upis.class)
                                .setParameter("programId", programId)
                                .list();

                        if (upisi.isEmpty()) {
                            System.out.println("Nema polaznika na ovom programu.");
                        } else {
                            for (Upis upis : upisi) {
                                String imePrint = upis.getPolaznik().getIme();
                                String prezimePrint = upis.getPolaznik().getPrezime();
                                String nazivPrograma = upis.getProgramObrazovanja().getNaziv();
                                int csvet = upis.getProgramObrazovanja().getCSvet();
                                System.out.printf("%-20s %-20s %-30s %-10d\n",
                                        imePrint, prezimePrint, nazivPrograma, csvet);
                            }
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




