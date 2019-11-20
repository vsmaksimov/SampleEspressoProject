package com.moonpi.swiftnotes.rule

import android.app.Activity
import android.content.Intent
import android.support.test.rule.ActivityTestRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.tinkoff.allure.android.FailshotRule

class SwiftnotesRule<out T : Activity> constructor(
    activityClass: Class<T>,
    launchActivity: Boolean
) : TestRule {

    private val failshotRule: FailshotRule = FailshotRule()
    private val activityTestRule: ActivityTestRule<T> = ActivityTestRule(
        activityClass,
        true,
        launchActivity
    )

    fun launchActivity(intent: Intent? = null): T = activityTestRule.launchActivity(intent)

    fun finishActivity() {
        activityTestRule.finishActivity()
    }

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(failshotRule).around(activityTestRule).apply(base, description)
    }

}
