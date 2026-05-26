package com.example.appcursos.Proyecto.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appcursos.R
import com.example.appcursos.ui.theme.AppCursosTheme
import kotlinx.coroutines.delay

@Composable
fun PantallaInicio(navController: NavHostController) {
    var clickBloqueado by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // --- SECCIÓN LOGO ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo VisionEducation",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(25.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "VisionEducation",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // --- SECCIÓN ACCIONES ---
            Column(modifier = Modifier.fillMaxWidth()) {

                // Botón Iniciar Sesión (Con navegación limpia)
                Button(
                    onClick = {
                        if (!clickBloqueado) {
                            clickBloqueado = true
                            // Mejoramos la navegación: eliminamos la pantalla de inicio del historial
                            // para que al hacer atrás en el login, no regrese infinitamente.
                            navController.navigate("login") {
                                popUpTo("inicio") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Registro
                OutlinedButton(
                    onClick = {
                        if (!clickBloqueado) {
                            clickBloqueado = true
                            navController.navigate("registro")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("Crear Perfil Nuevo", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    LaunchedEffect(clickBloqueado) {
        if (clickBloqueado) {
            delay(1000)
            clickBloqueado = false
        }
    }
}