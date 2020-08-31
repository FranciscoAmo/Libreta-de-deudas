package com.francisco.aplicaciongestionana;

import java.util.Date;

public class Cliente {
    String nombreCliente;
    int cantidad;
    String fechaconsulta;

    public Cliente(String nombreCliente, int cantidad, String fechaconsulta) {
        this.nombreCliente = nombreCliente;
        this.cantidad = cantidad;
        this.fechaconsulta = fechaconsulta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaconsulta() {


        return fechaconsulta;
    }

    public void setFechaconsulta(String fechaconsulta) {
        this.fechaconsulta = fechaconsulta;
    }
}
