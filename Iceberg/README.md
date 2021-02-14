# SPOTIFY ARTIST TRACKING

## APACHE DELTALAKE

#### Prérequis

Avoir lancé au moins une fois le programme Source pour récupérer les données à partir de l'API Spotify

#### Lancement d'un batch

`spark-submit --class com.esgi.datalock.Main target/scala-2.12/sbt-1.0/Iceberg-assembly-0.1.jar store "../Source/output"`

#### Lancement du ranking

`spark-submit --class com.esgi.datalock.Main target/scala-2.12/sbt-1.0/Iceberg-assembly-0.1.jar rank"`



