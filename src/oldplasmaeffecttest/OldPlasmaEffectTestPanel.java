package oldplasmaeffecttest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author leonardo
 */
public class OldPlasmaEffectTestPanel extends JPanel {
    
    private BufferedImage offscreen;
    private int[] data;
    
    private int[] palette = new int[256];
    private int[] data2;
    private float ci;
    
    public OldPlasmaEffectTestPanel(int width, int height) {
        offscreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        data = ((DataBufferInt) offscreen.getRaster().getDataBuffer()).getData();
        data2 = new int[width * height];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < 256; x++) {
            palette[x] = Color.HSBtoRGB(ci + x / 350f, 1f, 1f);
        }
        ci += 0.005f;
        ci = (ci > 1f) ? 0 : ci;
        
        double t = System.currentTimeMillis() * 0.02;
        for (int y = 0; y < offscreen.getHeight(); y++) {
            for (int x = 0; x < offscreen.getWidth(); x++) {
                double k = (t + x) * 0.02;
                double v1 = 128 + 128 * Math.sin(0.075 * (x * Math.sin(k) + y * Math.cos(k * 2)));
                double v2 = 128 + 128 * Math.sin(0.055 * (x * Math.cos(k * 3) + y * Math.sin(k * 4)));
                double v3 = 128 + 128 * Math.cos((t + y) * 0.05);
                double v4 = 128 + 128 * Math.sin((t + x) * 0.04);
                double v5 = 128 + 128 * Math.sin(k + Math.sqrt(v3 + v4));
                double v6 = (v1 + v2 + v5) / 3;
                data2[x + y * offscreen.getWidth()] = (int) v6;
            }
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = palette[data2[i]];
        }
        
        g.drawImage(offscreen, 0, 0, getWidth(), getHeight(), null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame();
                frame.setTitle("Old (School) Plasma Effect Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.getContentPane().add(new OldPlasmaEffectTestPanel(100, 100));
                frame.setVisible(true);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        frame.repaint();
                    }
                }, 100, 20);
            }
        });
    }
    
}
