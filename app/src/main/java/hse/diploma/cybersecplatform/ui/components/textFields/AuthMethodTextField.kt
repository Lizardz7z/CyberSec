package hse.diploma.cybersecplatform.ui.components.textFields

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hse.diploma.cybersecplatform.R
import hse.diploma.cybersecplatform.ui.theme.CyberSecPlatformTheme
import hse.diploma.cybersecplatform.ui.theme.Montserrat
import hse.diploma.cybersecplatform.ui.theme.Typography
import hse.diploma.cybersecplatform.utils.AuthMethodType
import hse.diploma.cybersecplatform.utils.MaskVisualTransformation
import hse.diploma.cybersecplatform.utils.isLoginValidAndAuthMethodType

@Composable
fun AuthMethodTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    val label = stringResource(R.string.auth_label_phone_number_email)

    val (isAuthMethodTypeValid, authMethodType) = isLoginValidAndAuthMethodType(value.text)

    Column(modifier = modifier.padding(8.dp)) {
        if (!isAuthMethodTypeValid) {
            Text(
                text = "${stringResource(R.string.auth_label_error)} ${label.lowercase()}",
                color = Color.Red,
                style = Typography.labelSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        TextField(
            value = value,
            textStyle = Typography.labelLarge,
            onValueChange = { newValue ->
                val digits = newValue.text.filter { it.isDigit() }
                if (digits.length <= 10) {
                    val formatted = "+7 $digits"
                    onValueChange(TextFieldValue(formatted, newValue.selection))
                }
            },
            label = {
                Text(
                    text = label,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = if (value.text.isEmpty()) 16.sp else 10.sp,
                    color = Color.Black
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            visualTransformation = if (authMethodType == AuthMethodType.PHONE) {
                MaskVisualTransformation("+7 (###)-###-##-##")
            } else {
                VisualTransformation.None
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (!isAuthMethodTypeValid) Color.Red else Color.Black,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_large))
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthMethodTextFieldPreview() {
    CyberSecPlatformTheme {
        AuthMethodTextField(
            value = TextFieldValue("9300315295"),
            onValueChange = {},
        )
    }
}
