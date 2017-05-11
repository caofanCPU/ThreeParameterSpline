# [ThreeParameterSpline](https://github.com/caofanCPU/ThreeParameterSpline)  
[UJMP + mysql connection pools + IO Stream + TreeMap]

## Version 0.5 
***
## Introduction
&ensp;&emsp;&ensp;&emsp;This project regards the study of [**cubic parametric spline interpolation function**](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/doc/三次参数样条曲线算法原理.pdf) as the breakthrough point, by choosing suitable type value points to construct the cam contour curve fitting, with the purpose to design a complete cam follower grinding task simulation algorithm, which of the cam with the grinding dynamic mathematical modeling. And the point of tangency dynamic follows **fitting error compensation**. With the use of Matlab simulation and **Java programming** to simulate the cam follower grinding task, the cam profile curve function analytic formula with high accuracy can be obtained. Through analyzing the cam contour curve fitting error data, determination of the cam grinding dynamic mathematical model with dynamic fully meets the requirements of the precision of the work piece cam designing for the high precision cam product, which will lay a strong foundation for the mass production manufacture.
***
## Components
- [regular expression](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/src/com/xyb/cf/DataCleaning.java)
- [IO Stream including File read&write](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/src/com/xyb/cf/FileOperation.java)
- [Java Matrix operation](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/src/com/xyb/cf/ParameterSolution.java)
- [GUI plot](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/src/com/xyb/cf/DataVisualization.java)
- [Database operation by c3p0Utils and mysql connector](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/src/com/xyb/util/C3P0Util.java)
- [Encapsulating Data to a entity into TreeMap storage](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/src/com/xyb/domain/Parameter.java)
***
##Highligths
![CurveFittingPlot](http://i1.piimg.com/588926/52e76ec44ec0f763.jpg)  
![ParametersDB](http://i1.piimg.com/588926/06f39e1a58bb6133.jpg)
***
## Remarks
&ensp;&emsp;&ensp;&emsp;Before your quick start of this project by Java, it will be helpful that reading the reference document [**Algorithm principle**](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/doc/三次参数样条曲线算法原理.pdf) carefully to know the algorithm principle and steps of coding. If you're all eagerness to see results, just executive [`ThreeParameterSplineP.jar`](https://github.com/caofanCPU/ThreeParameterSpline/tree/master/jar/ThreeParameterSplineP.jar). 