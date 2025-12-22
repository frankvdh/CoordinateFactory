package org.locationtech.jts.geom;

    /**
     * Coordinate Factory, to be used to create Coordinates of a specified subclass
     * 
     * @author <a href="mailto:drifter.frank@gmail.com">Frank van der Hulst</a>
 * @param <T>
     *
     */
public interface CoordinateFactory<T> {

    /**
     * Build an empty Coordinate
     *
     * @return Empty Coordinate
     */
    public T build();

    /**
     * Build a Coordinate given values in its CRS
     *
     * @param x WGS84 X value (longitude)
     * @param y WGS84 Y value (latitude)
     * @param z WGS84 Z value (altitude above Mean Sea Level
     * @return Filled Coordinate
     */
    public T build(double x, double y, double z);

    /**
     * Build a Coordinate given another Coordinate
     *
     * @param c Coordinate to copy from
     * @return Coordinate of this subclass
     */
    public T build(Coordinate c);

    /**
     * Build a Coordinate given values in WGS84 CRS
     *
     * @param x WGS84 X value (longitude)
     * @param y WGS84 Y value (latitude)
     * @param z WGS84 Z value (altitude above Mean Sea Level
     * @return Filled Coordinate in this CRS
     */
    public T buildFromWgs84(double x, double y, double z);

    /**
     * Convert a given Coordinate to WGS84 CRS
     *
     * @param c Coordinate of this subclass
     * @return WGS84 Coordinate
     */
    public Wgs84 buildToWgs84(T c);
}
