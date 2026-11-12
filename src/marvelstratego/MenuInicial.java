package marvelstratego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuInicial extends JFrame{

    public MenuInicial(){
        setTitle("Marvel Stratego");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("MARVEL HEROES STRATEGO", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(183, 28, 28));
        add(titulo, BorderLayout.NORTH);

        Acciones acciones = new Acciones();
        JButton btnLogin = new JButton("LOG IN");
        JButton btnCrear = new JButton("CREAR PLAYER");
        btnLogin.addActionListener(acciones);
        btnCrear.addActionListener(acciones);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnCrear.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel botones = new JPanel(new GridLayout(2, 1, 10, 15));
        botones.add(btnLogin);
        botones.add(btnCrear);
        add(botones, BorderLayout.CENTER);

        setSize(420, 250);
        setLocationRelativeTo(null);
    }

    private void login(){
        while(true){
            String username = JOptionPane.showInputDialog(this, "Username:", "LOG IN", JOptionPane.QUESTION_MESSAGE);
            if(username==null){
                return; 
            }
            String password = JOptionPane.showInputDialog(this, "Password:", "LOG IN", JOptionPane.QUESTION_MESSAGE);
            if(password==null){
                return;
            }
            try{
                Jugador jugador = Sistema.login(username, password);
                new MenuPrincipal(jugador).setVisible(true);
                dispose();
                return;
            }catch(CredencialesInvalidasException e){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void crearPlayer(){
        while(true){
            String username = JOptionPane.showInputDialog(this, "Username (único):", "CREAR PLAYER", JOptionPane.QUESTION_MESSAGE);
            if(username==null){
                return;
            }
            String password = JOptionPane.showInputDialog(this, "Password (5 caracteres: mayúscula, minúscula, número y especial. Ej: Ab3#x):", "CREAR PLAYER", JOptionPane.QUESTION_MESSAGE);
            if(password==null){
                return;
            }
            try{
                Sistema.crearJugador(username, password);
                JOptionPane.showMessageDialog(this, "Jugador creado correctamente.");
                return;
            }catch(UsuarioDuplicadoException e){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }catch(PasswordInvalidoException e){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class Acciones implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getActionCommand().equals("LOG IN")){
                login();
            }else if(e.getActionCommand().equals("CREAR PLAYER")){
                crearPlayer();
            }
        }
    }
}
