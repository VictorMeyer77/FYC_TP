
    # SPOTIFY ARTIST TRACKING

    ## APACHE HUDI

    #### Prérequis

    Avoir lancé au moins une fois le programme Source pour récupérer les données à partir de l'API Spotify

    #### Construction du projet

    `sbt clean assembly`

    #### Lancement du programme de ranking
    
    `spark-submit   --class com.esgi.datalock.Main target/scala-2.12/Hudi-assembly-0.1.jar  "../Source/output"</mark>`
