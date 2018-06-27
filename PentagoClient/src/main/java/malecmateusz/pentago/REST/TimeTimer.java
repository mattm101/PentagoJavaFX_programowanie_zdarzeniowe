package malecmateusz.pentago.REST;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeTimer {

    class TimeTimerTask extends TimerTask{
        @Override
        public void run() {
            final String time = new Query().sendTimeRequest();
            fireTimerEvent(time);
        }
    }

    private static TimeTimer instance;

    private Timer timer;

    private List<TimeListener> listeners = new ArrayList<TimeListener>();

    public static TimeTimer getInstance() {
        if(instance == null) instance = new TimeTimer();
        return instance;
    }

    //wysyłanie co 20 sek. bo web serwis potrzebuje kilku sekund na odpowiedz i blokuje
    //requesty częstsze niż co 1 sekundę
    private TimeTimer(){
        timer = new Timer();
        timer.schedule(new TimeTimerTask(), 0, 20000);
    }

    private synchronized void fireTimerEvent(String time){
        for(TimeListener l : listeners) l.timeArrived(time);
    }

    public synchronized void addListener(TimeListener listener){
        listeners.add(listener);
    }

    public synchronized void removeListener(TimeListener listener){
        listeners.remove(listener);
    }
}
