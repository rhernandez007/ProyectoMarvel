
package marvelstratego;


public class PiezaAgente extends PiezaMovil{

    public PiezaAgente(String nombre, Bando bando){
        super(nombre, 1, bando);
    }

    @Override
    public boolean ganaContraRango10(){
        return true;
    }
}
