package com.example.calculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import com.google.firebase.messaging.FirebaseMessaging
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener{ task ->
            if (!task.isSuccessful){
                return@addOnCompleteListener
            }
            val token = task.result
            Log.e("TEST", "TOKEN = $token")
        }
        setContent {
            CalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Calculator()
                }
            }
        }
    }
}

@Composable
fun Calculator() {

    var currentExpression by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }
    var invalidExpressionMessage by rememberSaveable { mutableStateOf(false) }
    var isNumberPositive by rememberSaveable { mutableStateOf(true) }


    val onButtonClick: (PadButton) -> Unit = { button ->
        run {
            when (button) {
                PadButton.AC -> {
                    currentExpression = ""
                    result = ""
                    invalidExpressionMessage = false
                }
            PadButton.PlusMinus -> {
                if (currentExpression.isNotEmpty() && !currentExpression.startsWith("-")) {
                    currentExpression = "-$currentExpression"
                } else if (currentExpression.startsWith("-")) {
                    currentExpression = currentExpression.substring(1)
                }
                isNumberPositive = !isNumberPositive
            }
            PadButton.Equals ->
                calculateResult(
                    currentExpression,
                    { res -> result = res },
                    { invalidExpressionMessage = true }
                )
                else -> {
                    currentExpression += button.expressionValue
                    invalidExpressionMessage = false
                }
            }
        }

    }

    Column(modifier = Modifier.fillMaxHeight()) {
        Box(modifier = Modifier.weight(0.33f)) {
            InputUI(currentExpression, result)
        }
        Box(modifier = Modifier.weight(0.66f)) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val rowModifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)

                ButtonRow(
                    rowModifier,
                    listOf(PadButton.Plus, PadButton.Minus, PadButton.Multiply, PadButton.Divide),
                    onButtonClick
                )

                ButtonRow(
                    rowModifier,
                    listOf(PadButton.Seven, PadButton.Eight, PadButton.Nine, PadButton.Degree),
                    onButtonClick,
                )

                ButtonRow(
                    rowModifier,
                    listOf(PadButton.Four, PadButton.Five, PadButton.Six, PadButton.PlusMinus),
                    onButtonClick,
                )

                ButtonRow(
                    rowModifier,
                    listOf(PadButton.One, PadButton.Two, PadButton.Three, PadButton.AC),
                    onButtonClick,
                )

                Row(
                    modifier = rowModifier,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(0.25f)) {
                        PadButtonUI(
                            PadButton.Zero,
                            onButtonClick,
                        )
                    }

                    Column(modifier = Modifier.weight(0.25f)) {
                        PadButtonUI(
                            PadButton.Decimal,
                            onButtonClick,
                        )
                    }

                    Column(modifier = Modifier.weight(0.5f)) {
                        PadButtonUI(
                            PadButton.Equals,
                            onButtonClick,
                        )
                    }
                }
            }
        }
    }
    if (invalidExpressionMessage) {
        result = stringResource(R.string.invalid)
    }
}

@Composable
private fun InputUI(
    currentExpression: String = "",
    result: String = "",
) {
    val fontSize = when {
        currentExpression.length > 30 -> 14.sp
        currentExpression.length > 25 -> 20.sp
        currentExpression.length > 20 -> 24.sp
        currentExpression.length > 15 -> 28.sp
        else -> 32.sp
    }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = currentExpression,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            fontSize = fontSize
        )
        Text(
            text = result,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displayLarge,
            fontSize = 48.sp,
        )
    }
}

enum class PadButton(
    val buttonText: String,
    val expressionValue: String = buttonText,
) {
    AC("AC"),
    PlusMinus("+/-"),
    Degree("xʸ", "^"),
    Divide("÷", "/"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Multiply("x", "*"),
    Four("4"),
    Five("5"),
    Six("6"),
    Minus("-"),
    One("1"),
    Two("2"),
    Three("3"),
    Plus("+"),
    Zero("0"),
    Decimal(",", "."),
    Equals("=")
}


@Composable
private fun ButtonRow(
    modifier: Modifier = Modifier,
    rowElements: List<PadButton>,
    onClickButton: (PadButton) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (rowElement in rowElements) {
            Column(modifier = Modifier.weight(0.25f)) {
                PadButtonUI(
                    rowElement,
                    onClickButton,
                )
            }
        }
    }
}

@Composable
fun PadButtonUI(
    button: PadButton = PadButton.AC,
    onClickButton: (PadButton) -> Unit,
) {
    val backgroundColor = when (button) {
        PadButton.AC, PadButton.PlusMinus, PadButton.Degree, PadButton.Equals -> MaterialTheme.colorScheme.primary
        PadButton.Divide, PadButton.Multiply, PadButton.Minus, PadButton.Plus -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.surface
    }

    val colorText = when (button) {
        PadButton.AC, PadButton.PlusMinus, PadButton.Degree, PadButton.Equals -> MaterialTheme.colorScheme.onPrimary
        PadButton.Divide, PadButton.Multiply, PadButton.Minus, PadButton.Plus -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .shadow(4.dp)
            .background(backgroundColor)
            .fillMaxSize()
            .clickable(onClick = { onClickButton(button) }),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = button.buttonText,
            color = colorText,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

fun calculateResult(expression: String, onResult: (String) -> Unit, onError: () -> Unit) {
    try {
        val result = ExpressionBuilder(expression).build().evaluate()
        onResult(result.toString())
    } catch (e: Exception) {
        onError()
//        throw RuntimeException("Test Crash")
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        Calculator()
    }
}