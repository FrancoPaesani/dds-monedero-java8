## Monedero

### Solución - Listado de Code Smells:

**<ins>1. Duplicated Code: [Cuenta. Línea 28 y 40]:</ins>** La validación de si el parámetro es negativo se puede extraer a un metodo -> validarMontoPositivo() y usarlo en el método poner(x) y sacar(x).

**<ins>2. Duplicación de Código/ duplicación de objetos: [Cuenta. Línea 36 y 52: Método poner(),sacar()]:</ins>**
Por cada new Movimiento(...).agregateA(...) estoy creando dos objetos Movimiento con la misma info, por un tema de mala ubicación de métodos (ver code smell N° 7).

**<ins>3. Long Method: [Cuenta. Línea 39: Método sacar()]:</ins>**

En el método sacar() ( vale también para el método poner() ) se están realizando demasiadas validaciones, las cuales pueden variar. Como por ejemplo, puede variar que la cantidad de depósitos por día sea de 3; o que el límite de extracción sea de 1000.
Sería mejor parametrizar estos valores y extraerlos a otro métodos o a otras clases las cuales realicen las validaciones.

**<ins>4. Feature Envy: [Cuenta. Línea 60. Método getMontoExtraidoA(fecha)]:</ins>**

En este método estamos realizando muchos mensajes sobre un Movimiento mientras la interfaz nos provee 2 métodos para realizar lo mismo con mayor encapsulamiento "fueExtraido(fecha)".

**<ins>5. Duplicated Code: [Movimiento. Línea 38. Método isDeposito()]:</ins>**

No hace falta tener los dos métodos isDepósito() y isExtracción(). Se puede hacer un return !isDeposito() (igualmente no es demasiada repetición, puede ayudar tener los 2 para más declaratividad).

**<ins>6. Misplaced Method: [Movimiento. Línea 46: Método agregateA(Cuenta)]:</ins>**

Un Movimiento no tiene porque saber agregarse a una cuenta en particular. Es la cuenta quien tiene que agregar un Movimiento determinado basado en la fecha, monto y tipo de operación.

**<ins>7. Feature Envy/Misplaced Method - Primitive Obsession (TipoOperación): [Movimiento. Línea 51: Método calcularValor(Cuenta) / Atributo esDeposito]:</ins>**

Es la propia cuenta quien debería realizar el set saldo actual a partir de un nuevo movimiento. El movimiento en base a un monto podría devolver el importe luego de realizar este movimiento, pero la asignación del saldo actual a la cuenta la debería hacer la Cuenta.
Se podría modelar una interfaz tipoOperación y delegar el cálculo del nuevo saldo a las clases que la implementen.


### Contexto

Este repositorio contiene el código de un _monedero virtual_, al que podemos agregarle y quitarle dinero, a través 
de los métodos `Monedero.sacar` y `Monedero.poner`, respectivamente. 
Pero hay algunos problemas: por un lado el código no está muy bien testeado, y por el otro, hay numeros _code smells_. 

### Consigna

Tenés seis tareas: 

 1. :fork_and_knife: Hacé un _fork_ de este repositorio (presionando desde Github el botón Fork)
 2. :arrow_down: Descargalo y construí el proyecto, utilizando `maven`
 2. :nose: Identificá y anotá todos los _code smells_ que encuentres 
 3. :test_tube: Agregá los tests faltantes y mejorá los existentes. 
     * :eyes: Ojo: ¡un test sin ningún tipo de aserción está incompleto!
 4. :rescue_worker_helmet: Corregí smells, de a un commit por vez. 
 5. :arrow_up: Subí todos los cambios a tu _fork_

### Tecnologías usadas

* Java 8.
* JUnit 5. :warning: La versión 5 de JUnit es la más nueva del framework y presenta algunas diferencias respecto a la versión "clásica" (JUnit 4). Para mayores detalles, ver:
    *  [Apunte de herramientas](https://docs.google.com/document/d/1VYBey56M0UU6C0689hAClAvF9ILE6E7nKIuOqrRJnWQ/edit#heading=h.dnwhvummp994)
    *  [Entrada de Blog (en inglés)](https://www.baeldung.com/junit-5-migration)
    *  [Entrada de Blog (en español)](https://www.paradigmadigital.com/dev/nos-espera-junit-5/)
* Maven 3.3 o superior
 

  


