package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Upis")
public class Upis {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "UpisID")
     private int upisId;

     @ManyToOne
     @JoinColumn(name = "IDPolaznik", referencedColumnName = "PolaznikID")
     private Polaznik polaznik;

     @ManyToOne
     @JoinColumn(name = "IDProgramObrazovanja", referencedColumnName = "ProgramObrazovanjaID")
     private ProgramObrazovanja programObrazovanja;

     // Getters and Setters
     public int getUpisId() {
          return upisId;
     }

     public void setUpisId(int upisId) {
          this.upisId = upisId;
     }

     public Polaznik getPolaznik() {
          return polaznik;
     }

     public void setPolaznik(Polaznik polaznik) {
          this.polaznik = polaznik;
     }

     public ProgramObrazovanja getProgramObrazovanja() {
          return programObrazovanja;
     }

     public void setProgramObrazovanja(ProgramObrazovanja programObrazovanja) {
          this.programObrazovanja = programObrazovanja;
     }
}