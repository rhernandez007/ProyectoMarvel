
package marvelstratego;

public class PiezaScout extends PiezaMovil{

    public PiezaScout(String nombre, Bando bando){
        super(nombre, 3, bando);
    }

    @Override
    public boolean rompeBombas(){
        return true;
    }
}
