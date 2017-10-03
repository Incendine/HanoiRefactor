package co.edu.uelbosque.esdat.hanoi;

/**
 * Clase Posicion
 * @author Daniel Alvarez
 */
public class Figura {


	private int x;
    private int y;
    private int ficha;
    private int alto;
    private int ancho; 

    public Figura(int ficha, int x, int y ) {
		this.x = x;
		this.y = y;
		this.ficha = ficha; 
	}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

	public int getFicha() {
		return ficha;
	}

	public void setFicha(int ficha) {
		this.ficha = ficha;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
   

    
}
