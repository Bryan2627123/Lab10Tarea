package com.example.lab10_tarea.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lab10_tarea.data.AdaptadorApiService
import com.example.lab10_tarea.data.RegistroFuncionamiento
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScreenAdaptador(navController: NavHostController, servicio: AdaptadorApiService) {
    var listaRegistros: SnapshotStateList<RegistroFuncionamiento> = remember { mutableStateListOf() }
    var editarRegistro by remember { mutableStateOf<RegistroFuncionamiento?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Cargar registros desde el servicio
    LaunchedEffect(Unit) {
        val listado = servicio.obtenerRegistros() // Ajusta según el método real
        listado.forEach { listaRegistros.add(it) }
    }

    if (editarRegistro == null) {
        // Mostrar listado de registros
        LazyColumn {
            item {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ID",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(0.1f)
                    )
                    Text(
                        text = "Electrodoméstico",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(0.5f)
                    )
                    Text(
                        text = "Acción",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(0.4f)
                    )
                }
            }

            items(listaRegistros) { registro ->
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${registro.id}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(0.1f)
                    )
                    Text(
                        text = registro.electrodomestico,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(0.5f)
                    )
                    IconButton(
                        onClick = {
                            editarRegistro = registro
                            Log.e("Registro-Edit", "ID = ${registro.id}")
                        },
                        modifier = Modifier.weight(0.2f)
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Editar")
                    }
                    IconButton(
                        onClick = {
                            // Ejecutamos la eliminación dentro de una corutina
                            coroutineScope.launch {
                                servicio.eliminarRegistro(registro.id)
                                listaRegistros.remove(registro)
                                Log.e("Registro-Delete", "ID = ${registro.id}")
                            }
                        },
                        modifier = Modifier.weight(0.2f)
                    ) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    } else {
        // Mostrar pantalla de edición de registro
        ContenidoRegistroEditar(navController, servicio, editarRegistro!!)
    }
}

@Composable
fun ContenidoRegistroEditar(
    navController: NavHostController,
    servicio: AdaptadorApiService,
    registro: RegistroFuncionamiento
) {
    var id by remember { mutableStateOf(registro.id) }
    var electrodomestico by remember { mutableStateOf(registro.electrodomestico) }
    var temperatura by remember { mutableStateOf(registro.temperatura.toString()) }
    var fecha by remember { mutableStateOf(registro.fecha) }
    var desconectado by remember { mutableStateOf(registro.desconectado) }
    var grabar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = id.toString(),
            onValueChange = { },
            label = { Text("ID (solo lectura)") },
            readOnly = true,
            singleLine = true
        )
        TextField(
            value = electrodomestico,
            onValueChange = { electrodomestico = it },
            label = { Text("Electrodoméstico") },
            singleLine = true
        )
        TextField(
            value = temperatura,
            onValueChange = { temperatura = it },
            label = { Text("Temperatura") },
            singleLine = true
        )
        TextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha") },
            singleLine = true
        )

        Button(
            onClick = {
                grabar = true
            }
        ) {
            Text("Guardar", fontSize = 16.sp)
        }
    }

    if (grabar) {
        val nuevoRegistro = RegistroFuncionamiento(
            id = id,
            electrodomestico = electrodomestico,
            temperatura = temperatura.toFloat(),
            fecha = fecha,
            desconectado = desconectado
        )

        LaunchedEffect(Unit) {
            if (id == 0) {
                servicio.crearRegistro(nuevoRegistro)
            } else {
                servicio.actualizarRegistro(id, nuevoRegistro)
            }
        }
        grabar = false
        navController.navigate("registros")
    }
}
