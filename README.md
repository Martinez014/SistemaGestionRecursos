# 📚 Sistema de Gestión de Recursos Educativos

Aplicación Android desarrollada en Kotlin que permite la gestión de recursos educativos con roles de **docente** y **estudiante**, utilizando una API en MockAPI.

---

## 🚀 Funcionalidades principales

### 👨‍🏫 Docente
- Crear recursos educativos
- Eliminar recursos con confirmación de seguridad
- Visualizar lista de recursos
- Ocultar funciones no permitidas según el rol

### 🎓 Estudiante
- Visualizar recursos disponibles
- Buscar recursos por ID, título o tipo
- Filtrar y ordenar recursos
- Agregar recursos a favoritos
- Ver lista de recursos favoritos
- Calificar recursos (sistema visual de rating 1–10)

---

## 🔐 Sistema de autenticación
- Login de usuarios conectados a MockAPI
- Roles diferenciados:
  - Estudiante
  - Docente
- Redirección según rol al iniciar sesión

---

## ⭐ Sistema de favoritos
- Cada usuario puede agregar recursos a favoritos
- Persistencia mediante API (MockAPI)
- Vista separada de favoritos

---

## ⭐ Sistema de calificación (visual)
- Los estudiantes pueden calificar recursos (1 a 10)
- Se calcula promedio de calificaciones por recurso
- Se muestra rating actualizado en la interfaz

---

## 🔎 Búsqueda y filtros
- Búsqueda por:
  - ID
  - Título
  - Tipo de recurso
- Ordenamiento por:
  - Título
  - ID

---

## 🛠️ Tecnologías utilizadas
- Kotlin
- Android Studio
- RecyclerView
- Retrofit
- Coroutines
- MockAPI
- SharedPreferences

---

## 📡 API utilizada
Se utiliza MockAPI para simular backend:
- Usuarios
- Recursos

---

## 📱 Requisitos del sistema
- Android 7.0 o superior
- Conexión a internet

---

## 📽️ Link del video
- https://drive.google.com/file/d/1_lfg69_4tIMmzJrkLVxT3d6qvFirWj1E/view?usp=sharing

---

## 👨‍💻 Autor
Edmilson Alejandro Martinez Reynosa - MR222768
