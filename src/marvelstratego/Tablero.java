
package marvelstratego;

import java.util.ArrayList;

public class Tablero{
    public static final int TAMANO = 10;

    private static final String[] NOMBRES_HEROES = {"Mr. Fantastic", "Captain America", "Professor X", "Nick Fury",
        "Spider-Man", "Wolverine", "Namor", "Daredevil", "Silver Surfer", "Hulk", "Iron Man",
        "Thor", "Human Torch", "Cyclops", "Invisible Woman", "Ghost Rider", "Punisher", "Blade", "Thing",
        "Emma Frost", "She-Hulk", "Giant Man", "Beast", "Colossus",
        "Gambit", "Spider-Girl", "Ice Man", "Storm", "Phoenix", "Dr. Strange", "Elektra", "Nightcrawler",
        "Black Widow"};
    private static final String[] NOMBRES_VILLANOS = {"Dr. Doom", "Galactus", "Kingpin", "Magneto",
        "Apocalypse", "Green Goblin", "Venom", "Bullseye", "Omega Red", "Onslaught", "Red Skull",
        "Mystique", "Mysterio", "Dr. Octopus", "Deadpool", "Abomination", "Thanos", "Black Cat", "Sabretooth",
        "Juggernaut", "Rhino", "Carnage", "Mole Man", "Lizard",
        "Mr. Sinister", "Sentinel 1", "Ultron", "Sandman", "Leader", "Viper", "Sentinel 2", "Electro",
        "Black Widow"};
    private static final int[] RANGOS = {10, 9, 8, 8,
        7, 7, 7, 6, 6, 6, 6,
        5, 5, 5, 5, 4, 4, 4, 4,
        3, 3, 3, 3, 3,
        2, 2, 2, 2, 2, 2, 2, 2,
        1};
    public static final int CANTIDAD_BOMBAS = 6;

    private Pieza[][] celdas = new Pieza[TAMANO][TAMANO];
    private ArrayList<Pieza> vencidasHeroes = new ArrayList<Pieza>();
    private ArrayList<Pieza> vencidasVillanos = new ArrayList<Pieza>();
    private Bando turno = Bando.HEROES;      
    private Bando ganador = null;           
    private FinPartida motivoFin = null;

    public Tablero(){
        formar(Bando.VILLANOS);
        formar(Bando.HEROES);
    }

    public Pieza getPieza(int fila, int col){
        if(!dentro(fila, col)){
            return null;
        }
        return celdas[fila][col];
    }

    public Bando getTurno(){
        return turno;
    }

    public Bando getGanador(){
        return ganador;
    }

    public FinPartida getMotivoFin(){
        return motivoFin;
    }

    public boolean isTerminada(){
        return ganador!=null;
    }

    public ArrayList<Pieza> getVencidas(Bando bando){
        if(bando==Bando.HEROES){
            return new ArrayList<Pieza>(vencidasHeroes);
        }
        return new ArrayList<Pieza>(vencidasVillanos);
    }

    public boolean dentro(int fila, int col){
        return fila>=0 && fila<TAMANO && col>=0 && col<TAMANO;
    }

    public boolean esZonaProhibida(int fila, int col){
        boolean filaCentro = fila==4 || fila==5;
        boolean colProhibida = col==2 || col==3 || col==6 || col==7;
        return filaCentro && colProhibida;
    }

    private ArrayList<Pieza> crearEjercito(Bando bando){
        String[] nombres = NOMBRES_HEROES;
        String nombreBomba = "Nova Blast";
        if(bando==Bando.VILLANOS){
            nombres = NOMBRES_VILLANOS;
            nombreBomba = "Pumpkin Bomb";
        }
        ArrayList<Pieza> ejercito = new ArrayList<Pieza>();
        for(int i=0;i<nombres.length;i++){
            int rango = RANGOS[i];
            if(rango==1){
                ejercito.add(new PiezaAgente(nombres[i], bando));
            }else if(rango==2){
                ejercito.add(new PiezaMistica(nombres[i], bando));
            }else if(rango==3){
                ejercito.add(new PiezaScout(nombres[i], bando));
            }else{
                ejercito.add(new PiezaMovil(nombres[i], rango, bando));
            }
        }
        for(int i=0;i<CANTIDAD_BOMBAS;i++){
            ejercito.add(new Bomba(nombreBomba, bando));
        }
        ejercito.add(new Tierra(bando));
        return ejercito;
    }

    private void formar(Bando bando){
        int fondo = 0;
        int segundaFila = 1;
        int frente = 3;
        int adelante = 1;          
        int misticaDesde = 2;
        int misticaHasta = 3;
        if(bando==Bando.HEROES){
            fondo = 9;
            segundaFila = 8;
            frente = 6;
            adelante = -1;
            misticaDesde = 6;
            misticaHasta = 7;
        }

        ArrayList<Pieza> ejercito = crearEjercito(bando);
        Pieza tierra = null;
        ArrayList<Pieza> bombas = new ArrayList<Pieza>();
        ArrayList<Pieza> misticas = new ArrayList<Pieza>();
        ArrayList<Pieza> resto = new ArrayList<Pieza>();
        for(int i=0;i<ejercito.size();i++){
            Pieza p = ejercito.get(i);
            if(p instanceof Tierra){
                tierra = p;
            }else if(p instanceof Bomba){
                bombas.add(p);
            }else if(p instanceof PiezaMistica){
                misticas.add(p);
            }else{
                resto.add(p);
            }
        }

        int colTierra = 1+(int)(Math.random()*8);
        celdas[fondo][colTierra] = tierra;
        celdas[fondo][colTierra-1] = bombas.remove(0);
        celdas[fondo][colTierra+1] = bombas.remove(0);
        celdas[fondo+adelante][colTierra] = bombas.remove(0);

        while(!bombas.isEmpty()){
            colocarAlAzar(bombas.remove(0), fondo, segundaFila);
        }

        while(!misticas.isEmpty()){
            colocarAlAzar(misticas.remove(0), misticaDesde, misticaHasta);
        }

        mezclar(resto);
        for(int c=0;c<TAMANO;c++){
            if(celdas[frente][c]==null){
                celdas[frente][c] = resto.remove(0);
            }
        }

        int filaInicio = Math.min(fondo, frente);
        int filaFin = Math.max(fondo, frente);
        for(int f=filaInicio;f<=filaFin;f++){
            for(int c=0;c<TAMANO;c++){
                if(celdas[f][c]==null){
                    celdas[f][c] = resto.remove(0);
                }
            }
        }
    }

    private void colocarAlAzar(Pieza pieza, int filaA, int filaB){
        int menor = Math.min(filaA, filaB);
        int cantidadFilas = Math.abs(filaA-filaB)+1;
        while(true){
            int f = menor+(int)(Math.random()*cantidadFilas);
            int c = (int)(Math.random()*TAMANO);
            if(celdas[f][c]==null){
                celdas[f][c] = pieza;
                return;
            }
        }
    }

    private void mezclar(ArrayList<Pieza> lista){
        for(int i=lista.size()-1;i>0;i--){
            int j = (int)(Math.random()*(i+1));
            Pieza temporal = lista.get(i);
            lista.set(i, lista.get(j));
            lista.set(j, temporal);
        }
    }

    public String mover(int f1, int c1, int f2, int c2) throws MovimientoInvalidoException{
        if(isTerminada()){
            throw new MovimientoInvalidoException("La partida ya terminó.");
        }
        if(!dentro(f1, c1) || !dentro(f2, c2)){
            throw new MovimientoInvalidoException("Esa casilla no existe.");
        }
        Pieza pieza = celdas[f1][c1];
        if(pieza==null || pieza.getBando()!=turno){
            throw new MovimientoInvalidoException("Seleccione una pieza de su bando ("+turno.getTexto()+").");
        }
        if(!pieza.esMovible()){
            throw new MovimientoInvalidoException("Las bombas y la Tierra no se pueden mover.");
        }
        if(esZonaProhibida(f2, c2)){
            throw new MovimientoInvalidoException("No se puede entrar a una zona prohibida.");
        }
        Pieza destino = celdas[f2][c2];
        if(destino!=null && destino.getBando()==turno){
            throw new MovimientoInvalidoException("Esa casilla está ocupada por una pieza propia.");
        }
        if(f1!=f2 && c1!=c2){
            throw new MovimientoInvalidoException("Solo se puede mover en línea recta (no en diagonal).");
        }
        int distancia = Math.abs(f2-f1)+Math.abs(c2-c1);
        if(distancia==0){
            throw new MovimientoInvalidoException("Seleccione una casilla distinta.");
        }
        if(distancia>pieza.getAlcance()){ 
            throw new MovimientoInvalidoException(pieza.getNombre()+" solo puede avanzar una casilla.");
        }
        if(distancia>1){
            if(destino!=null){
                throw new MovimientoInvalidoException("Solo se puede atacar a una pieza que esté al lado.");
            }
            int pasoFila = signo(f2-f1);
            int pasoCol = signo(c2-c1);
            if(!caminoLibre(f1+pasoFila, c1+pasoCol, f2, c2, pasoFila, pasoCol)){
                throw new MovimientoInvalidoException("El camino no está despejado.");
            }
        }

        String mensaje;
        if(destino==null){
            celdas[f2][c2] = pieza;
            celdas[f1][c1] = null;
            if(distancia>1){
                pieza.revelar(); 
                mensaje = pieza.getNombre()+" avanzó "+distancia+" casillas y quedó revelado.";
            }else{
                mensaje = "Pieza movida.";
            }
        }else{
            mensaje = atacar(f1, c1, f2, c2);
        }

        revisarFin();
        if(!isTerminada()){
            turno = turno.opuesto();
            if(!tieneMovimientos(turno, 0)){
                ganador = turno.opuesto();
                motivoFin = FinPartida.SIN_MOVIMIENTOS;
            }
        }
        return mensaje;
    }

    private String atacar(int f1, int c1, int f2, int c2){
        Pieza atacante = celdas[f1][c1];
        Pieza defensor = celdas[f2][c2];
        atacante.revelar();
        defensor.revelar();
        String combate = atacante+" ataca a "+defensor+": ";

        ResultadoCombate resultado = defensor.combatir(atacante);
        switch(resultado){
            case GANA:
                agregarVencida(defensor);
                celdas[f2][c2] = atacante;
                celdas[f1][c1] = null;
                if(defensor instanceof Tierra){
                    ganador = atacante.getBando();
                    motivoFin = FinPartida.TIERRA_CAPTURADA;
                    return combate+"¡capturó la Tierra!";
                }
                return combate+"gana "+atacante.getNombre()+".";
            case PIERDE:
                agregarVencida(atacante);
                celdas[f1][c1] = null;
                return combate+"gana "+defensor.getNombre()+".";
            default: //EMPATE
                agregarVencida(atacante);
                agregarVencida(defensor);
                celdas[f1][c1] = null;
                celdas[f2][c2] = null;
                return combate+"empate, se eliminan las dos.";
        }
    }

    private void agregarVencida(Pieza pieza){
        if(pieza.getBando()==Bando.HEROES){
            vencidasHeroes.add(pieza);
        }else{
            vencidasVillanos.add(pieza);
        }
    }

    private void revisarFin(){
        if(isTerminada()){
            return;
        }
        if(!tienePiezasMoviles(turno.opuesto())){
            ganador = turno;
            motivoFin = FinPartida.SIN_PIEZAS;
        }else if(!tienePiezasMoviles(turno)){
            ganador = turno.opuesto();
            motivoFin = FinPartida.SIN_PIEZAS;
        }
    }

    private boolean tienePiezasMoviles(Bando bando){
        for(int f=0;f<TAMANO;f++){
            for(int c=0;c<TAMANO;c++){
                Pieza p = celdas[f][c];
                if(p!=null && p.getBando()==bando && p.esMovible()){
                    return true;
                }
            }
        }
        return false;
    }

    public void retirar(Bando bando){
        if(!isTerminada()){
            ganador = bando.opuesto();
            motivoFin = FinPartida.RETIRO;
        }
    }

    private int signo(int numero){
        if(numero>0){
            return 1;
        }
        if(numero<0){
            return -1;
        }
        return 0;
    }

    private boolean caminoLibre(int fila, int col, int filaDestino, int colDestino, int pasoFila, int pasoCol){
        if(fila==filaDestino && col==colDestino){
            return true;
        }
        if(celdas[fila][col]!=null || esZonaProhibida(fila, col)){
            return false;
        }
        return caminoLibre(fila+pasoFila, col+pasoCol, filaDestino, colDestino, pasoFila, pasoCol);
    }

    public boolean tieneMovimientos(Bando bando, int indice){
        if(indice>=TAMANO*TAMANO){
            return false;
        }
        int fila = indice/TAMANO;
        int col = indice%TAMANO;
        Pieza p = celdas[fila][col];
        if(p!=null && p.getBando()==bando && p.esMovible() && puedeAvanzar(fila, col, bando)){
            return true;
        }
        return tieneMovimientos(bando, indice+1);
    }

    private boolean puedeAvanzar(int fila, int col, Bando bando){
        return casillaDisponible(fila-1, col, bando) || casillaDisponible(fila+1, col, bando)
                || casillaDisponible(fila, col-1, bando) || casillaDisponible(fila, col+1, bando);
    }

    private boolean casillaDisponible(int fila, int col, Bando bando){
        if(!dentro(fila, col) || esZonaProhibida(fila, col)){
            return false;
        }
        return celdas[fila][col]==null || celdas[fila][col].getBando()!=bando;
    }
}
