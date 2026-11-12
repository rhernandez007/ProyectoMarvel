package marvelstratego;

public interface Movible{
    int ALCANCE_FIJA = 0;   
    int ALCANCE_NORMAL = 1;  
    int ALCANCE_LIBRE = 9;   

    boolean esMovible();
    int getAlcance();
}