package com.kylecorry.trail_sense.tools.species_catalog

import com.kylecorry.trail_sense.shared.data.Identifiable

enum class FieldGuidePageTagType {
    Continent,
    Habitat,
    Taxonomy
}

enum class FieldGuidePageTag(override val id: Long, type: FieldGuidePageTagType) : Identifiable {
    Africa(1, FieldGuidePageTagType.Continent),
    Antarctica(2, FieldGuidePageTagType.Continent),
    Asia(3, FieldGuidePageTagType.Continent),
    Australia(4, FieldGuidePageTagType.Continent),
    Europe(5, FieldGuidePageTagType.Continent),
    NorthAmerica(6, FieldGuidePageTagType.Continent),
    SouthAmerica(7, FieldGuidePageTagType.Continent),
    Plant(8, FieldGuidePageTagType.Taxonomy),
    Animal(9, FieldGuidePageTagType.Taxonomy),
    Fungus(10, FieldGuidePageTagType.Taxonomy),
    Bird(11, FieldGuidePageTagType.Taxonomy),
    Mammal(12, FieldGuidePageTagType.Taxonomy),
    Reptile(13, FieldGuidePageTagType.Taxonomy),
    Amphibian(14, FieldGuidePageTagType.Taxonomy),
    Fish(15, FieldGuidePageTagType.Taxonomy),
    Insect(16, FieldGuidePageTagType.Taxonomy),
    Arachnid(17, FieldGuidePageTagType.Taxonomy),
    Crustacean(18, FieldGuidePageTagType.Taxonomy),
    Mollusk(19, FieldGuidePageTagType.Taxonomy),
    Forest(20, FieldGuidePageTagType.Habitat),
    Desert(21, FieldGuidePageTagType.Habitat),
    Grassland(22, FieldGuidePageTagType.Habitat),
    Wetland(23, FieldGuidePageTagType.Habitat),
    Mountain(24, FieldGuidePageTagType.Habitat),
    Urban(25, FieldGuidePageTagType.Habitat),
    Marine(26, FieldGuidePageTagType.Habitat),
    Freshwater(27, FieldGuidePageTagType.Habitat),
    Cave(28, FieldGuidePageTagType.Habitat),
    Tundra(29, FieldGuidePageTagType.Habitat)
}