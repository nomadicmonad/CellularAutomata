import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Paints the Cellular Automaton specified by the ruleSet variable onto the screen.
 */
public class CellularAutomata extends javax.swing.JPanel {

    public int beg, end, p = 0, ypos;
    public static int x = 800, y = 600;
    private BufferedImage img;
    private Graphics2D g2;
    private ArrayList<Integer> list1, list2;
    private boolean first = true;
    private byte ruleSet = (byte) ~0b01011010; //rule 90
    private byte current;

    public CellularAutomata() {
        System.out.println(ruleSet);
        list2 = new ArrayList<>(); 
        (list1 = new ArrayList<>()).add((beg = (end = x) - 2)+1);
        (g2 = (Graphics2D) (img = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB)).getGraphics()).setColor(Color.black);
        g2.fillRect(0, 0, x, y);
    }

    public void calculate() {
        p = beg;
        do {
            if ((ruleSet & ~(1 << (current += 
                    ((getList(!first).contains(p - 1)?4:0)+(getList(!first).contains(p)?2:0)+(getList(!first).contains(p + 1)?1:0)))
                    )) == ruleSet) {getList(first).add(p);}
            current = 0;
            beg -= (beg==p)?1:0;
            end += (end==p)?1:0;
        } while (++p < end);
        repaint();
    }
    
    public ArrayList getList(boolean b) {
        return (b)?list2:list1;
    }
    
    @Override
    public void paintComponent(java.awt.Graphics g) {
        Graphics2D g1 = (Graphics2D) g;
        for (int i = 0; i < end - beg; i++) {
            g2.setColor((getList(first).contains(i + beg))?Color.white:Color.black);
            Rectangle2D.Double rect = new Rectangle2D.Double(((beg + i)) + 1, ypos, 1, 1);
            g2.draw(rect);
            g2.fill(rect);
        }
        g1.drawImage(img, 0, 0, this);
        if (ypos++ >= y) {
            ypos = 0;
            g2.setColor(Color.black);
            g2.fillRect(0, 0, x, y);
        }
        getList(first = !first).clear();
        g1.dispose();
        calculate();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CellularAutomata rule110 = new CellularAutomata();
                JFrame frame = new JFrame();
                frame.setContentPane(rule110);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(x, y);
                frame.setVisible(true);
                rule110.calculate();
            }
        });
    }
}
