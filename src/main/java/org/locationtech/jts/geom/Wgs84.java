package org.locationtech.jts.geom;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;
import static java.lang.Double.parseDouble;
import static java.lang.Math.abs;
import java.text.DecimalFormat;
import static java.text.NumberFormat.getInstance;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * Earth coordinates in longitude, latitude, emtres AMSL
 *
 * x is stored in the range 0 ... 360, with values over 180 representing West. y
 * is stored in the range -90 ... +90, with negative representing South z is
 * stored in metres above means sea level
 *
 * This is convenient for NZ, where some polygons overlap the 180 meridian
 *
 * @author <a href="mailto:drifter.frank@gmail.com">Frank van der Hulst</a>
 */
@SuppressWarnings("serial")
public class Wgs84 extends Coordinate {

    /**
     *
     */
    final static public Pattern LATLON_DOUBLES = compile("^\\s*([NSEW\\+\\-]?)\\s*(\\d{1,3}(?:\\.\\d+)?)()()([NSEW]?)[,\\s]+([NSEW\\+\\-]?)\\s*(\\d{1,3}(?:\\.\\d+)?)()()([NSEW]?)[,\\s]*((?:\\d+(?:\\.\\d+)?)?)(m|ft|)\\s*$", CASE_INSENSITIVE);

    final static private Pattern[] patterns = {
        // First 3 patterns handle decimal degrees, degrees + decimal minutes, degrees minutes decimal seconds respectively
        // Degrees, minutes, and seconds can be optionally separated by spaces or commas
        // In each case, there may be a prefix or suffix to identify which hemisphere. Latitude & longitude can be given in any order
        // They may optionally be followed by an elevation in either metres (the default) or feet
        LATLON_DOUBLES,
        compile("^\\s*([NSEW\\+\\-]?)\\s*(\\d{1,3})[,\\s]*(\\d\\d(?:\\.\\d+)?)()([NSEW]?)[,\\s]+([NSEW\\+\\-]?)\\s*(\\d{1,3})[,\\s]*(\\d\\d(?:\\.\\d+)?)()([NSEW]?)[,\\s]*((?:\\d+(?:\\.\\d+)?)?)(m|ft|)\\s*$", CASE_INSENSITIVE),
        compile("^\\s*([NSEW\\+\\-]?)\\s*(\\d{1,3})[,\\s]*(\\d\\d)[,\\s]*(\\d\\d(?:\\.\\d+)?)([NSEW]?)[,\\s]+([NSEW\\+\\-]?)\\s*(\\d{1,3})[,\\s]*(\\d\\d)[,\\s]*(\\d\\d(?:\\.\\d+)?)([NSEW]?)[,\\s]*((?:\\d+(?:\\.\\d+)?)?)(m|ft|)\\s*$", CASE_INSENSITIVE)};

    final static private Pattern LAT_DMS = compile("^\\s*(\\d\\d?)(\\d\\d)?(\\d\\d(?:\\.\\d+)?)([NSns])");
    final static private Pattern LON_DMS = compile("^\\s*(\\d\\d\\d)(\\d\\d)?(\\d\\d(?:\\.\\d+)?)(?:\\D\\s)?([EWew])");

    /**
     *
     */
    public Wgs84() {
        this(Double.NaN, Double.NaN, Double.NaN);
    }

    /**
     * Build a Wgs84 coordinate from the specified Coordinate, which should contain latitude, longitude, altitude values
     *
     * @param c Coordinate to copy
     */
    public Wgs84(Coordinate c) {
        this(c.x, c.y, c.z);
    }

    /**
     *
     * @param lng
     * @param lat
     */
    public Wgs84(double lng, double lat) {
        this(lng, lat, Double.NaN);
    }

    /**
     *
     * @param lng
     * @param lat
     * @param elev
     */
    public Wgs84(double lng, double lat, double elev) {
        y = lat;
        x = lng;
        if (x < 0) {
            x = 360.0 - x;
        }
        z = elev;
    }

    /**
     * Construct LatLon from string
     *
     * @param s String containing "y, x" or "x, y"
     *
     * Acceptable format examples:
     *
     * P1: S123456.7 = 12deg 34 min 56.7 secs South S12 34 56.7 = 12deg 34 min
     * 56.7 secs South P2: 12 34.5 S = 12deg 34.5 min South P3: 123456.7S =
     * 12deg 34 min 56.7 secs South 123456S = 12deg 34 min 56 secs South P4:
     * -12.345 = 12.345deg South
     */
    public Wgs84(String s) {
        for (var p : patterns) {
            var m = p.matcher(s);
            try {
                if (m.find()) {
                    var n1 = degrees(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5));
                    if (isNaN(n1)) {
                        continue;
                    }
                    var n2 = degrees(m.group(6), m.group(7), m.group(8), m.group(9), m.group(10));
                    if (isNaN(n2)) {
                        continue;
                    }
                    var latFirst = (!m.group(1).isEmpty() && "NnSs".contains(m.group(1)))
                            || (!m.group(5).isEmpty() && "NnSs".contains(m.group(5))) || (abs(n1) < 90 && abs(n2) > 90);
                    if (latFirst) {
                        y = n1;
                        x = n2;
                    } else {
                        x = n1;
                        y = n2;
                    }
                    if (m.groupCount() < 12 || m.group(11).isBlank()) {
                        z = NaN;
                    } else {
                        z = parseDouble(m.group(11));
                        if (m.group(12).equalsIgnoreCase("ft")) {
                            z /= 3.28084f;
                        } else if (m.group(12).isEmpty() || m.group(12).equalsIgnoreCase("m")) {
                        } else {
                            throw new RuntimeException("Invalid elevation units '" + m.group(12) + "' in " + s);
                        }
                    }
                    return;
                }
            } catch (IndexOutOfBoundsException ex) {
//                LOG.info("Pattern {} index error: {}", p, s);
                // Obviously a mismatch.. keep trying other patterms
            }
        }
        throw new NumberFormatException("Not valid latitude/longitude: " + s);
    }

    /**
     * Construct LonLat from string
     *
     * Acceptable format examples:
     *
     * S123456.7 = 12deg 34 min 56.7 secs South S12 34 56.7 = 12deg 34 min 56.7
     * secs South 12 34.5 S = 12deg 34.5 min South 123456.7S = 12deg 34 min 56.7
     * secs South 123456S = 12deg 34 min 56 secs South -12.345 = 12.345deg South
     *
     * @param s String containing "y, x" or "x, y"
     * @param p Pattern to translate to LonLat... currently the only value is
     * LonLat.LATLON_DOUBLES
     * @param latFollowsLng true if latitude follows longitude in the pattern
     */
    public Wgs84(String s, Pattern p, boolean latFollowsLng) {
        var m = p.matcher(s);
        if (m.find()) {
            x = parseDouble(m.group(2)) + (m.group(3).isEmpty() ? 0 : parseDouble(m.group(3)) / 60) + (m.group(4).isEmpty() ? 0 : parseDouble(m.group(4)) / 3600);
            y = parseDouble(m.group(7)) + (m.group(8).isEmpty() ? 0 : parseDouble(m.group(8)) / 60) + (m.group(9).isEmpty() ? 0 : parseDouble(m.group(9)) / 3600);
            if (latFollowsLng) {
                if ((m.group(6) + m.group(10)).matches("[Ss\\-]")) {
                    y *= -1;
                }

                if ((m.group(1) + m.group(5)).matches("[Ww\\-]")) {
                    x = 360.0 - x;
                }
            } else {
                var t = x;
                x = y;
                y = t;
                if ((m.group(1) + m.group(6)).matches("[Ss\\-]")) {
                    y *= -1;
                }
                if ((m.group(6) + m.group(10)).matches("[Ww\\-]")) {
                    x = 360.0 - x;
                }
            }
            return;
        }
        x = NaN;
        y = NaN;
    }

    /**
     * Construct Wgs84 coordinate from two strings
     *
     * @param lat String representing latitude
     * @param lng String representing longitude
     */
    public Wgs84(String lat, String lng) {
        var m1 = LAT_DMS.matcher(lat);
        var m2 = LON_DMS.matcher(lng);
        if (m1.find() && m2.find()) {
            y = parseDouble(m1.group(1));
            if (m1.group(2) == null || m1.group(2).length() == 0) {
                y += parseDouble(m1.group(3)) / 60;
            } else {
                y += parseDouble(m1.group(2)) / 60 + parseDouble(m1.group(3)) / 3600;
            }
            if (m1.group(4).toUpperCase().equals("S")) {
                y *= -1;
            }

            x = parseDouble(m2.group(1));
            if (m2.group(2) == null || m2.group(2).length() == 0) {
                x += parseDouble(m2.group(3)) / 60;
            } else {
                x += parseDouble(m2.group(2)) / 60 + parseDouble(m2.group(3)) / 3600;
            }
            if (m2.group(4).toUpperCase().equals("W")) {
                x = 360.0 - x;
            }
            return;
        }

        y = parseDouble(lat);
        x = parseDouble(lng);
//    assert false: "Unrecognised LonLat format: " + lat + ", " + lng;
    }

    /**
     *
     * @param lng String representing longitude
     * @param lat String representing latitude
     * @param p Patterns to interpret lng and lat respectively. Each pattern
     * should contain 4 groups: degrees, minutes, seconds, hemisphere e.g.
     * "(\d\d\d)(\d\d)(\d\d(?:\.\d+)?)([EW])",
     * "(\d\d)(\d\d)(\d\d(?:\.\d+)?)([NS])" The minutes and/or seconds and/or
     * hemisphere groups may be null e.g. "(\d\d\d)\.\d+)()()([EW])",
     * "(\d\d\d)(\d\.\d+)()([EW])", "(\-?\d\d)\.\d+)()()()"
     */
    public Wgs84(String lng, String lat, Pattern[] p) {
        var m1 = p[0].matcher(lng);
        var m2 = p[1].matcher(lat);
        if (m1.find() && m2.find()) {
            y = parseDouble(m2.group(1));
            if (m2.group(2) == null || m2.group(2).length() == 0) {
                y += parseDouble(m2.group(3)) / 60;
            } else {
                y += parseDouble(m2.group(2)) / 60 + parseDouble(m2.group(3)) / 3600;
            }
            if (m2.group(4).toUpperCase().equals("S")) {
                y *= -1;
            }

            x = parseDouble(m1.group(1));
            if (m1.group(2) == null || m1.group(2).length() == 0) {
                x += parseDouble(m1.group(3)) / 60;
            } else {
                x += parseDouble(m1.group(2)) / 60 + parseDouble(m1.group(3)) / 3600;
            }
            if (m1.group(4).toUpperCase().equals("W")) {
                x = 360.0 - x;
            }
            return;
        }
        assert false : "Unrecognised LonLat format: " + lat + ", " + lng;
    }

    private double degrees(String prefix, String deg, String min, String sec, String suffix) {
        var negate = false;
        if (!prefix.isEmpty()) {
            if (!suffix.isEmpty()) {
                // Both prefix & suffix
                return NaN;
            }
            negate = "WwSs-".contains(prefix);
        } else {
            if (!suffix.isEmpty()) {
                negate = "WwSs".contains(suffix);
            }
        }
        var result = parseDouble(deg);
        if (!min.isBlank()) {
            result += parseDouble(min) / 60;
        }
        if (!sec.isBlank()) {
            result += parseDouble(sec) / 3600;
        }
        return negate ? result * -1 : result;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        // Used in generating KML
        var DP5 = new DecimalFormat("#0.00000");
        return DP5.format(x) + ", " + DP5.format(y);
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    private String formatDMS(double value, String sep1, String sep2, String posNeg) {
        final var f1 = getInstance();
        f1.setMaximumFractionDigits(1);
        f1.setMinimumFractionDigits(1);
        f1.setMinimumIntegerDigits(2);

        var pN = 0;
        if (value < 0) {
            pN = 1;
            value = -value;
        }
        var deg = (int) value;
        var minutes = (value - deg) * 60;
        var min = (int) minutes;
        var sec = (minutes - min) * 60;
        return String.format("%d%s%02d%s%s%s%c", deg, sep1, min, sep1, f1.format(sec), sep2, posNeg.charAt(pN));
    }

    /**
     *
     * @return
     */
    public String DMS() {
        return formatDMS(y, " ", " ", "NS") + ", " + formatDMS(x, " ", " ", "EW");
    }

    /**
     *
     * @return
     */
    @Override
    public Object clone() {
        return super.clone(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
}
