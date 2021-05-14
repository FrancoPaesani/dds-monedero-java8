package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cuenta {

  private double saldo = 0;
  private double limiteDiarioExtraccion;
  private int cantDepositosMaximos;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta(Double montoInicial, double limiteDiarioExtraccion, int cantDepositosMaximos) {
    if(montoInicial != null) {
      this.saldo = (double) montoInicial;
    }
    this.limiteDiarioExtraccion = Objects.requireNonNull(limiteDiarioExtraccion,"Se  requiere un limite diario de extraccion");
    this.cantDepositosMaximos = Objects.requireNonNull(cantDepositosMaximos,"Se  requiere un limite diario de depositos.");
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double monto) {
    validarMontoNoNegativo(monto);
    validarCantidadDepositosDiarios();

    Movimiento movimientoActual = new Movimiento(LocalDate.now(), monto, Deposito.getINSTANCE());
    this.agregarMovimiento(movimientoActual);
    this.saldo = movimientoActual.calcularValor(saldo, monto);
  }

  public void sacar(double monto) {
    validarMontoNoNegativo(monto);
    validarLimiteDiario(getMontoExtraidoA(LocalDate.now()), monto);
    validarSaldoSuficiente(getSaldo(), monto);

    Movimiento movimientoActual = new Movimiento(LocalDate.now(), monto, Extraccion.getINSTANCE());
    this.agregarMovimiento(movimientoActual);
    this.saldo = movimientoActual.calcularValor(saldo, monto);
  }
  private void validarSaldoSuficiente(double saldo, double monto) {
    if (saldo - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }
  private void validarLimiteDiario(double montoExtraidoHoy, double montoExtraccion) {
    if (montoExtraccion > limiteDiarioExtraccion - montoExtraidoHoy) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + limiteDiarioExtraccion
              + " diarios, extracción restante en el día de hoy: " + (limiteDiarioExtraccion - montoExtraidoHoy));
    }
  }
  private void validarMontoNoNegativo(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  private void validarCantidadDepositosDiarios() {
    if (this.cantDepositosEnElDia(LocalDate.now()) >= cantDepositosMaximos) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + cantDepositosMaximos + " depositos diarios");
    }
  }
  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public int cantDepositosEnElDia(LocalDate fecha) {
    return getMovimientos().stream()
            .filter(movimiento -> movimiento.fueDepositado(fecha)).collect(Collectors.toList()).size();
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
