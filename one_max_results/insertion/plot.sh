#!/bin/bash

gnuplot <<- EOF
		set key bmargin horizontal Right  enhanced autotitle box lt black linewidth 1.000 dashtype solid
        set title "Analyse des opérateurs d'insertion"
        set term pdf
        set output "insertions.pdf"
        
        set xrange [ * : * ] noreverse writeback
		set x2range [ * : * ] noreverse writeback
		set yrange [ * : * ] noreverse writeback
		set y2range [ * : * ] noreverse writeback
		set zrange [ * : * ] noreverse writeback
		set cbrange [ * : * ] noreverse writeback
		set rrange [ * : * ] noreverse writeback
		plot 'age.dat' u 1:2 w l, 'fitness.dat' u 1:2 w l
EOF
