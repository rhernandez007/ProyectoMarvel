
package marvelstratego;

import java.util.ArrayList;
import java.util.Calendar;

public class Sistema{
    public static final int LARGO_PASSWORD = 5;
    public static final int PUNTOS_POR_VICTORIA = 3;

    private static ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    private static ArrayList<Partida> partidas = new ArrayList<Partida>();
    private static int usuariosHistoricos = 0; 


    public static void crearJugador(String username, String password)
            throws UsuarioDuplicadoException, PasswordInvalidoException{
        if(username==null || username.trim().isEmpty()){
            throw new UsuarioDuplicadoException("Escriba un username.");
        }
        if(buscar(username.trim())!=null){
            throw new UsuarioDuplicadoException("El username \""+username.trim()+"\" ya existe. Escriba otro.");
        }
        validarPassword(password);
        jugadores.add(new Jugador(username.trim(), password));
        usuariosHistoricos++;
    }

    public static void validarPassword(String password) throws PasswordInvalidoException{
        if(password==null || password.length()!=LARGO_PASSWORD){
            throw new PasswordInvalidoException("El password debe tener exactamente "+LARGO_PASSWORD+" caracteres.");
        }
        boolean tieneMayuscula = false;
        boolean tieneMinuscula = false;
        boolean tieneNumero = false;
        boolean tieneEspecial = false;
        for(int i=0;i<password.length();i++){
            char c = password.charAt(i);
            if(c>='A' && c<='Z'){
                tieneMayuscula = true;
            }else if(c>='a' && c<='z'){
                tieneMinuscula = true;
            }else if(c>='0' && c<='9'){
                tieneNumero = true;
            }else if(c!=' '){
                tieneEspecial = true;
            }
        }
        if(!tieneMayuscula || !tieneMinuscula || !tieneNumero || !tieneEspecial){
            throw new PasswordInvalidoException("El password debe combinar al menos una MAYÚSCULA, una minúscula,\n"
                    +"un número y un carácter especial (por ejemplo: Ab3#x).");
        }
    }

    public static Jugador login(String username, String password) throws CredencialesInvalidasException{
        Jugador j = buscar(username.trim());
        if(j==null || !j.getPassword().equals(password)){
            throw new CredencialesInvalidasException("Username o password incorrectos. Intente de nuevo.");
        }
        return j;
    }

    public static void cambiarPassword(Jugador j, String actual, String nuevo)
            throws CredencialesInvalidasException, PasswordInvalidoException{
        if(!j.getPassword().equals(actual)){
            throw new CredencialesInvalidasException("El password actual no es correcto.");
        }
        validarPassword(nuevo);
        j.setPassword(nuevo);
    }

    public static void eliminarJugador(Jugador j, String password) throws CredencialesInvalidasException{
        if(!j.getPassword().equals(password)){
            throw new CredencialesInvalidasException("El password no es correcto. La cuenta no se eliminó.");
        }
        jugadores.remove(j);
    }

    public static Jugador buscar(String username){
        for(int i=0;i<jugadores.size();i++){
            if(jugadores.get(i).getUsername().equals(username)){
                return jugadores.get(i);
            }
        }
        return null;
    }

    public static ArrayList<Jugador> getJugadores(){
        return new ArrayList<Jugador>(jugadores);
    }

    public static int getUsuariosActivos(){
        return jugadores.size();
    }

    public static int getUsuariosHistoricos(){
        return usuariosHistoricos;
    }

    public static String registrarPartida(Jugador ganador, Bando bandoGanador, Jugador perdedor, FinPartida motivo){
        Bando bandoPerdedor = bandoGanador.opuesto();
        String log;
        switch(motivo){
            case SIN_MOVIMIENTOS:
                log = perdedor.getUsername()+" usando "+bandoPerdedor.getTexto()
                        +" ha perdido por no tener movimientos válidos disponibles ante "
                        +ganador.getUsername()+" — "+fechaActual();
                break;
            case RETIRO:
                log = ganador.getUsername()+" usando "+bandoGanador.getTexto()+" ha ganado ya que "
                        +perdedor.getUsername()+" usando "+bandoPerdedor.getTexto()
                        +" se ha retirado del juego — "+fechaActual();
                break;
            default:
                String accion = "CAPTURADO";
                if(bandoGanador==Bando.HEROES){
                    accion = "SALVADO";
                }
                log = ganador.getUsername()+" usando "+bandoGanador.getTexto()+" ha "+accion
                        +" la TIERRA! Venciendo a "+perdedor.getUsername()+" — "+fechaActual();
        }
        ganador.sumarPuntos(PUNTOS_POR_VICTORIA);
        ganador.sumarPartida(bandoGanador);
        perdedor.sumarPartida(bandoPerdedor);
        ganador.agregarLog(log);
        perdedor.agregarLog(log);
        partidas.add(new Partida(log, bandoGanador));
        return log;
    }

    public static int getPartidasJugadas(){
        return partidas.size();
    }

    private static String fechaActual(){
        Calendar hoy = Calendar.getInstance();
        int dia = hoy.get(Calendar.DAY_OF_MONTH);
        int mes = hoy.get(Calendar.MONTH)+1;
        int anio = hoy.get(Calendar.YEAR);
        int hora = hoy.get(Calendar.HOUR_OF_DAY);
        int minuto = hoy.get(Calendar.MINUTE);
        return dosDigitos(dia)+"/"+dosDigitos(mes)+"/"+anio+" "+dosDigitos(hora)+":"+dosDigitos(minuto);
    }

    private static String dosDigitos(int numero){
        if(numero<10){
            return "0"+numero;
        }
        return ""+numero;
    }

    public static int contarVictorias(Bando bando, int indice){
        if(indice>=partidas.size()){
            return 0;
        }
        int esta = 0;
        if(partidas.get(indice).getBandoGanador()==bando){
            esta = 1;
        }
        return esta+contarVictorias(bando, indice+1);
    }

    private static void insertarOrdenado(ArrayList<Jugador> ranking, Jugador j, int indice){
        if(indice>=ranking.size()){
            ranking.add(j);
            return;
        }
        if(j.getPuntos()>ranking.get(indice).getPuntos()){
            ranking.add(indice, j);
            return;
        }
        insertarOrdenado(ranking, j, indice+1);
    }

    public static ArrayList<Jugador> getRanking(){
        ArrayList<Jugador> ranking = new ArrayList<Jugador>();
        for(int i=0;i<jugadores.size();i++){
            insertarOrdenado(ranking, jugadores.get(i), 0);
        }
        return ranking;
    }
}