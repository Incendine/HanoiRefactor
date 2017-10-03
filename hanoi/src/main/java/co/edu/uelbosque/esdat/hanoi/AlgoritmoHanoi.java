package co.edu.uelbosque.esdat.hanoi;

import java.util.Observable;

public class AlgoritmoHanoi extends Observable {

	private int nm;
    private Movimiento[] movimientos;
    
    public AlgoritmoHanoi(Movimiento[] movimientos) {
    	this.nm=0;
    	this.movimientos=movimientos;

	}
    
	public Movimiento[] algoritmoHanoi(int n, int origen, int temporal, int destino) {
        if (n == 0) {
            return null;
        }
        algoritmoHanoi(n - 1, origen, destino, temporal);        
        nm++;
        movimientos[nm] = new Movimiento(n, origen, destino);
        algoritmoHanoi(n - 1, temporal, origen, destino);
        return movimientos;
    }
	
	public Movimiento[] algoritmoHanoi2(int n, int origen, int temporal, int destino) {
        if (n == 0) {
            return null;
        }
        algoritmoHanoi2(n - 1, origen, destino, temporal);
        this.setChanged();
        Movimiento movimientoTMP = new Movimiento(n,origen, destino);
        this.notifyObservers(movimientoTMP);
        algoritmoHanoi2(n - 1, temporal, origen, destino);
        return movimientos;
    }
	
}
