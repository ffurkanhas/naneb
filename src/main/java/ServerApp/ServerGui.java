package ServerApp;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class ServerGui {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception evt) {}
        JTextField t;
        JButton b;
        JFrame f = new JFrame("naneB Server Installer");
        Container cp = f.getContentPane();
        cp.setLayout(new GridBagLayout());
        cp.setBackground(UIManager.getColor("control"));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(2, 2, 2, 2);
        c.anchor = GridBagConstraints.WEST;

        cp.add(new JLabel("PostgreSql Server Adress:", SwingConstants.RIGHT), c);

        cp.add(new JLabel("PostgreSql User Name:", SwingConstants.RIGHT), c);

        cp.add(new JLabel("PostgreSql Password:", SwingConstants.RIGHT), c);

        cp.add(new JLabel("PostgreSql Database Name:", SwingConstants.RIGHT), c);

        cp.add(new JLabel("Zip/Post code:", SwingConstants.RIGHT), c);

        cp.add(new JLabel("Telephone:", SwingConstants.RIGHT), c);

        cp.add(new JButton("Clear"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        cp.add(t = new JTextField("localhost",20), c);
        t.setFocusAccelerator('n');
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        cp.add(t = new JTextField("postgres",20), c);
        t.setFocusAccelerator('h');
        cp.add(t = new JTextField("postgres",20), c);
        t.setFocusAccelerator('c');
        cp.add(t = new JTextField("naneb",20), c);
        t.setFocusAccelerator('s');
        cp.add(t = new JTextField(20), c);
        t.setFocusAccelerator('z');
        cp.add(t = new JTextField(20), c);
        t.setFocusAccelerator('t');
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        cp.add(b = new JButton("OK"), c);
        b.setMnemonic('o');

        f.pack();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
        f.setVisible(true);
    }
}