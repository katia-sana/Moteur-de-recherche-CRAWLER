package tp.tp1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Utils {
	
	final static Runtime runtime = Runtime.getRuntime();
	private static HashMap<Integer, Long> startTimes = new HashMap<Integer, Long>();
	private static int chronoId = 0;
	
    /**
     * Returns a String representation of memory information 
     * @return a String representation of memory information
     */
	public static String memoryInfo() {
		final long maxMemory = runtime.maxMemory();
		final long allocatedMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();
		return "  free memory: "
				+ (freeMemory / 1024.0 / 1024.0)
				+ "\n  allocated memory: "
				+ (allocatedMemory / 1024.0 / 1024.0)
				+ "\n  max memory: "
				+ (maxMemory / 1024.0 / 1024.0)
				+ "\n  total free memory: "
				+ ((freeMemory + (maxMemory - allocatedMemory)) / 1024.0 / 1024.0);
	} 
	
	/**
	 * Returns used memory (in bytes)
	 * @return used memory (in bytes)
	 */
	public static long getUsedMemory() {
		final long allocatedMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();
		return allocatedMemory - freeMemory;
	}

	/**
	 * Returns <code>true</code> if the memory is 90% full, <code>false</code> otherwise
	 * @return <code>true</code> if the memory is 90% full, <code>false</code> otherwise
	 */
	public static boolean isMemoryFull() {
		return isMemoryFull(0.9);
	}
	
	/**
	 * Return <code>true</code> if the ratio of the memory is full.
	 * @param ratio
	 * @return <code>true</code> if the ratio of the memory is full.
	 */
	public static boolean isMemoryFull(double ratio) {
		final long maxMemory = runtime.maxMemory();
		final long allocatedMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();
		final double r = ((double)(allocatedMemory - freeMemory) / (double)maxMemory);
		return r > ratio;
	}

	
    /**
     * Waits until a key has been pressed.
     */
    public static void waitKeyPressed() {
    	try {
    		System.out.println("\nPress a key to continue... \n");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Starts the chrono.
     * @return the identifier of the chrono
     */
	public static int startChrono() {
		startTimes.put(++chronoId, System.currentTimeMillis());
		return chronoId;
	}
	
	/**
	 * Ends the chrono and returns the time in seconds since last start
	 * @param chronoId the identifier of the chrono to end.
	 * @return the time in seconds since last start of the identified chrono
	 */
	public static double endChrono(int chronoId) {
		long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTimes.get(chronoId);
        return ((double)elapsed/1000.0);
	}
	
	/**
	 * Ends the chrono and returns a String representation of the time since last start
	 * @param chronoId the identifier of the chrono to end.
	 * @return a String representation of the time since last start
	 */
	public static String formatEndChrono(int chronoId) {
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTimes.get(chronoId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");            
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date(elapsed));
	}
	
	/**
	 * Returns a String representation of the current date and time 
	 * @return a String representation of the current date and time
	 */
	public static String logTime() {
		Calendar now = Calendar.getInstance();
		int hh = now.get(Calendar.HOUR_OF_DAY);         
		int mm = now.get(Calendar.MINUTE);         
		int ss = now.get(Calendar.SECOND);         
		int mois = now.get(Calendar.MONTH) +  1;         
		int jour= now.get(Calendar.DAY_OF_MONTH);         
		int annee = now.get(Calendar.YEAR);                    
		return jour+" / "+mois+" / " +annee+ "   "+ hh+":"+mm+":"+ss + "\n";    
	}
}
