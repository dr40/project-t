package fr.vodoji.utils.math;

import java.util.Random;

import android.os.SystemClock;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public class MathUtils {

	static private Random random = new Random(SystemClock.elapsedRealtime());
	
	///////////////////////////////////////////////////////////////////////////////////////////
	// Angle
	///////////////////////////////////////////////////////////////////////////////////////////
	
	static public double findRotation(double x1, double y1, double x2, double y2) {
		double t = -Math.toDegrees(Math.atan2(x2-x1,y2-y1));
		if (t < 0) t += 360;
		t = 180 - t;
		return Math.toRadians(t);
	}
	
	static public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
	
	static public int rndInt(int n) {
		if (n <= 0) {
			return random.nextInt();
		} else {
			return random.nextInt(n);
		}
	}
	
	
}
