#!/bin/bash
cd src;


while [[ "$#" > 0 ]]; do case $1 in
  -s|--selection) selection="$2"; shift;;
  -c|--crossover) crossover="$2"; shift;;
  -mut|--mutation) mutation="$2"; shift;;
  -i|--insertion) insertion="$2"; shift;;
  -t|--tests) tests="$2"; shift;;
  -N|--size) size="$2"; shift;;
  -max|--size) iter_max="$2"; shift;;
  -pc|--size) pc="$2"; shift;;
  -pm|--size) pm="$2"; shift;;

  *) echo "Unknown parameter passed: $1"; exit 1;;
esac; shift; done

javac ga_solver/*.java;
java ga_solver.Solver $selection $crossover $mutation $insertion $pc $pm $size $iter_max $tests > "../results/age.dat";


#gnuplot <<- EOF
#        set xlabel "iterations"
#        set ylabel "fitness"
#        set title "Fitness optimal en fonctions du nombre d'iterations"
#        set term png
#        set output "../results/1_flip.png"

#	plot '../results/results.txt' u 1:2 w l
#EOF
