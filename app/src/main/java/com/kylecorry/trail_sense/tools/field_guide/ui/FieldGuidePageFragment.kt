package com.kylecorry.trail_sense.tools.field_guide.ui

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.text.method.LinkMovementMethodCompat
import androidx.core.view.isVisible
import com.google.android.flexbox.FlexboxLayout
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.andromeda.core.ui.Colors
import com.kylecorry.andromeda.core.ui.useService
import com.kylecorry.andromeda.fragments.useBackgroundEffect
import com.kylecorry.andromeda.views.badge.Badge
import com.kylecorry.andromeda.views.toolbar.Toolbar
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.shared.colors.AppColor
import com.kylecorry.trail_sense.shared.extensions.TrailSenseReactiveFragment
import com.kylecorry.trail_sense.shared.extensions.useCoroutineQueue
import com.kylecorry.trail_sense.shared.io.FileSubsystem
import com.kylecorry.trail_sense.tools.field_guide.domain.FieldGuidePage
import com.kylecorry.trail_sense.tools.field_guide.domain.FieldGuidePageTag
import com.kylecorry.trail_sense.tools.field_guide.domain.FieldGuidePageTagType
import com.kylecorry.trail_sense.tools.field_guide.infrastructure.FieldGuideRepo

class FieldGuidePageFragment : TrailSenseReactiveFragment(R.layout.fragment_field_guide_page) {

    override fun update() {
        // Views
        val titleView = useView<Toolbar>(R.id.field_guide_page_title)
        val notesView = useView<TextView>(R.id.notes)
        val imageView = useView<ImageView>(R.id.image)
        val tagsView = useView<FlexboxLayout>(R.id.tags)
        val navController = useNavController()

        // State
        val page = usePage()

        // Services
        val files = useService<FileSubsystem>()

        useEffect(notesView) {
            notesView.movementMethod = LinkMovementMethodCompat.getInstance()
        }

        useEffect(page, titleView, notesView, imageView, tagsView, navController) {
            titleView.rightButton.isVisible = page?.isReadOnly == false
            titleView.rightButton.setOnClickListener {
                navController.navigate(
                    R.id.createFieldGuidePageFragment,
                    bundleOf("page_id" to page?.id)
                )
            }
            titleView.title.text = page?.name
            notesView.text = page?.notes
            val image = page?.images?.firstOrNull()
            imageView.setImageDrawable(
                image?.let { files.drawable(it) }
            )

            displayTags(tagsView, page?.tags)
        }

    }

    private val tagTypeColorMap = mapOf(
        FieldGuidePageTagType.Location to AppColor.Gray,
        FieldGuidePageTagType.Habitat to AppColor.Green,
        FieldGuidePageTagType.Classification to AppColor.Blue,
        FieldGuidePageTagType.ActivityPattern to AppColor.Yellow,
        FieldGuidePageTagType.HumanInteraction to AppColor.Brown
    )

    private fun displayTags(view: FlexboxLayout, tags: List<FieldGuidePageTag>? = null) {
        val mapper = FieldGuideTagNameMapper(requireContext())
        val sortedTags =
            (tags ?: emptyList()).sortedWith(compareBy({ it.type.ordinal }, { it.ordinal }))
        if (sortedTags.isEmpty()) {
            view.isVisible = false
            return
        }

        view.isVisible = true
        view.removeAllViews()

        val margin = Resources.dp(requireContext(), 8f).toInt()

        for (tag in sortedTags) {
            val badgeColor = (tagTypeColorMap[tag.type] ?: AppColor.Gray).color
            val foregroundColor = Colors.mostContrastingColor(Color.WHITE, Color.BLACK, badgeColor)
            val tagView = Badge(requireContext(), null).apply {
                statusImage.isVisible = false
                statusText.textSize = 12f
                setStatusText(mapper.getName(tag))
                statusText.setTextColor(foregroundColor)
                setBackgroundTint(badgeColor)
                layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, margin, margin)
                }
            }
            tagView.setOnClickListener { onTagClicked(tag) }
            view.addView(tagView)
        }
    }

    private fun usePage(): FieldGuidePage? {
        val pageId = useArgument<Long?>("page_id")
        val queue = useCoroutineQueue()
        val repo = useService<FieldGuideRepo>()
        val (page, setPage) = useState<FieldGuidePage?>(null)
        useBackgroundEffect(pageId) {
            if (pageId == null) {
                queue.replace { setPage(null) }
            } else {
                queue.replace { setPage(repo.getPage(pageId)) }
            }
        }
        return page
    }

    private fun onTagClicked(tag: FieldGuidePageTag) {
        // TODO: Handle tag click
    }

}