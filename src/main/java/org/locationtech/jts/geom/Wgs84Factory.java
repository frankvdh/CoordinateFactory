package org.locationtech.jts.geom;

/**
 * Factory to build WGS84 (lat, long, altitude) Coordinates
 *
 * @author <a href="mailto:drifter.frank@gmail.com">Frank van der Hulst</a>
 */
public class Wgs84Factory implements CoordinateFactory<Wgs84> {

    private static Wgs84Factory instance = null;

    /**
     *
     * @return
     */
    public static Wgs84Factory instance() {
        if (instance == null) {
            instance = new Wgs84Factory();
        }
        return instance;
    }

    /**
     * Build an empty WGS84 Coordinate
     *
     * @return
     */
    @Override
    public Wgs84 build() {
        return new Wgs84();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    public Wgs84 build(double x, double y, double z) {
        return new Wgs84(x, y, z);
    }

    /**
     * Convert the given Coordinate to Wgs84
     *
     * @param c Coordinate to convert
     * @return
     */
    @Override
    public Wgs84 build(Coordinate c) {
        return (c instanceof Wgs84 wgs84 ? wgs84 : new Wgs84(c));
    }
    
    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    public Wgs84 buildFromWgs84(double x, double y, double z) {
        return new Wgs84(x, y, z);
    }
    
    /**
     * Convert the given WGS84 Coordinate to WGS84... null operation
     *
     * @param c WGS84 coordinate
     * @return
     */
    @Override
    public Wgs84 buildToWgs84(Wgs84 c) {
        return c;
    }
}
