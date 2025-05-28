package org.example.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ProgramObrazovanja")
public class ProgramObrazovanja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProgramObrazovanjaID")
    private int programObrazovanjaID;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "CSVET")
    private int cSvet;

    @OneToMany(mappedBy = "programObrazovanja", cascade = CascadeType.ALL)
    private List<Upis> upisi;


    public int getProgramObrazovanjaID() {
        return programObrazovanjaID;
    }
    public void setProgramObrazovanjaID(int programObrazovanjaID) {
        this.programObrazovanjaID = programObrazovanjaID;
    }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getCSvet() {
        return cSvet;
    }
    public void setCSvet(int cSvet) {
        this.cSvet = cSvet;
    }

    public List<Upis> getUpisi() {
        return upisi;
    }
    public void setUpisi(List<Upis> upisi) {
        this.upisi = upisi;
    }
}