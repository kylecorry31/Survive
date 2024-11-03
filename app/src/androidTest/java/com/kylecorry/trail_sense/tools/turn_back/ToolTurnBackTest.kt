package com.kylecorry.trail_sense.tools.turn_back

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.click
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.hasText
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.not
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.optional
import com.kylecorry.trail_sense.test_utils.AutomationLibrary.string
import com.kylecorry.trail_sense.test_utils.TestUtils
import com.kylecorry.trail_sense.test_utils.TestUtils.handleExactAlarmsDialog
import com.kylecorry.trail_sense.test_utils.TestUtils.pickTime
import com.kylecorry.trail_sense.tools.tools.infrastructure.Tools
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ToolTurnBackTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val grantPermissionRule = TestUtils.mainPermissionsGranted()

    @get:Rule
    val instantExec = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        hiltRule.inject()
        TestUtils.setupApplication()
        TestUtils.setWaitForIdleTimeout()
        TestUtils.startWithTool(Tools.TURN_BACK)
    }

    @Test
    fun verifyBasicFunctionality() {
        hasText(R.string.instructions, string(R.string.time_not_set))

        // Enter a time
        click(R.id.edittext)
        pickTime(8, 0, false)

        handleExactAlarmsDialog()

        hasText(R.id.edittext, "8:00 PM", contains = true)
        hasText(
            R.id.instructions,
            Regex("Turn around by \\d+:\\d+ (AM|PM) \\(.*\\) to return at 8:00 PM")
        )
        click(R.id.cancel_button)
        hasText(R.id.instructions, string(R.string.time_not_set))

        // Return before dark
        click(R.id.sunset_button)

        // Loading dialog is possible
        optional {
            hasText(string(R.string.loading))
            not(12000) { hasText(string(R.string.loading), waitForTime = 0) }
        }

        handleExactAlarmsDialog()
        hasText(R.id.edittext, Regex("\\d+:\\d+ (AM|PM).*"))
    }

}