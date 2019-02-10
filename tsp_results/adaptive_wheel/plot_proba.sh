#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "probabilités"
        set term pdf
        set output "mutations_operators.pdf"
        
	plot 'operator_0.dat' u 1:2 w l, 'operator_1.dat' u 1:2 w l, 'operator_2.dat' u 1:2 w l ,  'operator_3.dat' u 1:2 w l
 #'5_flips_island.dat' u 1:2 w l
EOF
