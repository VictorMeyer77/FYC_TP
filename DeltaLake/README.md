# SPOTIFY ARTIST TRACKING

## DELTA LAKE

#### Prérequis

Avoir lancer au moins une fois le programme source.

#### Lancement de la stream

    spark-submit   --class com.esgi.datalock.Main target/scala-2.12/DeltaLake-assembly-0.1.jar rank

#### Lancement du batch

    spark-submit   --class com.esgi.datalock.Main target/scala-2.12/DeltaLake-assembly-0.1.jar store "../Source/output"
    
Le chemin précisé est celui où sont stockées les données brutes
