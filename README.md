#### *ARMA times series generator*

This application generates some types ARMA time series models according to described 
in the book "_Time series analysis, forecasting and control_" from Box & Jenkins. 
The inputs required by the user are:
- φ1  for autoregressive time series AR(1);
- φ1  and φ2 for autoregressive time series AR(2);
- θ1 for moving average time series MA(1);
- θ1 and θ2  for moving average time series MA(2);  
- φ1 and θ1  for autoregressive/moving average time series ARMA(1,1);    
- Sample size, minimum value is 50 and maximum value is 250;
- Mean of the time series;
- at() noise variance, value must be greater than zero.

**External libs required:**

-   GUI - MigLayout https://www.miglayout.com/
-   Gráficos - JFreChart https://www.jfree.org/jfreechart/
-   Exportação de tabelas para o Excel - Apache POI https://poi.apache.org/

For your convenience there is an runnable jar file in the _runnable_jar_ directory.
Tested using Windows 10 Home Single Language 21H2.

__Built using Java S.E. 1.7__

Some application screenshots:

 **MAIN WINDOW**
![main window](https://github.com/rgiovann/image-repo/blob/main/TS_FIG1.jpg)


  **CALCULATION RESULTS**

![calculation results](https://github.com/rgiovann/image-repo/blob/main/TS_FIG2.jpg)

**GRAPH**
![graph](https://github.com/rgiovann/image-repo/blob/main/TS_FIG3.jpg)
