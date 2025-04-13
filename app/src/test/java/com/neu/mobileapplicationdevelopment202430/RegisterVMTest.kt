package com.neu.mobileapplicationdevelopment202430

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.SignupResponse
import com.neu.mobileapplicationdevelopment202430.viewmodel.RegisterVM
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FoodRepository
    private lateinit var viewModel: RegisterVM

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = mockk(relaxed = true)
        viewModel = RegisterVM(repository)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun validSignUpOrNot_successfulSignUp_setsSignupStatusTrue() = runTest {
        val username = "newuser"
        val password = "newpassword"
        val response = SignupResponse("sign-up successful", user_id = 123)

        coEvery { repository.signup(username, password) } returns response

        viewModel.validSignUpOrNot(username, password)
        advanceUntilIdle()

        val result = viewModel.signupStatus.getOrAwaitValue()
        assertTrue(result == true)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun validSignUpOrNot_userAlreadyExists_setsErrorMessage() = runTest {
        val username = "existinguser"
        val password = "password"
        val response = SignupResponse("user already exists", user_id = null)

        coEvery { repository.signup(username, password) } returns response

        viewModel.validSignUpOrNot(username, password)
        advanceUntilIdle()

        val error = viewModel.errorMessage.getOrAwaitValue()
        assertEquals("user already exists", error)
        assertNull(viewModel.signupStatus.value)
    }

    @Test
    fun validSignUpOrNot_exceptionThrown_setsErrorMessage() = runTest {
        val username = "failuser"
        val password = "failpass"

        coEvery { repository.signup(username, password) } throws RuntimeException("Network error")

        viewModel.validSignUpOrNot(username, password)
        advanceUntilIdle()

        val error = viewModel.errorMessage.getOrAwaitValue()
        assertTrue(error!!.contains("Network error"))
        assertNull(viewModel.signupStatus.value)
    }

    @Test
    fun resetRegisterStatus_setsSignupStatusToNull() {
        viewModel.resetRegisterStatus()
        assertNull(viewModel.signupStatus.value)
    }

    @Test
    fun signupStatus_isNullInitially() {
        // Assert that signupStatus is null when view model is initialized
        assertNull(viewModel.signupStatus.value)
    }

    @Test
    fun signupStatus_isNullAfterReset() {
        // Reset the signup status
        viewModel.resetRegisterStatus()

        // Assert that signupStatus is reset to null
        assertNull(viewModel.signupStatus.value)
    }

    @Test
    fun errorMessage_isSet_whenUserAlreadyExists() = runTest {
        val username = "existinguser"
        val password = "password"
        val response = SignupResponse("user already exists", user_id = null)

        coEvery { repository.signup(username, password) } returns response

        viewModel.validSignUpOrNot(username, password)
        advanceUntilIdle()

        // Assert that the error message is set when user already exists
        val errorMessage = viewModel.errorMessage.getOrAwaitValue()
        assertEquals("user already exists", errorMessage)
    }





}
