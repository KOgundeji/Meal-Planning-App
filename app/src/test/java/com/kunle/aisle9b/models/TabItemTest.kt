package com.kunle.aisle9b.models

import androidx.compose.material3.Text
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TabItemTest {

    @Test
    fun createTabItemTest() {
        val tab = TabItem(title = "Settings") {
            Text(text = "Settings Tab")
        }

        assertThat(tab.title).isEqualTo("Settings")
    }
}