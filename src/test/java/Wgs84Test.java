/*
 * @author <a href="mailto:drifter.frank@gmail.com">Frank van der Hulst</a>
 * 
 */


import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Wgs84;

/**
 *
 * @author frank
 */
public class Wgs84Test {

    /**
     * Test of DMS method, of class Wgs84.
     */
    @Test
    public void testDMS() {
        // Decimal degrees
        check("175.836666,-39.78,1160ft", 175.83666, -39.78, 353);
        check("-175.836666,39.78,1160m", -175.83666, 39.78, 1160);
        check("175.836666,S39.78,1160", 175.83666, -39.78, 1160);
        check("W175.836666,39.78,1160m", -175.83666, 39.78, 1160);
        check("E175.836666,N39.78", 175.83666, 39.78, NaN);
        check("175.836666W,39.78N,1160ft", -175.83666, 39.78, 353);
        // Degrees, decimal minutes
        check("17533.836666,-3930.78", 175.56394, -39.513, NaN);
        // Degrees, minutes, decimal seconds
        check("1753300.836666,-393030.78", 175.55023, -39.50855, NaN);
    }

    private void check(String s, double x, double y, double z) {
        var instance = new Wgs84(s);
        assertEquals(x, instance.x, .0001);
        assertEquals(y, instance.y, .0001);
        assertEquals(z, instance.z, 1);

    }
}
