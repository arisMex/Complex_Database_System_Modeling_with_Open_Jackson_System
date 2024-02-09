# Nom du fichier de sortie de l'image
set terminal png
set output 'Graphs/0_ReqTempsTraitement.png'

# Titre du graphique
set title ' Temps que passe la requete i dans le system (i>= T/2) '
set xlabel 'Requete i '
set ylabel 'Temps (ms)'
set grid

# Plots
plot 'Data/0_Req2TempsTraitement.dat' using 1:2 with boxes title 'all','Data/0_ReqTempsTraitement.dat' using 1:2 with boxes title 'Regime stationnaire',