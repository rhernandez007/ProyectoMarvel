
package marvelstratego;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuPrincipal extends JFrame implements Actualizable{
    private Jugador jugador;
    private JLabel lblBienvenida = new JLabel("", JLabel.CENTER);
    private JLabel lblDatos = new JLabel("", JLabel.CENTER);
    private JMenuItem itemPartidaNueva = new JMenuItem("Partida Nueva");

    public MenuPrincipal(Jugador jugador){
        this.jugador = jugador;
        setTitle("Marvel Stratego - Menú Principal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Acciones acciones = new Acciones();
        JMenuBar barra = new JMenuBar();

        JMenu menuStratego = new JMenu("STRATEGO – MARVEL HEROES!");
        itemPartidaNueva.addActionListener(acciones);
        menuStratego.add(itemPartidaNueva);

        JMenu menuPerfil = new JMenu("MI PERFIL");
        JMenuItem itemPerfil = new JMenuItem("Ver Perfil y Log de Mis Últimos Juegos");
        JMenuItem itemPassword = new JMenuItem("Cambiar mi Password");
        JMenuItem itemEliminar = new JMenuItem("Eliminar mi Cuenta");
        itemPerfil.addActionListener(acciones);
        itemPassword.addActionListener(acciones);
        itemEliminar.addActionListener(acciones);
        menuPerfil.add(itemPerfil);
        menuPerfil.add(itemPassword);
        menuPerfil.add(itemEliminar);

        JMenu menuUniverso = new JMenu("UNIVERSO MARVEL");
        JMenuItem itemRanking = new JMenuItem("Ranking");
        JMenuItem itemBatallas = new JMenuItem("Batallas");
        itemRanking.addActionListener(acciones);
        itemBatallas.addActionListener(acciones);
        menuUniverso.add(itemRanking);
        menuUniverso.add(itemBatallas);

        JMenu menuSesion = new JMenu("CERRAR SESIÓN");
        JMenuItem itemCerrar = new JMenuItem("Cerrar Sesión");
        itemCerrar.addActionListener(acciones);
        menuSesion.add(itemCerrar);

        barra.add(menuStratego);
        barra.add(menuPerfil);
        barra.add(menuUniverso);
        barra.add(menuSesion);
        setJMenuBar(barra);

        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 22));
        lblDatos.setFont(new Font("Arial", Font.PLAIN, 15));
        setLayout(new GridLayout(2, 1));
        add(lblBienvenida);
        add(lblDatos);

        actualizar();
        setSize(720, 300);
        setLocationRelativeTo(null);
    }

    @Override
    public void actualizar(){
        lblBienvenida.setText("¡Bienvenido, "+jugador.getUsername()+"!");
        lblDatos.setText("Puntos: "+jugador.getPuntos()+"   |   Partidas con Héroes: "+jugador.getPartidasHeroes()
                +"   |   Partidas con Villanos: "+jugador.getPartidasVillanos());
        itemPartidaNueva.setEnabled(Sistema.getUsuariosActivos()>=2);
    }

    private void partidaNueva(){
        if(Sistema.getUsuariosActivos()<2){
            JOptionPane.showMessageDialog(this, "Se necesitan al menos 2 jugadores registrados.");
            return;
        }

        ArrayList<Jugador> todos = Sistema.getJugadores();
        String lista = "";
        for(int i=0;i<todos.size();i++){
            if(todos.get(i)!=jugador){
                lista += "- "+todos.get(i).getUsername()+"\n";
            }
        }
        String nombreRival = JOptionPane.showInputDialog(this, "Jugadores disponibles:\n"+lista+"\nEscriba el username de su rival:",
                "Partida Nueva", JOptionPane.QUESTION_MESSAGE);
        if(nombreRival==null){
            return;
        }
        Jugador rival = Sistema.buscar(nombreRival.trim());
        if(rival==null || rival==jugador){
            JOptionPane.showMessageDialog(this, "Ese rival no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this, "¿Quieres jugar con los HÉROES?\n(Si eliges No, jugarás con los VILLANOS)",
                "Partida Nueva", JOptionPane.YES_NO_OPTION);
        if(respuesta==JOptionPane.CLOSED_OPTION){
            return;
        }
        Bando miBando = Bando.HEROES;
        if(respuesta==JOptionPane.NO_OPTION){
            miBando = Bando.VILLANOS;
        }

        respuesta = JOptionPane.showConfirmDialog(this, "¿Jugar en modo TUTORIAL (se ven todas las fichas)?\n"
                +"(Si eliges No, se juega en modo CLÁSICO con las fichas del rival ocultas)", "Partida Nueva", JOptionPane.YES_NO_OPTION);
        if(respuesta==JOptionPane.CLOSED_OPTION){
            return;
        }
        Modalidad modalidad = Modalidad.CLASICO;
        if(respuesta==JOptionPane.YES_OPTION){
            modalidad = Modalidad.TUTORIAL;
        }

        Jugador heroes = jugador;
        Jugador villanos = rival;
        if(miBando==Bando.VILLANOS){
            heroes = rival;
            villanos = jugador;
        }
        new VentanaJuego(this, heroes, villanos, modalidad).setVisible(true);
    }

    private void verPerfil(){
        String texto = "Username: "+jugador.getUsername()+"\n"
                +"Puntos: "+jugador.getPuntos()+"\n"
                +"Partidas con Héroes: "+jugador.getPartidasHeroes()+"\n"
                +"Partidas con Villanos: "+jugador.getPartidasVillanos()+"\n\n"
                +"LOG DE MIS ÚLTIMOS JUEGOS (del más reciente al más viejo):\n";
        ArrayList<String> logs = jugador.getLogs();
        if(logs.isEmpty()){
            texto += "Todavía no has jugado partidas.";
        }
        for(int i=0;i<logs.size();i++){
            texto += (i+1)+". "+logs.get(i)+"\n";
        }
        new VentanaReporte("Mi Perfil", texto).setVisible(true);
    }

    private void cambiarPassword(){
        String actual = JOptionPane.showInputDialog(this, "Password actual:", "Cambiar mi Password", JOptionPane.QUESTION_MESSAGE);
        if(actual==null){
            return;
        }
        String nuevo = JOptionPane.showInputDialog(this, "Password nuevo (5 caracteres: mayúscula, minúscula, número y especial. Ej: Ab3#x):", "Cambiar mi Password", JOptionPane.QUESTION_MESSAGE);
        if(nuevo==null){
            return;
        }
        try{
            Sistema.cambiarPassword(jugador, actual, nuevo);
            JOptionPane.showMessageDialog(this, "Password cambiado correctamente.");
        }catch(CredencialesInvalidasException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }catch(PasswordInvalidoException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCuenta(){
        String password = JOptionPane.showInputDialog(this, "Para eliminar tu cuenta escribe tu password:", "Eliminar mi Cuenta", JOptionPane.WARNING_MESSAGE);
        if(password==null){
            return;
        }
        try{
            Sistema.eliminarJugador(jugador, password);
            JOptionPane.showMessageDialog(this, "Tu cuenta fue eliminada.");
            dispose();
            new MenuInicial().setVisible(true);
        }catch(CredencialesInvalidasException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ranking(){
        ArrayList<Jugador> ranking = Sistema.getRanking();
        String texto = "RANKING (de mayor a menor puntaje)\n\n";
        for(int i=0;i<ranking.size();i++){
            texto += (i+1)+" — "+ranking.get(i).getUsername()+" — "+ranking.get(i).getPuntos()+" puntos\n";
        }
        new VentanaReporte("Ranking", texto).setVisible(true);
    }

    private void batallas(){
        String texto = "ESTADÍSTICAS GLOBALES\n\n"
                +"Usuarios activos: "+Sistema.getUsuariosActivos()+"\n"
                +"Usuarios históricos (incluye eliminados): "+Sistema.getUsuariosHistoricos()+"\n"
                +"Partidas jugadas: "+Sistema.getPartidasJugadas()+"\n"
                +"   Victorias de los HÉROES: "+Sistema.contarVictorias(Bando.HEROES, 0)+"\n"
                +"   Victorias de los VILLANOS: "+Sistema.contarVictorias(Bando.VILLANOS, 0)+"\n";
        new VentanaReporte("Batallas", texto).setVisible(true);
    }

    private void cerrarSesion(){
        int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if(opcion==JOptionPane.YES_OPTION){
            dispose();
            new MenuInicial().setVisible(true);
        }
    }

    private class Acciones implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String comando = e.getActionCommand();
            if(comando.equals("Partida Nueva")){
                partidaNueva();
            }else if(comando.equals("Ver Perfil y Log de Mis Últimos Juegos")){
                verPerfil();
            }else if(comando.equals("Cambiar mi Password")){
                cambiarPassword();
            }else if(comando.equals("Eliminar mi Cuenta")){
                eliminarCuenta();
            }else if(comando.equals("Ranking")){
                ranking();
            }else if(comando.equals("Batallas")){
                batallas();
            }else if(comando.equals("Cerrar Sesión")){
                cerrarSesion();
            }
        }
    }
}