#!/bin/bash

gnuplot <<- EOF
	set xlabel "iterations"
	set ylabel "fitness"
	set term png
	set output "mutations.png"
	set key bot right

	set x2range [ * : * ] noreverse writeback
	set yrange [ * : * ] noreverse writeback
	set y2range [ * : * ] noreverse writeback
	set zrange [ * : * ] noreverse writeback
	set cbrange [ * : * ] noreverse writeback
	set rrange [ * : * ] noreverse writeback

	plot '1_flip.dat' u 1:2 w l, '3_flips.dat' u 1:2 w l, '5_flips.dat' u 1:2 w l,'bit_flip.dat' u 1:2 w l, 'mutation_adaptative.dat' u 1:2 w l
EOF

#, 'adaptive_wheel.dat'  u 1:2 w l , '5_flips_island.dat' u 1:2 w l
#enhanced autotitle box lt black linewidth 1.000 dashtype solid
