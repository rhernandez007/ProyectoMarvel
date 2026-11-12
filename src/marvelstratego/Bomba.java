
package marvelstratego;

public class Bomba extends Pieza{

    public Bomba(String nombre, Bando bando){
        super(nombre, 0, bando);
    }

    @Override
    public boolean esMovible(){
        return false;
    }

    @Override
    public int getAlcance(){
        return ALCANCE_FIJA;
    }

    @Override
    public ResultadoCombate combatir(Pieza atacante){
        if(atacante.rompeBombas()){
            return ResultadoCombate.GANA;
        }
        return ResultadoCombate.PIERDE;
    }

    @Override
    public String getTextoRango(){
        return "B";
    }
}
