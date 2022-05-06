package dni.com.retaguardanuvem;

public class Produto {

    String codigo;
    String nomepro;
    String descpro;
    String image;

    public Produto(String codigo, String nomepro, String descpro, String image) {
        this.codigo = codigo;
        this.nomepro = nomepro;
        this.descpro = descpro;
        this.image = image;
    }

    public Produto(){

    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNomepro(String nomepro) {
        this.nomepro = nomepro;
    }

    public void setDescpro(String descpro) {
        this.descpro = descpro;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNomepro() {

        return nomepro;
    }

    public String getDescpro() {

        return descpro;
    }

    public String getImage() {

        return image;
    }
}
