
package marvelstratego;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentanaReporte extends JFrame{

    public VentanaReporte(String titulo, String texto){
        setTitle(titulo);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(area));
        setSize(620, 420);
        setLocationRelativeTo(null);
    }
}
