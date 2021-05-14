package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuentaJorge;
  private Cuenta cuentaManuel;
  private Cuenta cuentaJosefa;
  private Cuenta cuentaDaniel;

  @BeforeEach
  void init() {
    cuentaJorge = new Cuenta(new BigDecimal(1000), new BigDecimal(500),3);
    cuentaManuel = new Cuenta(new BigDecimal(1000), new BigDecimal(2000),2);
    cuentaJosefa = new Cuenta(new BigDecimal(2000),new BigDecimal(1000),1);
    cuentaDaniel = new Cuenta(new BigDecimal(5000), new BigDecimal(2500),2);
  }

  @Test
  void unDepositode100EnCuentaQueTiene1000Suma1100() {
    assertTrue(Deposito.getINSTANCE().calcularNuevoSaldo(new BigDecimal(1000), new BigDecimal(100))
            .compareTo(new BigDecimal(1100)) == 0);
  }

  @Test
  void unaExtraccionDe100EnCuentaQueTiene1000Es900() {
    assertTrue(Extraccion.getINSTANCE().calcularNuevoSaldo(new BigDecimal(1000), new BigDecimal(100))
            .compareTo(new BigDecimal(900)) == 0);
  }

  @Test
  void DepositoDe500EnCuentaDe2000Tiene2500() {
    cuentaJosefa.poner(new BigDecimal(500));
    assertTrue(cuentaJosefa.getSaldo().compareTo(new BigDecimal(2500)) == 0);
  }

  @Test
  void MasDetresDepositosEnCuentaCon3DepositosMaximosNoSePuede() {
    cuentaJorge.poner(new BigDecimal(100));
    cuentaJorge.poner(new BigDecimal(200));
    cuentaJorge.poner(new BigDecimal(200));
    assertThrows(MaximaCantidadDepositosException.class,() -> cuentaJorge.poner(new BigDecimal(100)));
  }

  @Test
  void PuedoTresDepositosEnCuentaCon3DepositosMaximos() {
    cuentaJorge.poner(new BigDecimal(100));
    cuentaJorge.poner(new BigDecimal(200));
    assertEquals(cuentaJorge.getMovimientos().size(),2);
  }

  @Test
  void DosExtraccionesDe500Y1DepositoDe1000CuentaTiene3Operaciones() {
    cuentaJosefa.sacar(new BigDecimal(500));
    cuentaJosefa.sacar(new BigDecimal(500));
    cuentaJosefa.poner(new BigDecimal(1000));
    assertEquals(cuentaJosefa.getMovimientos().size(),3);
  }

  @Test
  void NingunaCuentaPuedeDepositarMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuentaManuel.poner(new BigDecimal(-1)));
  }

  @Test
  public void NingunaCuentaPuedeExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuentaDaniel.sacar(new BigDecimal(-500)));
  }

  @Test
  void CuentaQueTieneMonto1000NoSePuedeExtraerMasDe1000() {
    System.out.println(cuentaManuel.getMontoExtraidoA(LocalDate.now()));
    assertThrows(SaldoMenorException.class, () -> cuentaManuel.sacar(new BigDecimal(1100)));
  }

  @Test
  void ExtraccionDiariaNoPuedo5000EnCuentaConLimiteExtraccion2500() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> cuentaDaniel.sacar(new BigDecimal(5000)));
  }
}