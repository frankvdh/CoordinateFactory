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

DbfEngine
=========

This project allows read/write access to DBF database files. The specific 
motivation is to read and write Shapefiles, which use DBF files to store 
attributes. However, it should be useful to read any dBASE or FoxBase database 
files.

Hearing that this is useful to people motivates me... please do email me at 
drifter.frank@gmail.com if you find this useful.

This project is loosely based on the https://github.com/smart-flex/DbfEngine 
project, for which I am most grateful. However, it has been significantly
changed, including updating the code to Java23. That project is licensed under 
the GNU Lesser General Public License 
(http://www.gnu.org/licenses/lgpl-3.0.html).

Test Data and Example Code
--------------------------

See the src/test/resources directory for some sample files.

Build
-----

This project is built with JDK 23 and [Maven 3](https://maven.apache.org/) to
build this project. 

Support
-------

For support, send an email to drifter.frank@gmail.com.

Known Limitations and Problems
------------------------------

I have no mechanism currently for tracking issues and requests relating to this
project. Please email me with any issues.

This project only reads & writes .DBF files sequentially. There is no provision
for random access to records, nor indexes, as in a real database. Updating a
record therefore requires reading the entire file and rewriting it. However, it
wouldn't be a great difficulty to add random access to it.

This project does not handle "General" or "Memo" field types.
