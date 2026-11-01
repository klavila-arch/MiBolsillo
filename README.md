# MiBolsillo

**Aplicación móvil para la administración de finanzas personales**

Proyecto académico desarrollado por **Karen Lizbet Ávila Camacho** para la asignatura **Desarrollo de aplicaciones móviles I**, de la Escuela Bancaria y Comercial (EBC).

## Descripción

MiBolsillo es una aplicación móvil para Android que permite registrar, consultar y administrar ingresos y gastos personales de manera sencilla.

Su objetivo es facilitar el seguimiento de las finanzas personales mediante el cálculo automático del saldo disponible y la organización de movimientos financieros por periodos.

## Funcionalidades

- Registro de ingresos y gastos.
- Clasificación de movimientos por categoría.
- Incorporación de descripciones y fechas.
- Edición y eliminación de movimientos.
- Confirmación antes de eliminar registros.
- Cálculo automático de ingresos, gastos y saldo disponible.
- Almacenamiento local de la información.
- Consulta del historial de movimientos.
- Filtrado de movimientos por mes y año.
- Opción «Mostrar todos» para consultar el historial completo.
- Validación de importes financieros positivos y finitos.

## Tecnologías utilizadas

- **Lenguaje:** Kotlin.
- **Interfaz de usuario:** Jetpack Compose.
- **Entorno de desarrollo:** Android Studio.
- **Sistema operativo:** Android.
- **Herramienta de compilación:** Gradle.
- **Pruebas unitarias:** JUnit.
- **Control de versiones:** Git y GitHub.

## Arquitectura y organización

El proyecto separa las responsabilidades de presentación, gestión de estado y cálculos financieros.

El archivo `InicioViewModel.kt` gestiona el estado de la pantalla principal, mientras que `CalculosFinancieros.kt` contiene las funciones utilizadas para calcular el resumen financiero y validar importes.

Las pruebas automatizadas de estas funciones se encuentran en `CalculosFinancierosTest.kt`.

## Pruebas realizadas

Se documentaron diez escenarios de pruebas funcionales manuales, relacionados con el registro, edición, eliminación, persistencia de información, actualización de saldos y filtrado de movimientos.

Adicionalmente, se ejecutaron doce pruebas unitarias automatizadas mediante JUnit y Gradle, de las cuales once corresponden a `CalculosFinancierosTest` y una a `ExampleUnitTest`.

**Resultados de evaluación:**

| Tipo de prueba | Ejecutadas | Aprobadas | Fallidas |
|---|---:|---:|---:|
| Funcionales manuales | 10 | 10 | 0 |
| Unitarias automatizadas | 12 | 12 | 0 |
| **Total** | **22** | **22** | **0** |

Las pruebas automatizadas finalizaron con una tasa de éxito del 100 %.

Para ejecutar las pruebas unitarias desde la raíz del proyecto en Windows:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

El informe de resultados se genera en:

`app/build/reports/tests/testDebugUnitTest/index.html`

## Generación del APK

La aplicación cuenta con una compilación de depuración generada correctamente desde Android Studio.

Para generar el APK:

1. Abrir el proyecto en Android Studio.
2. Seleccionar **Build → Generate App Bundles or APKs → Generate APKs**.
3. Esperar a que finalice la compilación.

El archivo generado se encuentra en:

`app/build/outputs/apk/debug/app-debug.apk`

El APK de depuración fue instalado y abierto satisfactoriamente en un emulador Medium Phone con Android API 30.

**Nota:** El APK de depuración está destinado a pruebas y no constituye una versión de producción firmada para su publicación en Google Play.

## Repositorio

GitHub: https://github.com/klavila-arch/MiBolsillo

Rama de desarrollo: `mejoras-mibolsillo`

## Estado del proyecto

La aplicación cuenta con las funcionalidades principales implementadas, pruebas funcionales y unitarias satisfactorias y un APK de depuración generado y probado.

Como mejoras futuras se consideran las pruebas en dispositivos físicos, la evaluación de compatibilidad con otras versiones de Android y la incorporación de pruebas automatizadas de interfaz e integración.

## Autoría

**Karen Lizbet Ávila Camacho**

Escuela Bancaria y Comercial (EBC)

Licenciatura Ejecutiva en Programación y Transformación Digital

**Última actualización:** 1 de noviembre de 2026.