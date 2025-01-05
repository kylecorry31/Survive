package com.kylecorry.trail_sense.tools.species_catalog

import android.content.Context
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.shared.text.TextUtils

object BuiltInFieldGuide {

    private data class BuiltInFieldGuidePage(
        val resourceId: Int,
        val imagePath: String,
        val tags: List<FieldGuidePageTag>
    )

    private val pages = listOf(
        BuiltInFieldGuidePage(
            R.raw.squirrel,
            "field_guide/squirrel.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Mammal,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Desert,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Urban,
                FieldGuidePageTag.Tundra
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.sunfish,
            "field_guide/sunfish.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Fish,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.black_bass,
            "field_guide/black_bass.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Fish,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.carp,
            "field_guide/carp.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Fish,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.crayfish,
            "field_guide/crayfish.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Crustacean,
                FieldGuidePageTag.Wetland,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.crab,
            "field_guide/Crab.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Crustacean,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.clam,
            "field_guide/Clam.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Wetland,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.mussel,
            "field_guide/Mussel.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Wetland,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.littorinidae,
            "field_guide/Littorinidae.webp",
            listOf(FieldGuidePageTag.Animal, FieldGuidePageTag.Marine, FieldGuidePageTag.Freshwater)
        ),
        BuiltInFieldGuidePage(
            R.raw.leporidae,
            "field_guide/Leporidae.webp",
            listOf(
                FieldGuidePageTag.Antarctica,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Mammal,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Desert,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Wetland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Tundra
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.dasyprocta,
            "field_guide/Dasyprocta.webp",
            listOf(
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Mammal,
                FieldGuidePageTag.Grassland
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.muridae,
            "field_guide/Muridae.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Mammal,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Desert,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Freshwater,
                FieldGuidePageTag.Tundra
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.tetraonini,
            "field_guide/Tetraonini.webp",
            listOf(
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Bird,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Tundra
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.isoptera,
            "field_guide/Isoptera.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Antarctica,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Insect,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Wetland,
                FieldGuidePageTag.Urban,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.acridomorpha,
            "field_guide/Acridomorpha.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Insect,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Desert,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Urban,
                FieldGuidePageTag.Marine
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.grylloidea,
            "field_guide/Grylloidea.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Insect,
                FieldGuidePageTag.Cave
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.formicidae,
            "field_guide/Formicidae.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Antarctica,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Insect,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Desert,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Wetland
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.lumbricina,
            "field_guide/Lumbricina.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.coleoptera,
            "field_guide/Coleoptera.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Antarctica,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Animal,
                FieldGuidePageTag.Insect,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Desert,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Wetland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater,
                FieldGuidePageTag.Tundra
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.toxicodendron_radicans,
            "survival_guide/poison_ivy.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Wetland
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.toxicodendron_diversilobum,
            "field_guide/Toxicodendron diversilobum.webp",
            listOf(
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.toxicodendron_vernix,
            "field_guide/Toxicodendron vernix.webp",
            listOf(FieldGuidePageTag.Plant, FieldGuidePageTag.Wetland)
        ),
        BuiltInFieldGuidePage(
            R.raw.urtica_dioica,
            "survival_guide/stinging_nettle.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Wetland
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.taraxacum,
            "field_guide/Taraxacum.webp",
            listOf(
                FieldGuidePageTag.Antarctica,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Urban
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.laminariales,
            "field_guide/Laminariales.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Antarctica,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Marine
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.rumex_acetosa,
            "field_guide/Rumex acetosa.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Grassland
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.trifolium,
            "field_guide/Trifolium.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Urban,
                FieldGuidePageTag.Marine
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.typha,
            "field_guide/Typha.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Wetland,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.bambusoideae,
            "field_guide/Bambusoideae.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.plantago_major,
            "field_guide/Plantago major.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Plant,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Urban
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.laetiporus,
            "field_guide/Laetiporus.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Fungus,
                FieldGuidePageTag.Forest
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.boletales,
            "field_guide/Boletales.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Fungus,
                FieldGuidePageTag.Forest
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.morchella,
            "field_guide/Morchella.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Fungus,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Urban,
                FieldGuidePageTag.Cave
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.pleurotus,
            "field_guide/Pleurotus.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.SouthAmerica,
                FieldGuidePageTag.Fungus
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.calvatia_gigantea,
            "field_guide/Calvatia gigantea.webp",
            listOf(
                FieldGuidePageTag.Fungus,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.hericium_erinaceus,
            "field_guide/Hericium erinaceus.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Fungus,
                FieldGuidePageTag.Mountain
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.cladonia_rangiferina,
            "field_guide/Cladonia rangiferina.webp",
            listOf(
                FieldGuidePageTag.Fungus,
                FieldGuidePageTag.Forest,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Tundra
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.chert,
            "field_guide/Chert.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.basalt,
            "field_guide/Basalt.webp",
            listOf(
                FieldGuidePageTag.Africa,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.granite,
            "field_guide/Granite.webp",
            listOf(
                FieldGuidePageTag.Antarctica,
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Grassland,
                FieldGuidePageTag.Mountain,
                FieldGuidePageTag.Freshwater
            )
        ),
        BuiltInFieldGuidePage(
            R.raw.obsidian,
            "field_guide/Obsidian.webp",
            listOf(
                FieldGuidePageTag.Asia,
                FieldGuidePageTag.Australia,
                FieldGuidePageTag.Europe,
                FieldGuidePageTag.NorthAmerica,
                FieldGuidePageTag.Urban,
                FieldGuidePageTag.Marine,
                FieldGuidePageTag.Freshwater
            )
        ),
    )
    
    fun getFieldGuide(context: Context): List<FieldGuidePage> {
        return pages.mapIndexed { index, page ->
            val text = TextUtils.loadTextFromResources(context, page.resourceId)
            val lines = text.split("\n")
            val name = lines.first()
            val notes = lines.drop(1).joinToString("\n").trim()
            FieldGuidePage(
                -index.toLong(),
                name,
                listOf("android-assets://${page.imagePath}"),
                page.tags,
                notes
            )
        }
    }
}