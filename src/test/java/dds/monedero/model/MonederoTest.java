package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuentaJorge;
  private Cuenta cuentaManuel;
  private Cuenta cuentaJosefa;
  private Cuenta cuentaDaniel;

  @BeforeEach
  void init() {
    cuentaJorge = new Cuenta(new Double(1000), 500,3);
    cuentaManuel = new Cuenta(new Double(1000), 2000,2);
    cuentaJosefa = new Cuenta(new Double(2000),1000,1);
    cuentaDaniel = new Cuenta(new Double(5000), 2500,2);
  }

  @Test
  void unDepositode100EnCuentaQueTiene1000Suma1100() {
    assertEquals(Deposito.getINSTANCE().calcularNuevoSaldo(1000, 100), 1100);
  }

  @Test
  void unaExtraccionDe100EnCuentaQueTiene1000Es900() {
    assertEquals(Extraccion.getINSTANCE().calcularNuevoSaldo(1000, 100), 900);
  }

  @Test
  void DepositoDe500EnCuentaDe2000Tiene2500() {
    cuentaJosefa.poner(500);
    assertEquals(cuentaJosefa.getSaldo(),2500,0);
  }

  @Test
  void MasDetresDepositosEnCuentaCon3DepositosMaximosNoSePuede() {
    cuentaJorge.poner(100);
    cuentaJorge.poner(200);
    cuentaJorge.poner(200);
    assertThrows(MaximaCantidadDepositosException.class,() -> cuentaJorge.poner(100));
  }

  @Test
  void PuedoTresDepositosEnCuentaCon3DepositosMaximos() {
    cuentaJorge.poner(100);
    cuentaJorge.poner(200);
    assertEquals(cuentaJorge.getMovimientos().size(),2);
  }

  @Test
  void DosExtraccionesDe500Y1DepositoDe1000CuentaTiene3Operaciones() {
    cuentaJosefa.sacar(500);
    cuentaJosefa.sacar(500);
    cuentaJosefa.poner(1000);
    assertEquals(cuentaJosefa.getMovimientos().size(),3);
  }

  @Test
  void NingunaCuentaPuedeDepositarMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuentaManuel.poner(-1));
  }

  @Test
  public void NingunaCuentaPuedeExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuentaDaniel.sacar(-500));
  }

  @Test
  void CuentaQueTieneMonto1000NoSePuedeExtraerMasDe1000() {
    assertThrows(SaldoMenorException.class, () -> cuentaManuel.sacar(1100));
  }

  @Test
  void ExtraccionDiariaNoPuedo5000EnCuentaConLimiteExtraccion2500() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> cuentaDaniel.sacar(5000));
  }

}