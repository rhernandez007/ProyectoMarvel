
package marvelstratego;


public abstract class Pieza implements Movible, Combatiente{
    private String nombre;
    private int rango;
    private Bando bando;
    private boolean revelada = false;

    public Pieza(String nombre, int rango, Bando bando){
        this.nombre = nombre;
        this.rango = rango;
        this.bando = bando;
    }

    public String getNombre(){
        return nombre;
    }

    public int getRango(){
        return rango;
    }

    public Bando getBando(){
        return bando;
    }

    public boolean isRevelada(){
        return revelada;
    }

    public void revelar(){
        revelada = true;
    }

    public void ocultar(){
        revelada = false;
    }

    @Override
    public boolean rompeBombas(){
        return false;
    }

    @Override
    public boolean ganaContraRango10(){
        return false;
    }

    //Metodo abstracto: cada subclase (PiezaMovil, Bomba, Tierra) lo resuelve distinto
    @Override
    public abstract ResultadoCombate combatir(Pieza atacante);

    public String getTextoRango(){
        return ""+rango;
    }

    @Override
    public String toString(){
        return nombre+" ("+getTextoRango()+")";
    }
}
