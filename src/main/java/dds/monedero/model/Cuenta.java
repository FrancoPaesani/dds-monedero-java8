package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() { saldo = 0; }

  public Cuenta(double montoInicial) { saldo = montoInicial; }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    validarMontoNoNegativo(cuanto);
    validarCantidadDepositosDiarios(3);

    this.agregarMovimiento(LocalDate.now(), cuanto, true);
    this.saldo = saldo + cuanto;
  }

  public void sacar(double cuanto) {
    validarMontoNoNegativo(cuanto);
    validarSaldoSuficiente(getSaldo(), cuanto);
    validarLimiteDiario(getMontoExtraidoA(LocalDate.now()), cuanto, 1000);

    this.agregarMovimiento(LocalDate.now(), cuanto, false);
    this.saldo = saldo - cuanto;
  }
  private void validarSaldoSuficiente(double saldo, double monto) {
    if (saldo - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }
  private void validarLimiteDiario(double montoExtraidoHoy, double montoExtraccion, double limite) {
    if (montoExtraccion > limite - montoExtraidoHoy) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + limite
              + " diarios, extracción restante en el día de hoy: " + (limite - montoExtraidoHoy));
    }
  }
  private void validarMontoNoNegativo(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  private void validarCantidadDepositosDiarios(Integer cantDepositosMaximos) {
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= cantDepositosMaximos) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + cantDepositosMaximos + " depositos diarios");
    }
  }
  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
