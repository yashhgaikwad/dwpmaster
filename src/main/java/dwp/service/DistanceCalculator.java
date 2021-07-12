package dwp.service;

import dwp.model.Location;
import org.apache.lucene.util.SloppyMath;
import org.springframework.stereotype.Component;

/**
 * @author amit
 * @implNote component which is calculating milesBetween two different location.
 *
 */



@Component
public class DistanceCalculator {

    private static final double MILES_PER_METER = 0.00062137119223733D;
    
    public Double milesBetween(Location loc1, Location loc2) {
        double meters = SloppyMath.haversinMeters(
            loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
        
        return meters * MILES_PER_METER;
    }
}
