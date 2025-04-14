package com.neu.mobileapplicationdevelopment202430



import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.LoginResponse
import com.neu.mobileapplicationdevelopment202430.viewmodel.LoginVM
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()  // Allows LiveData to execute synchronously

    private lateinit var repository: FoodRepository
    private lateinit var viewModel: LoginVM

    @Before
    fun setUp() {
        // Set main dispatcher to a TestDispatcher for coroutines
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        // Create a mock FoodRepository
        repository = mockk()
        // Initialize the ViewModel with the mock repository
        viewModel = LoginVM(repository)
        clearMocks(repository)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to avoid affecting other tests
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun whenValidCredentialsProvidedThenLoginStatusTrueAndNoError() = runTest(UnconfinedTestDispatcher()) {
        val username = "testuser"
        val password = "correctpass"
        val fakeResponse = LoginResponse(message = "login successful", user_id = 100)

        coEvery { repository.login(username, password) } returns fakeResponse

        viewModel.validUserOrNot(username, password)
        delay(100)
        val status = viewModel.loginStatus.getOrAwaitValue()
        val loading = viewModel.isLoading.getOrAwaitValue()
        val error = viewModel.errorMessage.value

        assertEquals(true, status)
        assertNull("Error message should be null on success", error)
        assertEquals(false, loading)

        coVerify(exactly = 1) { repository.login(username, password) }
    }


    @Test
    fun whenInvalidCredentialsProvidedThenErrorMessageSetAndLoginStatusUnchanged() = runTest {
        // **Given** a set of invalid user credentials and the repository returns null (login failed)
        val username = "testuser"
        val password = "wrongpass"
        coEvery { repository.login(username, password) } returns null

        // **When** validUserOrNot is called with invalid credentials
        viewModel.validUserOrNot(username, password)
        val error = viewModel.errorMessage.getOrAwaitValue()     // should be updated to "invalid credentials"
        val loading = viewModel.isLoading.getOrAwaitValue()      // should become false after operation
        val status = viewModel.loginStatus.value                // loginStatus is not set to true (remains null)

        // **Then** errorMessage is "invalid credentials", loginStatus remains null (no success), and isLoading is false
        assertEquals("invalid credentials", error)
        assertNull("Login status should remain null on failure", status)
        assertEquals(false, loading)
        coVerify(exactly = 1) { repository.login(username, password) }
    }

    @Test
    fun whenRepositoryThrowsExceptionThenErrorMessageUpdatedAndLoadingStopped() = runTest {
        // **Given** valid credentials but repository.login throws an exception (e.g., network error)
        val username = "testuser"
        val password = "anyPassword"
        val exception = RuntimeException("Network error")
        coEvery { repository.login(username, password) } throws exception

        // **When** validUserOrNot is called and repository throws
        viewModel.validUserOrNot(username, password)
        val error = viewModel.errorMessage.getOrAwaitValue()    // should be set to the exception message
        val loading = viewModel.isLoading.getOrAwaitValue()     // should become false after operation
        val status = viewModel.loginStatus.value               // loginStatus remains null (no success)

        // **Then** errorMessage contains the exception message, loginStatus is still null, and isLoading is false
        assertTrue("Error message should contain 'Network error'", error?.contains("Network error") ?: false)
        assertNull("Login status should remain null on exception", status)
        assertEquals(false, loading)
        coVerify(exactly = 1) { repository.login(username, password) }
    }
}


