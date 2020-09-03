# DBDemoOneTable


Open DBDemoOneTable in NetBeans

Run the project:

<img width="326" alt="image" src="https://user-images.githubusercontent.com/56609358/92178718-016fd500-edf8-11ea-9ff2-ea95b293144f.png"><img width="331" alt="image" src="https://user-images.githubusercontent.com/56609358/92178689-ed2bd800-edf7-11ea-963b-dd600eb70520.png">










JavaFX demo program to manipulate a JavaDB database using JDBC
- organized in layers:
  GUI <-> Controller <-> Manager / Model / Exceptions / Setup (using create.sql file) <-> Database
- can modify for any other data domain -- must change files in each layer

Must have recent JDK, otherwise the Alert class is not recognized:
- Close NetBeans
- Download recent JDK and install
- Run Wordpad as Administrator
- Open C:\Program Files\Netbeans folder\etc\netbeans.conf
- edit line with jdkhome setting and replace old version with new version
  (last two digits of line)
- Open NetBeans

Uses embedded JavaDB driver. Once database is created, can inspect with NetBeans:
- Run program, Admin tab, Create DB, close program
- Files tab, DB folder, right-click, Properties, All Files ..., copy entire path
- Services tab, Databases, Drivers, Java DB (Embedded), right-click, Connect Using...,
  paste path as database name, Test Connection, Next, Next, Finish
- Services tab, Databases, list of connections, select newly created connection,
  open, APP, Tables, select a table, right-click, View Data, edit as desired
- NOTE: must disconnect connection (right-click) before running program again
