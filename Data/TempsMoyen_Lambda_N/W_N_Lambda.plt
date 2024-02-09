# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/W_En_Fct_de_N_et_Lambda.png'

# Titre du graphique
set title 'W en fonction de λ et n'
set xlabel 'λ'
set ylabel 'temps Moyen dans le system W'
set grid

# Plots
plot 'Data/TempsMoyen_Lambda_N/tempsMoyen_N_lambda_1.dat' using 1:2 with linespoints title '1 Servers','Data/TempsMoyen_Lambda_N/tempsMoyen_N_lambda_2.dat' using 1:2 with linespoints title '2 Servers','Data/TempsMoyen_Lambda_N/tempsMoyen_N_lambda_3.dat' using 1:2 with linespoints title '3 Servers','Data/TempsMoyen_Lambda_N/tempsMoyen_N_lambda_4.dat' using 1:2 with linespoints title '4 Servers'