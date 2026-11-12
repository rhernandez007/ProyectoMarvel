package marvelstratego;

public enum Modalidad{
    CLASICO("Clásico (fichas ocultas)"),
    TUTORIAL("Tutorial (fichas visibles)");

    private String texto;

    Modalidad(String texto){
        this.texto = texto;
    }

    public String getTexto(){
        return texto;
    }
}