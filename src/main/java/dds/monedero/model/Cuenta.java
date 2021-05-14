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
import java.math.BigDecimal;

public class Cuenta {

  private BigDecimal saldo = BigDecimal.ZERO;
  private BigDecimal limiteDiarioExtraccion;
  private int cantDepositosMaximos;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta(BigDecimal montoInicial, BigDecimal limiteDiarioExtraccion, int cantDepositosMaximos) {
    if(montoInicial != null) {
      this.saldo = montoInicial;
    }
    this.limiteDiarioExtraccion = Objects.requireNonNull(limiteDiarioExtraccion,"Se  requiere un limite diario de extraccion");
    this.cantDepositosMaximos = Objects.requireNonNull(cantDepositosMaximos,"Se  requiere un limite diario de depositos.");
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(BigDecimal monto) {
    validarMontoNoNegativo(monto);
    validarCantidadDepositosDiarios();

    Movimiento movimientoActual = new Movimiento(LocalDate.now(), monto, Deposito.getINSTANCE());
    this.agregarMovimiento(movimientoActual);
    this.saldo = movimientoActual.calcularValor(saldo, monto);
  }

  public void sacar(BigDecimal monto) {
    validarMontoNoNegativo(monto);
    validarLimiteDiario(getMontoExtraidoA(LocalDate.now()), monto);
    validarSaldoSuficiente(getSaldo(), monto);

    Movimiento movimientoActual = new Movimiento(LocalDate.now(), monto, Extraccion.getINSTANCE());
    this.agregarMovimiento(movimientoActual);
    this.saldo = movimientoActual.calcularValor(saldo, monto);
  }
  private void validarSaldoSuficiente(BigDecimal saldo, BigDecimal monto) {
    if (saldo.subtract(monto).compareTo(BigDecimal.ZERO) == -1) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }
  private void validarLimiteDiario(BigDecimal montoExtraidoAHoy, BigDecimal montoExtraccion) {
    BigDecimal puedoSacarHoy = limiteDiarioExtraccion.subtract(montoExtraidoAHoy);
    if (montoExtraccion.compareTo(puedoSacarHoy) == 1) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + limiteDiarioExtraccion
              + " diarios, extracción restante en el día de hoy: " + (puedoSacarHoy));
    }
  }
  private void validarMontoNoNegativo(BigDecimal monto) {
    if (monto.compareTo(BigDecimal.ZERO) <= 0) {
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

  public BigDecimal getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
            .filter(movimiento -> movimiento.fueExtraido(fecha))
            .map(Movimiento::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public int cantDepositosEnElDia(LocalDate fecha) {
    return getMovimientos().stream()
            .filter(movimiento -> movimiento.fueDepositado(fecha)).collect(Collectors.toList()).size();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public BigDecimal getSaldo() {
    return saldo;
  }

}
