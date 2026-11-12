package marvelstratego;

import java.util.ArrayList;

public class Jugador{
    private String username;
    private String password;
    private int puntos = 0;
    private int partidasHeroes = 0;
    private int partidasVillanos = 0;
    private ArrayList<String> logs = new ArrayList<String>(); //el mas reciente va primero

    public Jugador(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getPuntos(){
        return puntos;
    }

    public void sumarPuntos(int cantidad){
        puntos += cantidad;
    }

    public int getPartidasHeroes(){
        return partidasHeroes;
    }

    public int getPartidasVillanos(){
        return partidasVillanos;
    }

    //Suma una partida jugada con el bando indicado
    public void sumarPartida(Bando bando){
        if(bando==Bando.HEROES){
            partidasHeroes++;
        }else{
            partidasVillanos++;
        }
    }

    public void agregarLog(String log){
        logs.add(0, log);
    }

    public ArrayList<String> getLogs(){
        return new ArrayList<String>(logs);
    }
}