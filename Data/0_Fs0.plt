# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/0_Fs0.png'

# Titre du graphique
set title 'Fs0'
set xlabel 'T'
set ylabel 'Nombre de Requetes'
set grid

# Plots
plot 'Data/0_Fs0_file.dat' using 1:2 with linespoints title 'en File attente', 'Data/0_Fs0_traitement.dat' using 1:2 with linespoints title 'Traitées','Data/0_Fs0_sorties.dat' using 1:2 with linespoints title 'Sorties'