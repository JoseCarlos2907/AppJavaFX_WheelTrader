package com.iesfernandoaguilar.perezgonzalez.model;

public class Usuario {
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

    public Usuario() {
        this.idUsuario = -1L;
        this.nombreUsuario = "";
        this.correo = "";
        this.correoPP = "";
        this.rol = "USUARIO";
        this.estado = "ACTIVO";
    }
    

    public Usuario(String nombre, String apellidos, String dni, String direccion, String nombreUsuario, String contrasenia,
            String correo, String correoPP, String salt, boolean moderador) {
        this.idUsuario = -1L;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.direccion = direccion;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.correo = correo;
        this.correoPP = correoPP;
        this.rol = moderador ? "MODERADOR" : "USUARIO";
        this.estado = "ACTIVO";
        this.salt = salt;
    }


    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuaroio) {
        this.nombreUsuario = nombreUsuaroio;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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


    public String getDireccion() {
        return direccion;
    }


    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}