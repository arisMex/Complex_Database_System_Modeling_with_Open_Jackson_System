# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/WLambda.png'

# Titre du graphique
set title 'W en fonction de λ'
set xlabel 'λ'
set ylabel 'temps Moyen dans le system W'
set grid

# Plots
plot 'Data/00000_tempsMoyenLambda.dat' using 1:2 with linespoints title 'en File attente' 