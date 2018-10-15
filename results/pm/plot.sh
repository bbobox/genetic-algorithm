#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:150]
        set title "Analyse des probabilités de mutation"
        set term png
        set output "pm.png"
        
	plot 'pm_case1.dat' u 1:2 w l, 'pm_case2.dat' u 1:2 w l, 'pm_case3.dat' u 1:2 w l,'pm_case4.dat' u 1:2 w l, 'pm_case02.dat' u 1:2 w l
EOF
