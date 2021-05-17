package solutions.bellatrix.core.plugins.junit

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import solutions.bellatrix.core.plugins.TestResult

class TestResultListener : TestWatcher {
    override fun testSuccessful(context: ExtensionContext?) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.SUCCESS)
        super.testSuccessful(context)
    }

    override fun testFailed(context: ExtensionContext?, cause: Throwable?) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.FAILURE)
        super.testFailed(context, cause)
    }
}