package com.kylecorry.trail_sense.lintchecks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.Issue

class CustomIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(UseMethodRestrictionDetector.ISSUE_USE_METHOD_RESTRICTED)

    override val api: Int = com.android.tools.lint.detector.api.CURRENT_API

    override val vendor: Vendor
        get() = Vendor(
            vendorName = "Kyle Corry",
            identifier = "com.kylecorry.trail_sense"
        )
}
