package com.example.social_media_app.util.ui_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBox(inputText:String, label:String, onValueChange:(String)->Unit){
    OutlinedTextField(
        value = inputText,
        onValueChange = { onValueChange(it) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        placeholder = { Text(text = label) },
        singleLine = true,
        maxLines = 1,
//        leadingIcon = { Icon(imageVector = Icon!!, contentDescription = null, tint = Color.Blue) },
        modifier = Modifier
            .fillMaxWidth().padding( horizontal = 20.dp, vertical = 10.dp),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Blue,
            containerColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            focusedIndicatorColor = Color.Blue,

            ),
        shape = RoundedCornerShape(15.dp)
    )
}