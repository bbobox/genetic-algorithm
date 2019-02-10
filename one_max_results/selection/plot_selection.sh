#!/bin/bash

gnuplot <<- EOF
	set key bot right 
        set xlabel "iterations"
        set ylabel "fitness"
        set term pdf
        set output "selections.pdf"

	set xrange [ * : * ] noreverse writeback
	set x2range [ * : * ] noreverse writeback
	set yrange [ * : * ] noreverse writeback
	set y2range [ * : * ] noreverse writeback
	set zrange [ * : * ] noreverse writeback
	set cbrange [ * : * ] noreverse writeback
	set rrange [ * : * ] noreverse writeback
        
	plot 'best.dat' u 1:2 w l, 'random.dat' u 1:2 w l, 'tournament.dat' u 1:2 w l
EOF
