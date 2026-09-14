package com.example.data.model

enum class CompanionType(val displayName: String, @Suppress("unused") val description: String) {
    BEAR("Barnaby Bear", "Gentle, calm, and loves cozy cups of tea"),
    BUNNY("Boba Bunny", "Playful, energetic, and always cheers you on"),
    CAT("Mochi Cat", "Quietly comforting, curled up and peaceful"),
}

object DefaultCatalog {
    val items = listOf(
        // Clothing
        CustomItem(
            id = "cloth_hoodie",
            name = "Cozy Hoodie",
            category = ItemCategory.CLOTHING,
            cost = 100,
            description = "A soft terracotta hoodie that feels like a gentle hug",
            isOwned = true,
            isEquipped = true,
        ),
        CustomItem(
            id = "cloth_sweater",
            name = "Knit Sweater",
            category = ItemCategory.CLOTHING,
            cost = 250,
            description = "Hand-knitted cream sweater with cable patterns",
        ),
        CustomItem(
            id = "cloth_pajamas",
            name = "Cloud Pajamas",
            category = ItemCategory.CLOTHING,
            cost = 200,
            description = "Light blue pajama set patterned with tiny stars",
        ),
        CustomItem(
            id = "cloth_beanie",
            name = "Soft Beanie",
            category = ItemCategory.CLOTHING,
            cost = 150,
            description = "Warm knit beanie with a fluffy pom-pom",
        ),
        CustomItem(
            id = "cloth_scarf",
            name = "Winter Scarf",
            category = ItemCategory.CLOTHING,
            cost = 180,
            description = "Long fringed amber wool scarf",
        ),
        CustomItem(
            id = "cloth_glasses",
            name = "Round Glasses",
            category = ItemCategory.CLOTHING,
            cost = 120,
            description = "Cute round wire-rim reading spectacles",
        ),

        // Accessories
        CustomItem(
            id = "acc_mug",
            name = "Steaming Mug",
            category = ItemCategory.ACCESSORY,
            cost = 80,
            description = "A ceramic mug with warm chamomile tea or cocoa",
            isOwned = true,
            isEquipped = true,
        ),
        CustomItem(
            id = "acc_plushie",
            name = "Star Plushie",
            category = ItemCategory.ACCESSORY,
            cost = 150,
            description = "A tiny soft star for moments when you need comfort",
        ),
        CustomItem(
            id = "acc_headphones",
            name = "Calm Headphones",
            category = ItemCategory.ACCESSORY,
            cost = 220,
            description = "Cozy over-ear headphones playing rain sounds",
        ),
        CustomItem(
            id = "acc_book",
            name = "Storybook",
            category = ItemCategory.ACCESSORY,
            cost = 140,
            description = "A beloved paperback full of gentle tales",
        ),

        // Furniture
        CustomItem(
            id = "furn_cushion",
            name = "Floor Cushion",
            category = ItemCategory.FURNITURE,
            cost = 100,
            description = "A soft linen floor cushion to sit on",
            isOwned = true,
            isEquipped = true,
        ),
        CustomItem(
            id = "furn_armchair",
            name = "Reading Armchair",
            category = ItemCategory.FURNITURE,
            cost = 350,
            description = "A deep, comfortable mustard velvet reading chair",
        ),
        CustomItem(
            id = "furn_futon",
            name = "Cozy Futon Bed",
            category = ItemCategory.FURNITURE,
            cost = 450,
            description = "A warm floor mattress with a quilted duvet",
        ),
        CustomItem(
            id = "furn_bookshelf",
            name = "Mini Bookshelf",
            category = ItemCategory.FURNITURE,
            cost = 400,
            description = "Wooden shelf stacked with cozy books and plants",
        ),

        // Rugs
        CustomItem(
            id = "rug_cream",
            name = "Round Wool Rug",
            category = ItemCategory.RUG,
            cost = 120,
            description = "A round textured rug warming up the floor",
            isOwned = true,
            isEquipped = true,
        ),
        CustomItem(
            id = "rug_terracotta",
            name = "Sunset Weave Rug",
            category = ItemCategory.RUG,
            cost = 260,
            description = "A woven geometric rug with warm terracotta accents",
        ),

        // Plants
        CustomItem(
            id = "plant_monstera",
            name = "Potted Monstera",
            category = ItemCategory.PLANT,
            cost = 160,
            description = "A vibrant green leafy plant in a ceramic pot",
        ),
        CustomItem(
            id = "plant_ivy",
            name = "Hanging Ivy",
            category = ItemCategory.PLANT,
            cost = 210,
            description = "Cascading greenery trailing along the wall",
        ),
        CustomItem(
            id = "plant_bonsai",
            name = "Peace Bonsai",
            category = ItemCategory.PLANT,
            cost = 320,
            description = "A carefully shaped miniature tree of mindfulness",
        ),

        // Lighting
        CustomItem(
            id = "light_lamp",
            name = "Warm Desk Lamp",
            category = ItemCategory.LIGHTING,
            cost = 180,
            description = "Soft glowing table lamp with a honey lampshade",
        ),
        CustomItem(
            id = "light_fairy",
            name = "Fairy String Lights",
            category = ItemCategory.LIGHTING,
            cost = 280,
            description = "A delicate string of warm golden glowing bulbs",
        ),

        // Wall Decor
        CustomItem(
            id = "wall_window",
            name = "Sunlit Window",
            category = ItemCategory.WALL_DECOR,
            cost = 380,
            description = "A wooden arched window with drifting pastel clouds",
        ),
        CustomItem(
            id = "wall_poster",
            name = "Breathe Mountain Print",
            category = ItemCategory.WALL_DECOR,
            cost = 160,
            description = "Minimalist wall art reading 'One breath at a time'",
        ),
    )
}
