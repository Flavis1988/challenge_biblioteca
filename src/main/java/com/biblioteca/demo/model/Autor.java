package com.biblioteca.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(unique = true)
    private String nombre;

    private int fechaDeNacimiento;

    private int fechaDeFallecido;

    public Autor(){}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeFallecido = datosAutor.fechaDeFallecido();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(int fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public int getFechaDeFallecido() {
        return fechaDeFallecido;
    }

    public void setFechaDeFallecido(int fechaDeFallecido) {
        this.fechaDeFallecido = fechaDeFallecido;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "Autor:" +
                " Nombre: " + nombre  +  "\n" +
                ", Fecha De Nacimiento: " + fechaDeNacimiento +  "\n" +
                ", Fecha De Fallecido: " + fechaDeFallecido;
    }
}

