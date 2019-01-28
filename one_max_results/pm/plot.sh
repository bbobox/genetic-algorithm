#!/bin/bash

gnuplot <<- EOF
		set key out right horizontal  
		set term pdf
		set output "pm.pdf"
        set xlabel "iterations"
        set ylabel "fitness"
        
	plot 'pm=0,1.dat' u 1:2 w l, 'pm=0,3.dat' u 1:2 w l,'pm=0,7.dat' u 1:2 w l, 'pm=1.dat' u 1:2 w l
EOF


