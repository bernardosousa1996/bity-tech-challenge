package com.example.bity.data.utils

import com.example.bity.utils.formatTimestamp
import com.example.bity.utils.weiToEth
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun `test weiToEth converts correctly`() {
        val weiAmount = "1000000000000000000"
        val expectedEthAmount = "1.00000"

        val result = weiToEth(weiAmount)

        assertEquals(expectedEthAmount, result)
    }

    @Test
    fun `test weiToEth with zero`() {
        val weiAmount = "0.00000"
        val expectedEthAmount = "0.00000"

        val result = weiToEth(weiAmount)

        assertEquals(expectedEthAmount, result)
    }

    @Test
    fun `test weiToEth with non-zero input`() {
        val weiAmount = "500000000000000000"
        val expectedEthAmount = "0.50000"

        val result = weiToEth(weiAmount)

        assertEquals(expectedEthAmount, result)
    }


    @Test
    fun `test formatTimestamp correctly formats date`() {
        val timestamp = "1628910000"  // UNIX Timestamp
        val expectedFormattedDate = "Aug-14-2021 03:00:00 AM" // Expected format

        val result = formatTimestamp(timestamp)

        assertEquals(expectedFormattedDate, result)
    }

    @Test
    fun `test formatTimestamp with invalid timestamp`() {
        val timestamp = "invalid"
        val expectedFormattedDate = "Invalid Date"

        val result = formatTimestamp(timestamp)

        assertEquals(expectedFormattedDate, result)
    }
}