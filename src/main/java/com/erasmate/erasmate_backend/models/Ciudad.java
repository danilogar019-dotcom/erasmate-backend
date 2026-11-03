package com.erasmate.erasmate_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ciudad")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long idCiudad;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String pais;

    // Una ciudad tiene muchos lugares
    @OneToMany(mappedBy = "ciudad")
    @JsonIgnore
    private List<Lugar> lugares = new ArrayList<>();

    // Constructor vacío: obligatorio para JPA
    public Ciudad() {
    }

    public Ciudad(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }

    public Long getIdCiudad() { return idCiudad; }
    public void setIdCiudad(Long idCiudad) { this.idCiudad = idCiudad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public List<Lugar> getLugares() { return lugares; }
    public void setLugares(List<Lugar> lugares) { this.lugares = lugares; }
}
