
package marvelstratego;

public class Partida{
    private String log;
    private Bando bandoGanador;

    public Partida(String log, Bando bandoGanador){
        this.log = log;
        this.bandoGanador = bandoGanador;
    }

    public String getLog(){
        return log;
    }

    public Bando getBandoGanador(){
        return bandoGanador;
    }
}
