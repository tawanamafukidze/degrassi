import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class GUI extends JFrame {
    protected JPanel contentPane;
    protected JLabel background;
    protected JLabel headingLabel;

    protected myKeyEvent myKeyAdapter;

    public static class myKeyEvent {
        private myKeyEvent() {}
        public KeyAdapter getMyKeyAdapter(JButton widget) {
            return new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        widget.doClick();
                    }
                }
            };
        }
    }

    public GUI() {
        headingLabel = new JLabel("");
        background = new JLabel("");
        contentPane = new JPanel();
        myKeyAdapter = new myKeyEvent();
        setConfig();
    }

    protected abstract void loadWidgets();

    //default configuration, override to set custom configurations per frame.
    protected void setConfig() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(null);
        setContentPane(contentPane);
        background.setIcon(new ImageIcon("img\\background.png"));
        background.setBounds(0, 0, 463, 261);
        headingLabel.setIcon(new ImageIcon("img\\heading3.png"));
    }
}
