#!/bin/bash

gnuplot <<- EOF
	set xlabel "iterations"
	set ylabel "fitness"
	set term pdf
	set output "mutations_tsp.pdf"
	set key right

	set x2range [ * : * ] noreverse writeback
	set yrange [ * : * ] noreverse writeback
	set y2range [ * : * ] noreverse writeback
	set zrange [ * : * ] noreverse writeback
	set cbrange [ * : * ] noreverse writeback
	set rrange [ * : * ] noreverse writeback

	plot 'swap.dat' u 1:2 w l, 'insert.dat' u 1:2 w l, 'inversion.dat' u 1:2 w l,'scramble.dat' u 1:2 w l, 'adaptative_mutation.dat' u 1:2 w l
EOF

#, 'adaptive_wheel.dat'  u 1:2 w l , '5_flips_island.dat' u 1:2 w l
#enhanced autotitle box lt black linewidth 1.000 dashtype solid
