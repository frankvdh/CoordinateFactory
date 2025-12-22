package org.locationtech.jts.geom;

/**
 * Factory to build generic Coordinates. Use where subclasses are not needed
 *
 * @author <a href="mailto:drifter.frank@gmail.com">Frank van der Hulst</a>
 */
public class GenericCoordinateFactory implements CoordinateFactory<Coordinate> {

    private static GenericCoordinateFactory instance = null;

    /**
     *
     * @return
     */
    public static GenericCoordinateFactory instance() {
        if (instance == null) {
            instance = new GenericCoordinateFactory();
        }
        return instance;
    }
    
    /**
     * Private constructor to force usage via the instance() method
     */
    private GenericCoordinateFactory() {}
    
    /**
     * Build empty Coordinate
     *
     * @return empty Coordinate
     */
    @Override
    public Coordinate build() {
        return new Coordinate();
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    public Coordinate build(double x, double y, double z) {
        return new Coordinate(x, y, z);
    }

    /**
     * Build copy of specified Coordinate
     *
     * @param c Coordinate to copy
     * @return
     */
    @Override
    public Coordinate build(Coordinate c) {
        return new Coordinate(c);
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    public Coordinate buildFromWgs84(double x, double y, double z) {
        throw new RuntimeException("Not supported for generic Coordinate");
    }

    @Override
    public Wgs84 buildToWgs84(Coordinate c) {
        throw new RuntimeException("Not supported for generic Coordinate");
    }    

}
