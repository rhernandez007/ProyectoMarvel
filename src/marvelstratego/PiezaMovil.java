
package marvelstratego;

public class PiezaMovil extends Pieza{

    public PiezaMovil(String nombre, int rango, Bando bando){
        super(nombre, rango, bando);
    }

    @Override
    public boolean esMovible(){
        return true;
    }

    @Override
    public int getAlcance(){
        return ALCANCE_NORMAL;
    }

    @Override
    public ResultadoCombate combatir(Pieza atacante){
        if(getRango()==10 && atacante.ganaContraRango10()){
            return ResultadoCombate.GANA;
        }
        if(atacante.getRango()>getRango()){
            return ResultadoCombate.GANA;
        }
        if(atacante.getRango()==getRango()){
            return ResultadoCombate.EMPATE;
        }
        return ResultadoCombate.PIERDE;
    }
}
