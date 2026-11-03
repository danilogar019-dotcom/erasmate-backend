package com.erasmate.erasmate_backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "lugar")
public class Lugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lugar")
    private Long idLugar;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "precio_aprox")
    private Double precioAprox;

    // Muchos lugares pertenecen a una ciudad
    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    // Muchos lugares pertenecen a una categoría
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    // Constructor vacío: obligatorio para JPA
    public Lugar() {
    }

    public Lugar(String nombre, String descripcion, Double precioAprox, Ciudad ciudad, Categoria categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioAprox = precioAprox;
        this.ciudad = ciudad;
        this.categoria = categoria;
    }

    public Long getIdLugar() { return idLugar; }
    public void setIdLugar(Long idLugar) { this.idLugar = idLugar; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecioAprox() { return precioAprox; }
    public void setPrecioAprox(Double precioAprox) { this.precioAprox = precioAprox; }

    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
