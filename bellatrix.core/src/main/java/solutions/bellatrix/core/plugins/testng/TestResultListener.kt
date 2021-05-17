package solutions.bellatrix.core.plugins.testng

import org.testng.ITestListener
import org.testng.ITestResult
import solutions.bellatrix.core.plugins.TestResult

class TestResultListener : ITestListener {
    override fun onTestSuccess(result: ITestResult?) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.SUCCESS)
    }

    override fun onTestFailure(result: ITestResult?) {
        BaseTest.CURRENT_TEST_RESULT.set(TestResult.FAILURE)
    }
}