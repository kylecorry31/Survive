package com.kylecorry.trail_sense.tools.species_catalog

import com.kylecorry.trail_sense.shared.data.Identifiable

enum class FieldGuidePageTag(override val id: Long) : Identifiable {
    Africa(1),
    Antarctica(2),
    Asia(3),
    Australia(4),
    Europe(5),
    NorthAmerica(6),
    SouthAmerica(7),
    Plant(8),
    Animal(9),
    Fungus(10),
    Bird(11),
    Mammal(12),
    Reptile(13),
    Amphibian(14),
    Fish(15),
    Insect(16),
    Arachnid(17),
    Crustacean(18),
    Mollusk(19),
    Forest(20),
    Desert(21),
    Grassland(22),
    Wetland(23),
    Mountain(24),
    Urban(25),
    Marine(26),
    Freshwater(27),
    Cave(28),
    Tundra(29)
}