# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/0_TempsMoyen.png'

# Titre du graphique
set title 'Temps moyen que passe unrrequete dans le systeme à l-instant i'
set xlabel 'T'
set ylabel 'Temps Moyen'
set grid

# Plots
plot 'Data/0_tempsMoyenReq.dat' using 1:2 with linespoints title ' temps moyen '