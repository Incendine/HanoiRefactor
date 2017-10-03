package co.edu.uelbosque.esdat.hanoi;

import java.util.Observable;
import java.util.Observer;

/**
 * Hello world!
 *
 */
public class App implements Observer
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App app=new App();
        Movimiento movimientos[] = new Movimiento[(int) Math.pow(2, 3)];
        AlgoritmoHanoi ah=new AlgoritmoHanoi(movimientos);
        ah.addObserver(app);
        movimientos=ah.algoritmoHanoi2(3, 1, 2, 3);
        
    }

	public void update(Observable o, Object arg) {
		Movimiento tmp=(Movimiento)arg;
		System.out.println("Moviendo de torre:"+tmp.getTorreOrigen()+
				" a torre: "+tmp.getTorreDestino()+
				" disco: "+tmp.getFicha());
	}
}
