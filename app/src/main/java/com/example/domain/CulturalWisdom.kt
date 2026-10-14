package com.example.domain

data class ProverbItem(
    val moore: String,
    val french: String,
    val explanation: String,
    val category: String
)

object CulturalWisdom {
    val proverbs = listOf(
        ProverbItem(
            moore = "Ned yaa a menga ninsaal.",
            french = "L'homme est l'artisan de sa propre dignité.",
            explanation = "La grandeur d'un homme ne se mesure pas à ses tentations, mais à sa capacité à régner sur ses instincts plutôt que de leur obéir.",
            category = "Discipline"
        ),
        ProverbItem(
            moore = "Bi yaa yĩmde, bĩisim yaa kãenkãe.",
            french = "La petite graine d'effort devient le grand baobab inébranlable.",
            explanation = "Chaque minute où tu dis NON à l'illusion, ton cerveau reconstruit ses circuits de volonté et de clarté.",
            category = "Patience"
        ),
        ProverbItem(
            moore = "Ra zoe tãnd bɩ f na n paam n zĩnd a zug ye.",
            french = "Ne fuis pas la montagne d'épreuves, tu t'assoiras bientôt à son sommet.",
            explanation = "L'inconfort passager de l'envie ne dure que quelques minutes. Respire et laisse la vague passer sans céder.",
            category = "Courage"
        ),
        ProverbItem(
            moore = "Gom-biiga pa tõe n kʋ sãamb ye.",
            french = "La parole tenue envers soi-même est le bouclier suprême.",
            explanation = "Honore l'engagement que tu as pris envers ton futur toi. Tu vaux bien plus qu'un plaisir éphémère sur un écran.",
            category = "Honneur"
        ),
        ProverbItem(
            moore = "Wẽnnaam kõo ned fãa a pãng.",
            french = "La force nécessaire pour vaincre l'obscurité est déjà en toi.",
            explanation = "Tu n'es pas une victime de tes pulsions. Tu es un guerrier en pleine reconstruction.",
            category = "Renaissance"
        ),
        ProverbItem(
            moore = "Sõmbr yaa pãng, sãam-n-taar yaa yelle.",
            french = "L'union et la franchise apportent la délivrance.",
            explanation = "La honte fleurit dans le secret. En parlant à un frère ou au coach, le piège de la tentation se dissout.",
            category = "Fraternité"
        )
    )

    fun getRandomProverb(): ProverbItem = proverbs.random()
}
