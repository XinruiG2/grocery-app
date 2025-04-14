package com.neu.mobileapplicationdevelopment202430

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit rule to replace the Main dispatcher with a TestDispatcher for unit tests.
 * Provides a TestScope bound to this dispatcher.
 */
@ExperimentalCoroutinesApi
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(), // Creates the dispatcher
) : TestWatcher() {

    // Creates a scope bound to the dispatcher managed by this rule
    val testScope = TestScope(testDispatcher)

    override fun starting(description: Description) {
        //super.starting(description)
        // Set the Main dispatcher before the test runs (and before @Before if rules run first)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        //super.finished(description)
        // Reset the Main dispatcher after the test finishes
        Dispatchers.resetMain()
    }
}