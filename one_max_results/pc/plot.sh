#!/bin/bash

gnuplot <<- EOF
		set key out bot horizontal  
		set term pdf
		set output "pc.pdf"
        set xlabel "iterations"
        set ylabel "fitness"
        
	plot 'pc=0,1.dat' u 1:2 w l, 'pc=0,3.dat' u 1:2 w l, 'pc=0,7.dat'  u 1:2 w l, 'pc=1.dat' u 1:2 w l
EOF


#!/bin/bash


