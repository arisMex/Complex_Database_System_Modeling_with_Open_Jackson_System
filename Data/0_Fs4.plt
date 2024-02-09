# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/0_Fs4.png'

# Titre du graphique
set title 'Fs4'
set xlabel 'T'
set ylabel 'Nombre de Requetes'
set grid

# Plots
plot 'Data/0_Fs4_file.dat' using 1:2 with linespoints title 'en File attente', 'Data/0_Fs4_traitement.dat' using 1:2 with linespoints title 'Traitées','Data/0_Fs4_sorties.dat' using 1:2 with linespoints title 'Sorties'