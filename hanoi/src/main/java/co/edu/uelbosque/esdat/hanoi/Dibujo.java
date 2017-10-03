package co.edu.uelbosque.esdat.hanoi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Clase Dibujo
 * 
 * @author Daniel Alvarez
 */
public class Dibujo extends JPanel implements ActionListener, Observer {

	private int nroFichas;
	private Movimiento movimientoActual;
	private Image[] fichas;
	private int x, y;
	private int ficha;
	private Movimiento[] movimientos;
	private Fichita posiciones;
	private Timer timer;
	private boolean movimientoCompletado;
	private int paso;
	private static final int VELOCIDAD = 1;
	private static final int LIMITE_FICHAS = 8;
	private static final int LIMITE_TORRES = 3;
	private MainFrame nucleo;
	private ArrayList<Fichita> listaPosiciones;
	private Dimension screenSize;
	private Stack<Fichita> torre1;
	private Stack<Fichita> torre2;
	private Stack<Fichita> torre3;
	private Hashtable<Integer, Stack<Fichita>> ht;
	private Stack<Fichita> tmpPUSH;
	private Stack<Fichita> tmpPOP;
	
	public Dibujo(int nroFichas, MainFrame nucleo) {
		this.nroFichas = nroFichas;
		this.nucleo = nucleo;
		configurarPanel();
		inicializarComponentesDeAnimacion();
		timer = new Timer(VELOCIDAD, this);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	}

	private void configurarPanel() {
		setDoubleBuffered(true);
		setBackground(Color.WHITE);
	}



	private void inicializarComponentesDeAnimacion() {
		listaPosiciones = new ArrayList<Fichita>();

		ficha = 1;
		//Torres----
		torre1 = new Stack<Fichita>();
		torre2 = new Stack<Fichita>();
		torre3 = new Stack<Fichita>();
		//HashTable
		ht = new Hashtable<Integer, Stack<Fichita>>();
		ht.put(1, torre1);
		ht.put(2, torre2);
		ht.put(3, torre3);
		for (int i = 1; i <= nroFichas; i++) {
			int w = nroFichas - i + 1;
			posiciones= new Fichita(i, posicionXFicha(i, 1), posicionYFicha(w));
			listaPosiciones.add(posiciones);
			torre1.push(posiciones);
		}
		posiciones=consultarFicha(1);
		x = posiciones.getX();
		y = posiciones.getY();
		movimientoCompletado = false;
		paso = 1;
	}

	public void paint(Graphics g) {
		
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		int alto=0;
		if(nroFichas>8){
			alto=(int)((screenSize.getHeight()/nroFichas)/5);
		}else
			alto=25;
		for (int i = nroFichas; i >= 1; i--) {
			int ancho=(19+((i*10)));
			posiciones=consultarFicha(i);
			posiciones.setAlto(alto);
			posiciones.setAncho(ancho);
			if(i%2==0)
				g2.setColor(Color.MAGENTA);
			else
				g2.setColor(Color.ORANGE);
			
			g2.fillRect(posiciones.getX(), posiciones.getY(), posiciones.getAncho(),posiciones.getAlto());
			
		}
		g2.drawString("Torre 1", 115, 640);
		g2.drawString("Torre 2", 315, 640);
		g2.drawString("Torre 3", 515, 640);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void actionPerformed(ActionEvent e) {
		posiciones=consultarFicha(ficha);
		switch (paso) {
		case 1: // mover hacia arriba
			if (y > 30) { // 30 es el maximo a elevar la ficha
				y--;
				posiciones.setY(y);
			} else {
				if (movimientoActual.getTorreOrigen() < movimientoActual.getTorreDestino()) {
					paso = 2; // mover a la derecha
				} else {
					paso = 3; // mover a la izquierda
				}
			}
			break;
		case 2: // mover hacia derecha
			if (x < posicionXFicha(ficha, movimientoActual.getTorreDestino())) { // recorre
																					// hasta
																					// la
																					// torre
																					// destino
				x++;
				posiciones.setX(x);
			} else {
				paso = 4;
			}
			break;
		case 3: // mover hacia izquierda
			if (x > posicionXFicha(ficha, movimientoActual.getTorreDestino())) { // recorre
																					// hasta
																					// la
																					// torre
																					// destino
				x--;
				posiciones.setX(x);
			} else {
				paso = 4;
			}
			break;
		case 4: // mover hacia abajo
			tmpPOP = ht.get(movimientoActual.getTorreDestino());
			int nivel = tmpPOP.peek().getFicha();
			
			if (y < posicionYFicha(nivel)) {
				y++;
				posiciones.setY(y);
			} else {
				movimientoCompletado = true;
				timer.stop();
			}
			break;
		}
		repaint();
	}

	public static int posicionXFicha(int ficha, int torre) {
		int k = (torre - 1) * 200;
		//posiciones=consultarFicha(ficha);
		return ((k+110-(ficha/10))-ficha*5);
	}

	public static int posicionYFicha(int nivel) {
		int nivelito=0;

		nivelito=640-((nivel*27)+27);
		
		return nivelito;
	}

	public void iniciarAnimacion() {
		final AlgoritmoHanoi ah = new AlgoritmoHanoi(movimientos);
		ah.addObserver(this);
		Thread t = new Thread(new Runnable() {

			public void run() {
				movimientos = ah.algoritmoHanoi2(nroFichas, 1, 2, 3); // llena
																		// el
																		// vector
																		// de
																		// movimientos

			}
		});
		t.start();
		//

	}

	public void pausarAnimacion() {
		timer.stop();
	}

	public void update(Observable o, Object arg) {
		Movimiento tmp = (Movimiento) arg;
		
		
		System.out.println("Moviendo de torre:" + tmp.getTorreOrigen() + " a torre: " + tmp.getTorreDestino()
				+ " disco: " + tmp.getFicha());
		this.movimientoActual = tmp;
		tmpPOP = ht.get(tmp.getTorreOrigen());
		tmpPUSH = ht.get(tmp.getTorreDestino());
		
		posiciones=tmpPOP.pop();
		tmpPUSH.push(posiciones);
		
		
		paso = 1;
		if (movimientoActual.getFicha() == (int) Math.pow(2, nroFichas)) {
			nucleo.resolucionCompletada();
		} else {
			// movimientoCompletado = false;
			ficha = movimientoActual.getFicha();
			posiciones=consultarFicha(ficha);
			x = posiciones.getX();
			y = posiciones.getY();
		}
		synchronized (timer) {
			timer.restart();
			while (timer.isRunning()) {
				try {
					timer.wait(2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}
	
	public Fichita consultarFicha (int ficha){
		
		for(int i=0; i< listaPosiciones.size(); i++) {
            if(listaPosiciones.get(i).getFicha()==ficha){
            	posiciones= listaPosiciones.get(i);
            }
        }
		return posiciones;
	}
}
