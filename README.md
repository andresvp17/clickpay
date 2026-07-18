# ClickPay

## ¿Qué Haremos?

Vamos a desarrollar un sistema de registros de nómina, donde nos centraremos en verificar que cada empleado dentro del sistema se le pague lo que corresponda.

Actuaremos cómo el empleador, el cual es responsable de sumar en forma precisa las horas que un empleado ha trabajado y cuánto debe ser su remuneración, teniendo en cuenta seguros, impuestos y más.

---

## ¿De qué será capaz el software?

Cómo tal el software llevará registros de los pagos hechos, pagos pendientes, y lo que cada empleado debe cobrar, siendo capaz el sistema de generar las facturas de cada pago del empleado.

En vista de no poder utilizar una base de datos real, considero que la mejor idea sería utilizar archivos CSV que actuarán cómo nuestra _"base de datos"_ la cual cargaremos una vez el programa inicie.

El software debería ser capaz de realizar tareas cómo un _CRUD_ (Create Read Update Delete), donde podrá crear usuarios nuevos, nuevos registros de pago y nuevas facturas, leerlas, actualizar datos de estas y borrarlas de ser necesarias por alguna razón.

---

## Estructura del Proyecto

Clickpay
│
├── entity/ # Core de Modelos (Employee, Payslip)
├── repository/ # Operaciones de escritura/lectura de los CSV (DAO Pattern)
├── service/ # Lógica de negocio y cálculos
└── ui/ # Interfaz de usuario

Para el proyecto me parece que sería bueno mantener una estructura simple para no complicarnos. Dicha estructura se desarrollaría de la siguiente forma:

- **Entity**: representa las clases principales del proyecto, las entidades que representan los elementos más básicos del proyecto cómo la clase Empleado, Payroll, Salario.
- **Repository**: acá se van a hacer las operaciones de los archivos cuando se modifiquen.
- **Service**: se hará la lógica de negocio, los cálculos en cada operación necesaria para poder calcular el sueldo correcto.
- **UI**: vivirá la creación del layout de la interfaz de usuario.

---

## Relaciones de Clase

![[class_relation.png]]
