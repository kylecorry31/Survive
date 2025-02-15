package com.kylecorry.trail_sense.lintchecks

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getParentOfType

class UseMethodRestrictionDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> {
        return listOf(
            "useMemo",
            "useEffect",
            "useState",
            "useAndroidContext",
            "useService",
            "useBackgroundEffect",
            "useCallback",
            "useCoroutineQueue",
            "useView",
            "useArgument",
            "useArguments"
        )
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val methodName = method.name

        // Check if the method starts with "use"
        if (!methodName.startsWith("use")) return

        // Get the surrounding method (if any)
        val containingMethod = node.getParentOfType(UMethod::class.java, true)

        if (containingMethod != null) {
            val callerName = containingMethod.name
            // Allowed callers: methods starting with "use" or exactly "update"
            if (callerName.startsWith("use") || callerName == "update") {
                return
            }
        } else {
            // If there's no containing method, it's likely in an anonymous function
            context.report(
                ISSUE_USE_METHOD_RESTRICTED,
                node,
                context.getLocation(node),
                "Hooks should only be called from other hooks or in the 'update()' method directly."
            )
            return
        }

        // If not called from a valid method, report the issue
        context.report(
            ISSUE_USE_METHOD_RESTRICTED,
            node,
            context.getLocation(node),
            "Hooks should only be called from other hooks or in the 'update()' method directly."
        )
    }

    companion object {
        val ISSUE_USE_METHOD_RESTRICTED: Issue = Issue.create(
            id = "UseMethodRestricted",
            briefDescription = "Restricted usage of 'use*' methods",
            explanation = "Hooks should only be called from other hooks or in the 'update()' method directly.",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                UseMethodRestrictionDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
