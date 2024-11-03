package com.kylecorry.trail_sense.tools.paths

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.click
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.clickOk
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.hasText
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.input
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.isVisible
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.not
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.string
import com.kylecorry.trail_sense.test_utils.TestUtils
import com.kylecorry.trail_sense.test_utils.TestUtils.back
import com.kylecorry.trail_sense.test_utils.TestUtils.waitFor
import com.kylecorry.trail_sense.test_utils.notifications.hasTitle
import com.kylecorry.trail_sense.test_utils.notifications.notification
import com.kylecorry.trail_sense.test_utils.views.quickAction
import com.kylecorry.trail_sense.tools.paths.infrastructure.alerts.BacktrackAlerter
import com.kylecorry.trail_sense.tools.tools.infrastructure.Tools
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class ToolPathsTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val grantPermissionRule = TestUtils.allPermissionsGranted()

    @get:Rule
    val instantExec = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        hiltRule.inject()
        TestUtils.setWaitForIdleTimeout()
        TestUtils.setupApplication()
        TestUtils.startWithTool(Tools.PATHS)
    }

    @Test
    fun verifyBasicFunctionality() {
        hasText(R.id.paths_title, string(R.string.paths))

        canUseBacktrack()
        canRenamePath()
        canViewPathDetails()
        // TODO: Add path group
        // TODO: Import path
        // TODO: Create empty path
        // TODO: Rename, export, delete, and move group
        // TODO: Rename, hide/show, export, merge, delete, simplify, and move path
        // TODO: Search path
        // TODO: Change sort
        // TODO: Quick settings tile
        verifyQuickAction()
    }

    private fun canViewPathDetails() {
        // Open the path
        click(com.kylecorry.andromeda.views.R.id.title)

        // Wait for the path to open
        hasText(R.id.path_title, "Test Path")

        // TODO: Verify the stats are shown
        // TODO: Change path styles
        // TODO: Add a point
        // TODO: Navigate
        // TODO: View path points
        // TODO: Simplify path
        // TODO: Export path
        // TODO: Hide/show path
        back()
    }

    private fun canRenamePath() {
        TestUtils.clickListItemMenu(string(R.string.rename))
        input(string(R.string.name), "Test Path")
        clickOk()
        hasText(com.kylecorry.andromeda.views.R.id.title, "Test Path")
    }

    private fun canUseBacktrack() {
        // Verify it will run every 15 minutes by default
        hasText(R.id.play_bar_title, "Off - 15m")

        // Click the start button
        click(R.id.play_btn)


        waitFor {
            notification(BacktrackAlerter.NOTIFICATION_ID).hasTitle(R.string.backtrack)
        }

        hasText(R.id.play_bar_title, "On - 15m")

        // Wait for the path to be created
        isVisible(com.kylecorry.andromeda.views.R.id.title, waitForTime = 12000)

        // Stop backtrack
        click(R.id.play_btn)

        not { notification(BacktrackAlerter.NOTIFICATION_ID) }
    }

    private fun verifyQuickAction() {
        TestUtils.openQuickActions()
        click(quickAction(Tools.QUICK_ACTION_BACKTRACK))

        waitFor {
            notification(BacktrackAlerter.NOTIFICATION_ID).hasTitle(R.string.backtrack)
        }

        // Wait for the path to be created
        isVisible(com.kylecorry.andromeda.views.R.id.title, index = 1, waitForTime = 12000)

        click(quickAction(Tools.QUICK_ACTION_BACKTRACK))

        not { notification(BacktrackAlerter.NOTIFICATION_ID) }

        TestUtils.closeQuickActions()
    }
}