#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:400]
        set title "Analyse des probabilités de mutation en bit-flip"
        set term png
        set output "pm.png"
        
	plot 'pm=0,1.dat' u 1:2 w l, 'pm=0,3.dat' u 1:2 w l, 'pm=0,5.dat' u 1:2 w l,'pm=0,8.dat' u 1:2 w l, 'pm=1.dat' u 1:2 w l
EOF
