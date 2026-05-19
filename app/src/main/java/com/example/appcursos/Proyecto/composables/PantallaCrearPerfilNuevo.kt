package com.example.appcursos.Proyecto.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appcursos.ui.theme.AppCursosTheme

@Composable
fun PantallaCrearPerfilNuevo(navController: NavHostController) {
    var usuario by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    val opcionesRoles = listOf("alumno", "profesor")
    val (rolSeleccionado, onRolSelected) = remember { mutableStateOf(opcionesRoles[0]) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Text(text = "Crea tu Perfil", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                Text(text = "Información oficial para VisionEducation", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Row(Modifier.selectableGroup().fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    opcionesRoles.forEach { texto ->
                        Row(
                            Modifier.selectable(selected = (texto == rolSeleccionado), onClick = { onRolSelected(texto) }, role = Role.RadioButton).padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (texto == rolSeleccionado), onClick = null)
                            Text(text = texto.replaceFirstChar { it.uppercase() }, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Nombre de Usuario") },
                    leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
            }
            item {
                OutlinedTextField(value = apellidoPaterno, onValueChange = { apellidoPaterno = it }, label = { Text("Apellido Paterno") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
            }
            item {
                OutlinedTextField(value = apellidoMaterno, onValueChange = { apellidoMaterno = it }, label = { Text("Apellido Materno") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
            }
            item {
                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = { fechaNacimiento = it },
                    label = { Text("Fecha Nacimiento (YYYY-MM-DD)") },
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                // --- CAMBIO SOLICITADO AQUÍ ---
                Button(
                    onClick = { navController.navigate("dashboard") },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    val textoMostrar = if (rolSeleccionado == "profesor") "Maestro" else "Alumno"
                    Text("Registrar $textoMostrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            item {
                TextButton(onClick = { navController.navigate("login") }) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegistro() {
    AppCursosTheme {
        PantallaCrearPerfilNuevo(rememberNavController())
    }
}