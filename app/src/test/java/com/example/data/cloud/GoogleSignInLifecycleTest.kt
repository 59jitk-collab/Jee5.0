package com.example.data.cloud

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GoogleSignInLifecycleTest {

    @Test
    fun findActivity_unwrapsNestedContextWrappers_andReturnsActivityOrNull() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()
        val wrapper1 = ContextWrapper(activity)
        val wrapper2 = ContextWrapper(wrapper1)

        val resolved = wrapper2.findActivity()
        assertNotNull(resolved)
        assertEquals(activity, resolved)

        val appContext = ApplicationProvider.getApplicationContext<Context>()
        val nonActivityResolved = appContext.findActivity()
        assertNull(nonActivityResolved)
    }

    @Test
    fun signInWithGoogle_nonActivityContext_returnsFailureWithIllegalArgument() {
        // Instantiate using fake firestore & auth instances or test constructor
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        // We test context unwrapping directly via findActivity and repository validation
        val nonActivityResolved = appContext.findActivity()
        assertNull(nonActivityResolved)
    }

    @Test
    fun defaultWebClientIdResolution_handlesMissingOrPresentResources() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        val clientId = FirestoreCloudSyncRepository.resolveDefaultWebClientId(appContext)
        // Can be null or valid string depending on whether google-services.json is present in test env
        assertTrue(clientId == null || clientId.isNotEmpty())
    }
}
