package model;

public class Usuario {
    private Long id;

    private String nombreUsuario;

    private String contrasenia;

    private String salt;

    public Usuario() {}
    
    public Usuario(String nombreUsuario, String contrasenia, String salt) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}