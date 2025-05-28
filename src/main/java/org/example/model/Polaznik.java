package org.example.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Polaznik")
public class Polaznik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PolaznikID")  // EXACT MATCH
    private int polaznikID;

    @Column(name = "Ime")
    private String ime;

    @Column(name = "Prezime")
    private String prezime;

    @OneToMany(mappedBy = "polaznik", cascade = CascadeType.ALL)
    private List<Upis> upisi;

    // Getters and setters
    public int getPolaznikID() {
        return polaznikID;
    }
    public void setPolaznikID(int polaznikID) {
        this.polaznikID = polaznikID;
    }

    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public List<Upis> getUpisi() {
        return upisi;
    }
    public void setUpisi(List<Upis> upisi) {
        this.upisi = upisi;
    }
}