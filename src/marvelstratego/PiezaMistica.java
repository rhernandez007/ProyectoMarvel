
package marvelstratego;

public class PiezaMistica extends PiezaMovil{

    public PiezaMistica(String nombre, Bando bando){
        super(nombre, 2, bando);
    }

    @Override
    public int getAlcance(){
        return ALCANCE_LIBRE;
    }
}