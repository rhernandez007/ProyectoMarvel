
package marvelstratego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentanaJuego extends JFrame implements Actualizable{
    private static final Color COLOR_HEROES = new Color(25, 85, 160);
    private static final Color COLOR_VILLANOS = new Color(40, 120, 45);
    private static final Color COLOR_OCULTA = new Color(40, 40, 40);
    private static final Color COLOR_VACIA = new Color(225, 225, 215);
    private static final Color COLOR_PROHIBIDA = new Color(110, 110, 110);

    private MenuPrincipal menu;
    private Jugador jugadorHeroes;
    private Jugador jugadorVillanos;
    private Modalidad modalidad;
    private Tablero tablero = new Tablero();

    private Bando vistaDe = Bando.HEROES;   
    private boolean ocultarTodo = false;     
    private int filaSel = -1;                
    private int colSel = -1;

    private JButton[][] casillas = new JButton[Tablero.TAMANO][Tablero.TAMANO];
    private JLabel lblTurno = new JLabel("", JLabel.CENTER);
    private JLabel lblMensaje = new JLabel(" ", JLabel.CENTER);
    private JTextArea txtVencidasHeroes = new JTextArea(10, 18);
    private JTextArea txtVencidasVillanos = new JTextArea(10, 18);

    public VentanaJuego(MenuPrincipal menu, Jugador jugadorHeroes, Jugador jugadorVillanos, Modalidad modalidad){
        this.menu = menu;
        this.jugadorHeroes = jugadorHeroes;
        this.jugadorVillanos = jugadorVillanos;
        this.modalidad = modalidad;

        setTitle("Marvel Stratego - "+jugadorHeroes.getUsername()+" (Héroes) vs "+jugadorVillanos.getUsername()+" (Villanos)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Acciones acciones = new Acciones();

        lblTurno.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTurno, BorderLayout.NORTH);

        JPanel panelTablero = new JPanel(new GridLayout(Tablero.TAMANO, Tablero.TAMANO, 2, 2));
        panelTablero.setBackground(Color.BLACK);
        for(int f=0;f<Tablero.TAMANO;f++){
            for(int c=0;c<Tablero.TAMANO;c++){
                JButton boton = new JButton();
                boton.setActionCommand(""+(f*Tablero.TAMANO+c)); 
                boton.addActionListener(acciones);
                boton.setFont(new Font("Arial", Font.BOLD, 10));
                boton.setMargin(new Insets(0, 0, 0, 0)); 
                casillas[f][c] = boton;
                panelTablero.add(boton);
            }
        }
        add(panelTablero, BorderLayout.CENTER);

        txtVencidasHeroes.setEditable(false);
        txtVencidasVillanos.setEditable(false);
        JPanel panelVencidas = new JPanel(new GridLayout(2, 1));
        panelVencidas.add(new JScrollPane(txtVencidasHeroes));
        panelVencidas.add(new JScrollPane(txtVencidasVillanos));
        add(panelVencidas, BorderLayout.EAST);

        JButton btnRetiro = new JButton("RETIRO");
        btnRetiro.addActionListener(acciones);
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel panelAbajo = new JPanel(new BorderLayout());
        panelAbajo.add(lblMensaje, BorderLayout.CENTER);
        panelAbajo.add(btnRetiro, BorderLayout.EAST);
        add(panelAbajo, BorderLayout.SOUTH);

        actualizar();
        setSize(1250, 760);
        setLocationRelativeTo(null);
    }

    private Jugador jugadorDe(Bando bando){
        if(bando==Bando.HEROES){
            return jugadorHeroes;
        }
        return jugadorVillanos;
    }

    @Override
    public void actualizar(){
        Bando turno = tablero.getTurno();
        lblTurno.setText("Turno: "+turno.getTexto()+" ("+jugadorDe(turno).getUsername()+")   —   "+modalidad.getTexto());
        if(turno==Bando.HEROES){
            lblTurno.setForeground(COLOR_HEROES);
        }else{
            lblTurno.setForeground(COLOR_VILLANOS);
        }

        for(int f=0;f<Tablero.TAMANO;f++){
            for(int c=0;c<Tablero.TAMANO;c++){
                pintarCasilla(f, c);
            }
        }
        txtVencidasHeroes.setText("HÉROES VENCIDOS:\n"+listaVencidas(Bando.HEROES));
        txtVencidasVillanos.setText("VILLANOS VENCIDOS:\n"+listaVencidas(Bando.VILLANOS));
    }

    private String listaVencidas(Bando bando){
        ArrayList<Pieza> vencidas = tablero.getVencidas(bando);
        String texto = "";
        for(int i=0;i<vencidas.size();i++){
            texto += vencidas.get(i)+"\n";
        }
        return texto;
    }

    private void pintarCasilla(int f, int c){
        JButton boton = casillas[f][c];
        if(tablero.esZonaProhibida(f, c)){
            boton.setBackground(COLOR_PROHIBIDA);
            boton.setText("");
            return;
        }
        Pieza p = tablero.getPieza(f, c);
        if(p==null){
            boton.setBackground(COLOR_VACIA);
            boton.setText("");
            return;
        }
        boton.setForeground(Color.WHITE);
        if(!esVisible(p)){
            boton.setBackground(COLOR_OCULTA);
            boton.setText("?");
        }else{
            boton.setText(p.getTextoRango()+" "+p.getNombre());
            if(p.getBando()==Bando.HEROES){
                boton.setBackground(COLOR_HEROES);
            }else{
                boton.setBackground(COLOR_VILLANOS);
            }
        }
        if(f==filaSel && c==colSel){ 
            boton.setBackground(Color.YELLOW);
            boton.setForeground(Color.BLACK);
        }
    }

    private boolean esVisible(Pieza p){
        if(tablero.isTerminada() || modalidad==Modalidad.TUTORIAL){
            return true;
        }
        if(ocultarTodo){
            return false;
        }
        return p.getBando()==vistaDe || p.isRevelada();
    }

    private void clicCasilla(int f, int c){
        if(tablero.isTerminada()){
            return;
        }
        Pieza p = tablero.getPieza(f, c);
        Bando turno = tablero.getTurno();

        if(p!=null && p.getBando()==turno){
            filaSel = f;
            colSel = c;
            lblMensaje.setText("Seleccionado: "+p+". Ahora elija la casilla de destino.");
            actualizar();
            return;
        }
        if(filaSel==-1){
            lblMensaje.setText("Primero seleccione una de sus piezas ("+turno.getTexto()+").");
            return;
        }

        try{
            String resultado = tablero.mover(filaSel, colSel, f, c);
            filaSel = -1;
            colSel = -1;
            lblMensaje.setText(resultado);
            if(tablero.isTerminada()){
                actualizar();
                terminarPartida(resultado);
            }else{
                cambiarTurno(resultado);
            }
        }catch(MovimientoInvalidoException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Movimiento inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cambiarTurno(String resultado){
        if(modalidad==Modalidad.CLASICO){
            ocultarTodo = true;
            actualizar();
            Bando siguiente = tablero.getTurno();
            JOptionPane.showMessageDialog(this, resultado+"\n\nAhora es turno de los "+siguiente.getTexto()
                    +" ("+jugadorDe(siguiente).getUsername()+").\nPresione Aceptar cuando ese jugador esté frente a la pantalla.");
            vistaDe = siguiente;
            ocultarTodo = false;
        }
        actualizar();
    }

    private void retiro(){
        if(tablero.isTerminada()){
            return;
        }
        Bando turno = tablero.getTurno();
        int opcion = JOptionPane.showConfirmDialog(this, jugadorDe(turno).getUsername()+", ¿seguro que deseas retirarte?\n"
                +"Tu rival recibirá "+Sistema.PUNTOS_POR_VICTORIA+" puntos.", "RETIRO", JOptionPane.YES_NO_OPTION);
        if(opcion==JOptionPane.YES_OPTION){
            tablero.retirar(turno);
            actualizar();
            terminarPartida(jugadorDe(turno).getUsername()+" se retiró.");
        }
    }

    private void terminarPartida(String ultimoMovimiento){
        Bando bandoGanador = tablero.getGanador();
        Jugador ganador = jugadorDe(bandoGanador);
        Jugador perdedor = jugadorDe(bandoGanador.opuesto());
        String log = Sistema.registrarPartida(ganador, bandoGanador, perdedor, tablero.getMotivoFin());
        JOptionPane.showMessageDialog(this, ultimoMovimiento+"\n\n¡FIN DE LA PARTIDA!\n"+log);
        Actualizable anterior = menu; 
        anterior.actualizar();
        dispose();
    }

    private class Acciones implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getActionCommand().equals("RETIRO")){
                retiro();
            }else{
                int numero = Integer.parseInt(e.getActionCommand());
                clicCasilla(numero/Tablero.TAMANO, numero%Tablero.TAMANO);
            }
        }
    }
}