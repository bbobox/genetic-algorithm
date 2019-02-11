#one-max

Exécution de l'AG (Steady-state)  pour le One-Max: ./ga_onemax.sh avec affection d'une valeur à chaque paramètre:
	-selection : -s  ( 1: best, 2: random, 3 : tournament)
	-crossover : -c  ( 1: mono-point, 2 : uniform )
	-mutation : -m   ( 0 : bit-flip , -1 : adaptive_wheel, : 1 : 1-flip , 3 : 3-flip , 5: 5-flip ) 
	-insertion : -i  ( 1: fitness , 2 : age )
	-population_size : -pop val
	-problem_size : -N  val
	-iterations_max : -max val
	-nombre_exécutions : -t val
	-mutations_proba : - pm val ( entre 0 et 1)
	-croisment_proba : - pc val ( entre 0 et 1)
	
	
	Exemple : ./ga_onemax.sh -s 3 -c 1 -mut 0 -i 1 -pc 0.1 -pm 1 -N 100 -max 1000 -t 1 -pop 10
	

Exécution de l'AG  pour le model en ile dynamique : ./islands_dynamic.sh avec affection d'une valeur à chaque paramètre:
	-problem_size : -N  val
	-iterations_max : -max val
	-nombre_exécutions : -t val
	-population_size : -pop 

	Example : ./islands_dynamic.sh -N 100 -max 500 -t 2 -pop 100
	Les resultats sont fournies dans /one_max_results sous forme de 4 fichiers (operator_1.dat,.. operator_3.dat) et 
	représente les valeurs de probabilité de chaque opérateur de mutation au cours des itérations
	
	
	
Exécution de l'AG (Steady-state)  pour le TSP (a280.TSP déjà intégré dans le programme): ./ga_tsp.sh avec affection d'une valeur à chaque paramètre:
	-selection : -s  ( 1: best, 2: random, 3 : tournament)
	-crossover : -c  ( 1: order1Crossover, 2 : pmxCrossover, 3 : cycleCrossover )
	-mutation : -m   ( 0 : swap , 2 : insert, 3 : inversion , 4 :scramble , -1: adaptive_wheel ) 
	-insertion : -i  ( 1: fitness , 2 : age )
	-population_size : -pop val
	-iterations_max : -max val
	-nombre_exécutions : -t val
	-mutations_proba : - pm val ( entre 0 et 1)
	-croisment_proba : - pc val ( entre 0 et 1)
	
	Ex: ./ga_tsp.sh -s 3 -c 1 -i 2 -mut 1 -pc 0 -pm 1 -max 100 -t 5 -pop 50
	
	
http://elib.zib.de/pub/mp-testdata/tsp/tsplib/tsplib.html
