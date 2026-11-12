
package marvelstratego;

public enum Bando{
    HEROES("HÉROES"),
    VILLANOS("VILLANOS");

    private String texto;

    Bando(String texto){
        this.texto = texto;
    }

    public String getTexto(){
        return texto;
    }

    public Bando opuesto(){
        if(this==HEROES){
            return VILLANOS;
        }
        return HEROES;
    }
}