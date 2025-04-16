package com.iesfernandoaguilar.perezgonzalez.model;

import java.util.List;
import java.util.Set;

public class Usuario {
    // *-- Atributos --* //
    private Long idUsuario;

    private String nombre;

    private String apellidos;

    private String dni;

    private String direccion;

    private String nombreUsuario;

    private String contrasenia;

    private String correo;

    private String correoPP;

    private String rol;

    private String estado;

    private String salt;

    // *-- Relaciones --* //
    private List<Reporte> reportesEnviados;

    private List<Reporte> reportesRecibidos;

    private List<Notificacion> notificacionesEnviadas;

    private List<Notificacion> notificacionesRecibidas;

    private List<Valoracion> valoracionesEnviadas;

    private List<Valoracion> valoracionesRecibidas;

    private Set<Anuncio> anunciosGuardados;

    private List<Anuncio> anunciosPublicados;

    private List<Venta> ventas;

    private List<Venta> compras;

    private List<Reunion> reunionesRecibidas;

    private List<Reunion> reunionesOfrecidas;


    // *-- Constructores --* //
    public Usuario() {}


    // *-- Getters y Setters --* //
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoPP() {
        return correoPP;
    }

    public void setCorreoPP(String correoPP) {
        this.correoPP = correoPP;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Reporte> getReportesEnviados() {
        return reportesEnviados;
    }

    public void setReportesEnviados(List<Reporte> reportesEnviados) {
        this.reportesEnviados = reportesEnviados;
    }

    public void addReporteEnviado(Reporte reporte) {
        this.reportesEnviados.add(reporte);
    }

    public List<Reporte> getReportesRecibidos() {
        return reportesRecibidos;
    }

    public void setReportesRecibidos(List<Reporte> reportesRecibidos) {
        this.reportesRecibidos = reportesRecibidos;
    }

    public void addReporteRecibido(Reporte reporte) {
        this.reportesRecibidos.add(reporte);
    }

    public List<Notificacion> getNotificacionesEnviadas() {
        return notificacionesEnviadas;
    }

    public void setNotificacionesEnviadas(List<Notificacion> notificacionesEnviadas) {
        this.notificacionesEnviadas = notificacionesEnviadas;
    }

    public void addNotificacionEnviada(Notificacion notificacion) {
        this.notificacionesEnviadas.add(notificacion);
    }

    public List<Notificacion> getNotificacionesRecibidas() {
        return notificacionesRecibidas;
    }

    public void setNotificacionesRecibidas(List<Notificacion> notificacionesRecibidas) {
        this.notificacionesRecibidas = notificacionesRecibidas;
    }

    public void addNotificacionRecibida(Notificacion notificacion) {
        this.notificacionesRecibidas.add(notificacion);
    }

    public List<Valoracion> getValoracionesEnviadas() {
        return valoracionesEnviadas;
    }

    public void setValoracionesEnviadas(List<Valoracion> valoracionesEnviadas) {
        this.valoracionesEnviadas = valoracionesEnviadas;
    }

    public void addValoracionEnviada(Valoracion valoracion) {
        this.valoracionesEnviadas.add(valoracion);
    }

    public List<Valoracion> getValoracionesRecibidas() {
        return valoracionesRecibidas;
    }

    public void setValoracionesRecibidas(List<Valoracion> valoracionesRecibidas) {
        this.valoracionesRecibidas = valoracionesRecibidas;
    }

    public void addValoracionRecibida(Valoracion valoracion) {
        this.valoracionesRecibidas.add(valoracion);
    }

    public Set<Anuncio> getAnunciosGuardados() {
        return anunciosGuardados;
    }

    public void setAnunciosGuardados(Set<Anuncio> anunciosGuardados) {
        this.anunciosGuardados = anunciosGuardados;
    }

    public void addAnuncioGuardado(Anuncio anuncio) {
        this.anunciosGuardados.add(anuncio);
    }

    public List<Anuncio> getAnunciosPublicados() {
        return anunciosPublicados;
    }

    public void setAnunciosPublicados(List<Anuncio> anunciosPublicados) {
        this.anunciosPublicados = anunciosPublicados;
    }

    public void addAnuncioPublicado(Anuncio anuncio) {
        this.anunciosPublicados.add(anuncio);
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    public void addVenta(Venta venta) {
        this.ventas.add(venta);
    }

    public List<Venta> getCompras() {
        return compras;
    }

    public void setCompras(List<Venta> compras) {
        this.compras = compras;
    }

    public void addCompra(Venta venta) {
        this.compras.add(venta);
    }

    public List<Reunion> getReunionesRecibidas() {
        return reunionesRecibidas;
    }

    public void setReunionesRecibidas(List<Reunion> reunionesRecibidas) {
        this.reunionesRecibidas = reunionesRecibidas;
    }

    public void addReunionRecibida(Reunion reunion) {
        this.reunionesRecibidas.add(reunion);
    }

    public List<Reunion> getReunionesOfrecidas() {
        return reunionesOfrecidas;
    }

    public void setReunionesOfrecidas(List<Reunion> reunionesOfrecidas) {
        this.reunionesOfrecidas = reunionesOfrecidas;
    }

    public void addReunionOfrecida(Reunion reunion) {
        this.reunionesOfrecidas.add(reunion);
    }

    // *-- Métodos --* //
    public void parse(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombre = usuario.getNombre();
        this.apellidos = usuario.getApellidos();
        this.dni = usuario.getDni();
        this.direccion = usuario.getDireccion();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.contrasenia = usuario.getContrasenia();
        this.correo = usuario.getCorreo();
        this.correoPP = usuario.getCorreoPP();
        this.rol = usuario.getRol();
        this.estado = usuario.getEstado();
        this.salt = usuario.getSalt();

        this.reportesEnviados = null;
        this.reportesRecibidos = null;
        this.notificacionesEnviadas = null;
        this.notificacionesRecibidas = null;
        this.valoracionesEnviadas = null;
        this.valoracionesRecibidas = null;
        this.anunciosGuardados = null;
        this.anunciosPublicados = null;
        this.ventas = null;
        this.compras = null;
        this.reunionesRecibidas = null;
        this.reunionesOfrecidas = null;
    }
}
