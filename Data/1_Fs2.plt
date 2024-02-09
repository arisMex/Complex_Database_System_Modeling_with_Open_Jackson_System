# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/1_Fs2.png'

# Titre du graphique
set title 'Fs2'
set xlabel 'T'
set ylabel 'Nombre de Requetes'
set grid

# Plots
plot 'Data/1_Fs2_file.dat' using 1:2 with linespoints title 'en File attente', 'Data/1_Fs2_traitement.dat' using 1:2 with linespoints title 'Traitées','Data/1_Fs2_sorties.dat' using 1:2 with linespoints title 'Sorties'