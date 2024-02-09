# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/0_NombreRequetes.png'

# Titre du graphique
set title 'Nombre de requetes / ms'
set xlabel 'T'
set ylabel 'Nombre de Requetes'
set grid

# Plots
plot 'Data/0_nbRequetes.dat' using 1:2 with linespoints title ' dans le system', 'Data/0_nbRequetesEntrees.dat' using 1:2 with linespoints title 'Total Entrees','Data/0_nbRequetesSorties.dat' using 1:2 with linespoints title ' Sorties'