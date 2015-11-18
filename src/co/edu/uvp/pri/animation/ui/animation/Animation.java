package co.edu.uvp.pri.animation.ui.animation;

import com.sun.javafx.geom.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class Animation extends JComponent implements Runnable {

    private Thread thread = null;
    private int posx = 100;
    private int posy = 180;
    private int posxR = 0;
    private int dx = 1;
    private int dy = 1;
    public int speed = 20; // Velocidad de movimiento de la bola
    public Rectangle rebote;
    private Rectangle r1 = null, r2 = null, r3 = null, r4 = null,
            r5 = null, r6 = null, r7 = null, r8 = null;
    private ArrayList<Rectangle> rects = new ArrayList<>();

    private int paint = 8, survivors = paint; //variable para pintar
    private int[] posDestroyed;

    private int posD = 0;

    public Animation(int px, int py, int pxR) {
        posDestroyed = new int[paint];
        int index = 0;
        while (index < paint) {
            posDestroyed[index] = -1;
            index++;
        }
        this.posx = px;
        this.posy = py;
        this.posxR = pxR;

    }

    @Override
    public void paintComponent(Graphics g) {
        //Ball
        g.setColor(Color.red);
        g.fillOval(posx, posy, 20, 20);
        g.setColor(Color.white);
        g.drawOval(posx, posy, 20, 20);
        //Rectangulo de rebote

        rebote = new Rectangle(posxR, getHeight() - 10, 80, 10);

        g.setColor(Color.yellow);
        g.fillRect(posxR, getHeight() - 10, 80, 10);
        g.setColor(Color.white);
        g.drawRect(rebote.x, rebote.y, rebote.width, rebote.height);
        level1(g, paint, survivors);

    }

    public void level1(Graphics g, int CantidadPaint, int survivors) {

        int widthPanel = getWidth();
        rects.clear();
        int xpos = 0, ypos = 0, width = widthPanel / CantidadPaint, height = 20;
        int index = 0, indexD = 0, posDestroy = 0;
        boolean pintar = true;
//        indexD = CantidadPaint - 1;
        indexD = 0;
        while (indexD < CantidadPaint) {
            pintar = true;
            Rectangle r = new Rectangle(xpos, ypos, width, height);

            if (posDestroyed[indexD] != indexD) {
                rects.add(r);
                g.setColor(Color.BLUE);
                g.fillRect(r.x, r.y, r.width, r.height);
                g.setColor(Color.WHITE);
                g.drawRect(r.x, r.y, r.width, r.height);
            } else {
                rects.add(null);
            }
            xpos += width;
            indexD++;
        }

    }

    public int posdestroyed(int px) {
        int w = getWidth(), inicio = 0;
        int pos = 0, anchor = (w / this.paint);
        while (inicio <= w) {
            if (px <= anchor) {
                return pos;
            }
            inicio += anchor;
            pos++;
        }
        return -1;
    }

    public boolean destroyRect(int px, int py) {
        for (Rectangle rect : rects) {
            if (rect != null) {
                if ((py <= rect.height) && (px >= rect.x && (px<= rect.x + rect.width))) {

                    this.survivors -= 1;
//                    this.posDestroy = posdestroyed(px);
                    this.posDestroyed[rects.indexOf(rect)] = rects.indexOf(rect);
                    System.out.println("Lo toco");
                    System.out.println("pos detroy: " + this.posDestroyed[rects.indexOf(rect)]);
                    String arreglo = "";
                    for (int i = 0; i < posDestroyed.length; i++) {
                        arreglo += posDestroyed[i] + " ";
                    }
                    System.out.println("arreglo: " + arreglo);
                    return true;
                }
            }

        }
        //De este pedazo no es necesario ya que se evalua en el hilo
        if (py <= 0) {
            System.out.println("toca una parte vacia");
            return true;
        }
        return false;

    }

    public boolean winner() {
        for (Rectangle rect : rects) {
            if (rect != null) {
                return false;
            }
        }
        return true;

    }

    @Override
    public void run() {

        while (this.thread != null) {
            posx += dx;
            posy += dy;
            if (winner()) {
                JOptionPane.showMessageDialog(null, "Eres ganador!!!!!");
                this.pause();
            }

            //posicion del rectangulo segun el mouse
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent evento) {
                    posxR = evento.getX() - 40;
                }

            });

            if (posx + 20 == (this.getWidth())) {
                dx = -1;
            }

            if (posy + 20 == rebote.y) {
                if (posx + 10 < rebote.x) {
                    JOptionPane.showMessageDialog(null, "Game Over");
                    this.pause();
                }

                dy = -1;
            }

            if (posx <= 0) {
                dx = 1;
            }

            if (posy <= 0) {
                dy = 1;
            }

            //Evaluar cuando toque un rectangulo superior
            if (destroyRect(posx, posy)) {
                dy = 1;

            }

            repaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                Logger.getLogger(Animation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void init() {
        if (this.thread == null) {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    public void pause() {
        this.thread = null;
    }

    public void restart() {
        this.pause();
        posx = 0;
        posy = 10;
        posxR = (getWidth() / 2) - 40;
        this.init();
    }

}
