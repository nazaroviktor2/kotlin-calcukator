package com.example.calculator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calculator.ui.theme.CalculatorTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testInitialUI() {
        composeTestRule.setContent {
            CalculatorTheme {
                Calculator()
            }
        }

        composeTestRule.onNodeWithText("0").assertExists()
        composeTestRule.onNodeWithText("AC").assertExists()
        composeTestRule.onNodeWithText("+/-").assertExists()
        composeTestRule.onNodeWithText("=").assertExists()
    }

    @Test
    fun testAddition() {
        composeTestRule.setContent {
            CalculatorTheme {
                Calculator()
            }
        }

        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("=").performClick()
        composeTestRule.onNodeWithText("2").assertExists()
    }

    @Test
    fun testSubtraction() {
        composeTestRule.setContent {
            CalculatorTheme {
                Calculator()
            }
        }

        composeTestRule.onNodeWithText("5").performClick()
        composeTestRule.onNodeWithText("-").performClick()
        composeTestRule.onNodeWithText("3").performClick()
        composeTestRule.onNodeWithText("=").performClick()
        composeTestRule.onNodeWithText("2").assertExists()
    }

    @Test
    fun testMultiplication() {
        composeTestRule.setContent {
            CalculatorTheme {
                Calculator()
            }
        }

        composeTestRule.onNodeWithText("2").performClick()
        composeTestRule.onNodeWithText("x").performClick()
        composeTestRule.onNodeWithText("3").performClick()
        composeTestRule.onNodeWithText("=").performClick()
        composeTestRule.onNodeWithText("6").assertExists()
    }

    @Test
    fun testDivision() {
        composeTestRule.setContent {
            CalculatorTheme {
                Calculator()
            }
        }

        composeTestRule.onNodeWithText("8").performClick()
        composeTestRule.onNodeWithText("÷").performClick()
        composeTestRule.onNodeWithText("2").performClick()
        composeTestRule.onNodeWithText("=").performClick()
        composeTestRule.onNodeWithText("4").assertExists()
    }

    @Test
    fun testInvalidExpression() {
        composeTestRule.setContent {
            CalculatorTheme {
                Calculator()
            }
        }

        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("/").performClick()
        composeTestRule.onNodeWithText("0").performClick()
        composeTestRule.onNodeWithText("=").performClick()
        composeTestRule.onNodeWithText("Invalid expression").assertExists()
    }
}
