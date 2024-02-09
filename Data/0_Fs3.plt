# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/0_Fs3.png'

# Titre du graphique
set title 'Fs3'
set xlabel 'T'
set ylabel 'Nombre de Requetes'
set grid

# Plots
plot 'Data/0_Fs3_file.dat' using 1:2 with linespoints title 'en File attente', 'Data/0_Fs3_traitement.dat' using 1:2 with linespoints title 'Traitées','Data/0_Fs3_sorties.dat' using 1:2 with linespoints title 'Sorties'