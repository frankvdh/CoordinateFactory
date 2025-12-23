<!---
  Licensed under the Creative Commons BY-NC-SA license agreement. 
  the License.  You may obtain a copy of the License at

       https://creativecommons.org/licenses/by-nc-sa/4.0/

You are free to:

Share — copy and redistribute the material in any medium or format

Adapt — remix, transform, and build upon the material

The licensor cannot revoke these freedoms as long as you follow the license 
terms.

Under the following terms:

Attribution — You must give appropriate credit , provide a link to the license, 
and indicate if changes were made. You may do so in any reasonable manner, but 
not in any way that suggests the licensor endorses you or your use.

NonCommercial — You may not use the material for commercial purposes .

ShareAlike — If you remix, transform, or build upon the material, you must 
distribute your contributions under the same license as the original.

No additional restrictions — You may not apply legal terms or technological 
measures that legally restrict others from doing anything the license permits.

Notices:
You do not have to comply with the license for elements of the material in the 
public domain or where your use is permitted by an applicable exception or 
limitation.

No warranties are given. The license may not give you all of the permissions 
necessary for your intended use. For example, other rights such as publicity, 
privacy, or moral rights may limit how you use the material.
--->

CoordinateFactory
=================

This project allows control of _Coordinate_ subclasses, in the same way that 
_CoordinateSequenceFactory_ allows control of _CoordinateSequence_ subclasses. 

The problem I'm trying to solve is that I have a subclass of _Coordinate_ which 
has additional methods. However, when the JTS library performs _union()_, _difference()_, etc operations on _LineStrings_ or _Polygons_ made up of my _Coordinates_,
it constructs <u>**generic _Coordinate_**</u> objects and inserts them in the resulting object. These have the correct 
x,y,z interpolated value, but, because 
they're generic _Coordinates_, they cannot call methods in my 
_Coordinate_ subclass, which means processing those _Polygons_ fails unless I do a 
whole lot of conversions. :( 

The process I use is that a _CoordinateFactory_ is passed to any method or 
object that needs to construct _Coordinates_. That method can then call the 
factory to build any new _Coordinates_ as the correct subclass. 

The _CoordinateFactory_ interface takes a type parameter which specifies what 
_Coordinate_ subclass it will build. It then provides methods to build objects of 
that subclass from given x, y, z values, or from a _Wgs84_ (longitude, latitude, altitude) object, and to build a 
_Wgs84_ object from the _CoordinateFactory's_ _Coordinate_ class. 

To support this, a _Wgs84_ subclass of _Coordinate_ is provided. This includes methods
to convert a wide variety of String representations of WGS84 coordinates into a
_Wgs84_ object. From here, another _CoordinateFactory's_ _buildFromWgs84_ method can
be used to convert to that subclass. Conversely, the _buildToWgs84_ method can be
used to convert from another subclass to _Wgs84_. A _GenericCoordinateFactory_ is 
also provided, which builds generic _Coordinate_ objects.

This project is not a complete solution, but maybe, hopefully, it this idea will be taken up by 
the maintainers of JTS and _CoordinateFactorys_ will be able to be passed to _union()_ etc. Or a default _CoordinateFactory_ be specified for building any interpolated points.

Until then, those pesky generic _Coordinates_ still have to be dealt with. The 
_build(Coordinate c)_ method is intended to be lightweight, so calling it a lot is not disastrous. If the given object is of the same 
subclass, it is returned without processing. If it is a generic _Coordinate_, a 
new subclass object is returned, with the same x,y,z values. Only if the 
parameter is of a different subclass entirely is a conversion done.

Hearing that this is useful to people motivates me... please do email me at 
drifter.frank@gmail.com if you find this useful.

This project is written in Java25.

Build
-----

This project is built with JDK 25 and [Maven 3](https://maven.apache.org/). 

Support
-------

I have no mechanism currently for tracking issues and requests relating to this
project. Please send an email to drifter.frank@gmail.com with any issues, thoughts, suggestions.


Known Limitations
-----------------

This project does not fix the problem inherent in _union()_ etc operations as
described above. A not-very-good solution is to follow all calls to _union(), 
difference()_, etc with a routine to loop through all _Coordinates_ replacing them 
with _Coordinates_ of the correct subclass. e.g.

            var geom = linestring.difference(polygon);
            for (var n = 0; n < geom.getNumGeometries(); n++) {
                var coords = geom.getGeometryN(n).getCoordinates();
                for (var i = 0; i < coords.length; i++) {
                    coords[i] = COORDINATE_FACTORY.build(coords[i]);
                }
            }

