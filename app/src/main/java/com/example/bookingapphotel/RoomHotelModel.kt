package com.example.bookingapphotel

class RoomHotelModel (
    //ces val doivent être du mêe nom que celle dans la base de donnée
    //pour plus de simplicité dans la récupération de ces éléments
    val hotel: String = "Nom de l'hotel",
    val petite_description: String = "petite description de l'hotel",
    val tarif_par_nuit: String = "Prix",
    val photo_chambre: String = "https://www.google.com/imgres?imgurl=https%3A%2F%2Fwww.guidesulysse.com%2Fimages%2Fdestinations%2FiStock-1137803766.jpg&imgrefurl=https%3A%2F%2Fwww.guidesulysse.com%2Ffr%2Ffiche-contenu.aspx%3Fid%3D470&tbnid=G8HYeux3WUR3iM&vet=12ahUKEwjSl9CW8LD9AhVVrycCHfjiAYYQMygDegUIARDjAQ..i&docid=v-fQiRFg6YyWgM&w=1258&h=833&q=barcelone&ved=2ahUKEwjSl9CW8LD9AhVVrycCHfjiAYYQMygDegUIARDjAQ"
    )