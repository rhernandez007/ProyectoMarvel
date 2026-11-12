
package marvelstratego;

public class Tierra extends Pieza{

    public Tierra(Bando bando){
        super("Planet Earth", 0, bando);
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
        return ResultadoCombate.GANA;
    }

    @Override
    public String getTextoRango(){
        return "T";
    }
}