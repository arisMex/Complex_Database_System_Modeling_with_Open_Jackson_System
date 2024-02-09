# MIS_ProjetFinal

**Introduction : **

Dans le cadre de ce projet, nous explorons le fonctionnement d'une base de données
distribuée sur plusieurs serveurs. Les requêtes des utilisateurs, générées selon une loi
de Poisson, sont initialement dirigées vers un coordinateur. Ce dernier a pour mission
de répartir ces requêtes de manière stratégique parmi les différents serveurs, en
suivant des probabilités déterminées (qi). Chaque serveur dispose d'un temps de
traitement spécifique, caractérisé par une distribution exponentielle (μi). À la
conclusion du traitement dans un serveur donné, la requête a deux options : elle peut
quitter le système avec une probabilité (pi) ou retourner au coordinateur pour être
redirigée ultérieurement. L'étude approfondie de ce système s'articule autour de divers
paramètres tels que le nombre de serveurs, les temps de traitement de ces serveurs, les
taux d'arrivée des requêtes, ainsi que les probabilités de sortie du système.

L'objectif central du projet est de mesurer la stabilité du système dans un état
permanent. Cette évaluation repose sur une analyse approfondie des paramètres clés
qui influent sur le fonctionnement du système et à identifier les configurations
optimales et à déterminer les conditions sous lesquelles le système atteint un régime
permanent stable.

**Le rapport complet du projet est disponible  [ici](Rapport_du_Projet.pdf).**


