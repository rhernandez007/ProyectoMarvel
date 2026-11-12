package marvelstratego;
public interface Combatiente{
    ResultadoCombate combatir(Pieza atacante);
    boolean rompeBombas();                     
    boolean ganaContraRango10();               
}